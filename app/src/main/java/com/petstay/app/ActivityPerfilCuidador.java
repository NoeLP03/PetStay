package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityPerfilCuidador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvNombre, tvEmail, tvTelefono, tvDireccion, tvCurp, tvAcepta, tvCapacidad, tvTamano;
    private Button btnAgendar;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String cuidadorId;

    // Drawer references
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cuidador);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Configurar Toolbar y Navigation Drawer (Igual que el Main)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);

        // Manejo del botón atrás moderno
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

        // 2. Vincular vistas con los IDs del XML
        tvNombre = findViewById(R.id.tvNombreCuidador);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvCurp = findViewById(R.id.tvCurp);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvAcepta = findViewById(R.id.tvAceptaMascota);
        tvCapacidad = findViewById(R.id.tvCapacidad);
        tvTamano = findViewById(R.id.tvTamanoMax);
        btnAgendar = findViewById(R.id.btnAgendarCita);

        // 3. Obtener ID del cuidador pasado por Intent
        cuidadorId = getIntent().getStringExtra("cuidadorId");

        if (cuidadorId != null) {
            cargarDatosCuidador();
        }

        // 4. Botón para agendar
        btnAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfilCuidador.this, ActivityCita.class);
            intent.putExtra("cuidadorId", cuidadorId);
            intent.putExtra("nombreCuidador", tvNombre.getText().toString());
            startActivity(intent);
        });
    }

    private void cargarDatosCuidador() {
        mFirestore.collection("Usuarios").document(cuidadorId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        tvNombre.setText(doc.getString("nombre"));
                        tvEmail.setText("📧 " + doc.getString("email"));
                        tvTelefono.setText("📞 " + doc.getString("telefono"));

                        String curp = doc.getString("curp");
                        tvCurp.setText("🆔 CURP: " + (curp != null ? curp : "No disponible"));

                        String calle = doc.getString("calle");
                        String num = doc.getString("no");
                        String colonia = doc.getString("colonia");
                        String ciudad = doc.getString("ciudad");

                        String direccionCompleta = "📍 " + (calle != null ? calle : "") +
                                " #" + (num != null ? num : "") +
                                ", " + (colonia != null ? colonia : "") +
                                ", " + (ciudad != null ? ciudad : "");

                        tvDireccion.setText(direccionCompleta);
                        tvAcepta.setText("🐾 Acepta: " + doc.getString("acepta"));
                        tvCapacidad.setText("🏠 Capacidad: " + doc.getString("capacidad") + " mascotas");
                        tvTamano.setText("📏 Tamaño máx: " + doc.getString("tamanoMax"));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
            startActivity(new Intent(this, ActivityListaCuidadores.class));
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
        NavigationView navigationView = findViewById(R.id.NaviView);
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
}