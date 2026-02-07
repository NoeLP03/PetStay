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

        obtenerCuidadores();
    }

    // Lógica para los clics del menú lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_perf) {
            startActivity(new Intent(this, ActivityPerfil.class));
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void actualizarMenu() {
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            FirebaseUser user = mAuth.getCurrentUser();
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
    }

    private void obtenerCuidadores() {
        listaCuidadores = new ArrayList<>();
        mFirestore.collection("Usuarios")
                .whereEqualTo("rol", "cuidador")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Cuidador c = doc.toObject(Cuidador.class);
                        listaCuidadores.add(c);
                    }
                    adapter = new CuidadorAdapter(listaCuidadores, this);
                    recyclerView.setAdapter(adapter);
                });
    }
}