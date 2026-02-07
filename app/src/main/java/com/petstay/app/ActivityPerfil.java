package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityPerfil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvNombre, tvEmail, tvPhone, tvNombreGrande, tvRolEtiqueta;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

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

        // --- VINCULAR VISTAS ---
        tvNombreGrande = findViewById(R.id.tv_nombre_grande);
        tvRolEtiqueta = findViewById(R.id.tv_rol_etiqueta);
        tvNombre = findViewById(R.id.tv_nombre_perfil);
        tvEmail = findViewById(R.id.tv_email_perfil);
        tvPhone = findViewById(R.id.tv_phone_perfil);

        obtenerDatosUsuario();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_cui) {
            startActivity(new Intent(this, ActivityListaCuidadores.class));
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

    private void obtenerDatosUsuario() {
        if (mAuth.getCurrentUser() == null) return;

        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection("Usuarios").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String email = documentSnapshot.getString("email");
                        String telefono = documentSnapshot.getString("telefono");
                        String rol = documentSnapshot.getString("rol");

                        tvNombreGrande.setText(nombre != null ? nombre : "Usuario");

                        if (rol != null) {
                            tvRolEtiqueta.setText("Perfil: " + rol.toUpperCase());
                        }

                        tvNombre.setText(nombre);
                        tvEmail.setText(email);
                        tvPhone.setText(telefono);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                });
    }
}