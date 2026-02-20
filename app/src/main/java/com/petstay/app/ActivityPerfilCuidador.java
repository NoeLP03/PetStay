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

    // Vistas del Perfil
    private TextView tvNombre, tvEmail, tvTelefono, tvCurp, tvDireccion;
    private TextView tvCalle, tvColonia, tvCodigoPostal; // Vistas de ubicación
    private TextView tvAcepta, tvCapacidad, tvTamano;

    private Button btnAgendar;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String cuidadorId;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cuidador);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Configurar Toolbar y Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);

        // 2. Vincular vistas con los IDs del XML (Asegúrate que coincidan con tu Layout)
        tvNombre = findViewById(R.id.tvNombreCuidador);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvCurp = findViewById(R.id.tvCurp);

        // Campos de ubicación detallada
        tvCalle = findViewById(R.id.tvCalle);
        tvColonia = findViewById(R.id.tvColonia);
        tvCodigoPostal = findViewById(R.id.tvCodigoPostal);
        tvDireccion = findViewById(R.id.tvDireccion); // El pie de página 📍

        // Detalles del servicio
        tvAcepta = findViewById(R.id.tvAceptaMascota);
        tvCapacidad = findViewById(R.id.tvCapacidad);
        tvTamano = findViewById(R.id.tvTamanoMax);
        btnAgendar = findViewById(R.id.btnAgendarCita);

        // 3. Obtener ID del cuidador desde el Intent
        cuidadorId = getIntent().getStringExtra("cuidadorId");

        if (cuidadorId != null) {
            cargarDatosCuidador();
        }

        btnAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityCita.class);
            intent.putExtra("cuidadorId", cuidadorId);
            intent.putExtra("nombreCuidador", tvNombre.getText().toString());
            startActivity(intent);
        });
    }

    private void cargarDatosCuidador() {
        mFirestore.collection("Usuarios").document(cuidadorId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        // --- DATOS PERSONALES ---
                        tvNombre.setText(doc.getString("nombre"));
                        tvEmail.setText("📧 " + doc.getString("email"));
                        tvTelefono.setText("📞 " + doc.getString("telefono"));
                        tvCurp.setText("🆔 CURP: " + doc.getString("curp"));

                        // --- UBICACIÓN (Claves individuales de Firebase) ---
                        String calle = doc.getString("calle");
                        String num = doc.getString("numeroCasa");
                        String colonia = doc.getString("colonia");
                        String cpValue = doc.getString("cp"); // Clave 'cp' de tu Firebase
                        String ciudad = doc.getString("ciudad");

                        // Seteo de los campos ahora visibles y separados
                        tvCalle.setText("🏠 Calle: " + (calle != null ? calle : "---") + " #" + (num != null ? num : "S/N"));
                        tvColonia.setText("🏘️ Colonia: " + (colonia != null ? colonia : "---"));
                        tvCodigoPostal.setText("📮 C.P.: " + (cpValue != null ? cpValue : "---"));

                        // Pie de tarjeta para la Ciudad
                        tvDireccion.setText("📍 Ciudad: " + (ciudad != null ? ciudad : "---"));

                        // --- DETALLES SERVICIO ---
                        tvAcepta.setText("🐾 Acepta: " + doc.getString("acepta"));
                        tvCapacidad.setText("🏠 Capacidad: " + doc.getString("capacidad") + " mascotas");
                        tvTamano.setText("📏 Tamaño máx: " + doc.getString("tamanoMax"));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }

    // --- MÉTODOS DEL MENU / DRAWER ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_logout) {
            cerrarSesion();
        } else if (id == R.id.nav_cui) {
            startActivity(new Intent(this, ActivityListaCuidadores.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ActivityLogOut.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenu();
    }

    private void actualizarMenu() {
        NavigationView navigationView = findViewById(R.id.NaviView);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            FirebaseUser user = mAuth.getCurrentUser();
            menu.findItem(R.id.nav_login).setVisible(user == null);
            menu.findItem(R.id.nav_register).setVisible(user == null);
            menu.findItem(R.id.nav_logout).setVisible(user != null);
        }
    }
}