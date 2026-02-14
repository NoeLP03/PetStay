package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ActivityListaCuidadores extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private CuidadorAdapter adapter;
    private List<Cuidador> listaCuidadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cuidadores);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // --- CONFIGURACIÓN DE BARRA Y MENÚ ---
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);

        // --- CONFIGURACIÓN RECYCLERVIEW ---
        recyclerView = findViewById(R.id.rvCuidadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializamos la lista y el adaptador de una vez
        listaCuidadores = new ArrayList<>();
        adapter = new CuidadorAdapter(listaCuidadores, this);
        recyclerView.setAdapter(adapter);

        obtenerCuidadores();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_perf) {
            startActivity(new Intent(this, ActivityPerfil.class));
        } else if (id == R.id.nav_logout) {
            cerrarSesion();
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void actualizarMenu() {
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            FirebaseUser user = mAuth.getCurrentUser();

            // Si hay usuario, ocultamos Login/Registro y mostramos Perfil/Logout
            menu.findItem(R.id.nav_login).setVisible(user == null);
            menu.findItem(R.id.nav_register).setVisible(user == null);
            menu.findItem(R.id.nav_logout).setVisible(user != null);
            menu.findItem(R.id.nav_perf).setVisible(user != null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenu();
        // Recargamos por si algún cuidador actualizó sus datos
        obtenerCuidadores();
    }

    private void obtenerCuidadores() {
        mFirestore.collection("Usuarios")
                .whereEqualTo("rol", "cuidador")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaCuidadores.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Cuidador c = doc.toObject(Cuidador.class);
                        if (c != null) {
                            // IMPORTANTE: Seteamos el ID manualmente desde el documento
                            c.setId(doc.getId());
                            listaCuidadores.add(c);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar la lista", Toast.LENGTH_SHORT).show();
                });
    }
}