package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;
    private CuidadorAdapter adaptador;
    private List<Cuidador> listaCuidadores;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });


        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerCuidadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaCuidadores = new ArrayList<>();
        adaptador = new CuidadorAdapter(listaCuidadores,  this);
        recyclerView.setAdapter(adaptador);

        cargarCuidadoresDesdeFirebase();
    }

    private void cargarCuidadoresDesdeFirebase() {

        db.collection("Usuarios")
                .whereEqualTo("rol", "cuidador")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaCuidadores.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String nombre = document.getString("nombre");
                            String ciudad = document.getString("ciudad");
                            String acepta = document.getString("acepta");
                            String capacidad = document.getString("capacidad");

                            Cuidador c = new Cuidador(nombre, ciudad, capacidad, acepta);
                            listaCuidadores.add(c);
                        }
                        adaptador.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error al obtener datos", task.getException());
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            cargarCuidadoresDesdeFirebase();
        }
        else if (id == R.id.nav_perf) {
            startActivity(new Intent(this, ActivityPerfil.class));
        }
        else if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        }
        else if (id == R.id.nav_register) {
            startActivity(new Intent(this, ActivitySelectionRol.class));
        }
        else if (id == R.id.nav_logout) {
            cerrarSesion();
        }
        else if (id == R.id.nav_cita) {
            startActivity(new Intent(this, ActivityCita.class));
        }
        else if (id == R.id.nav_cui) {
            Intent intent = new Intent(MainActivity.this, ActivityListaCuidadores.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void actualizarMenu() {
        NavigationView navigationView = findViewById(R.id.NaviView);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            MenuItem loginItem = menu.findItem(R.id.nav_login);
            MenuItem registerItem = menu.findItem(R.id.nav_register);
            MenuItem logoutItem = menu.findItem(R.id.nav_logout);
            MenuItem profileItem = menu.findItem(R.id.nav_perf);

            if (user != null) {
                if (loginItem != null) loginItem.setVisible(false);
                if (registerItem != null) registerItem.setVisible(false);
                if (logoutItem != null) logoutItem.setVisible(true);
                if (profileItem != null) profileItem.setVisible(true);
            } else {
                if (loginItem != null) loginItem.setVisible(true);
                if (registerItem != null) registerItem.setVisible(true);
                if (logoutItem != null) logoutItem.setVisible(false);
                if (profileItem != null) profileItem.setVisible(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenu();
    }
}