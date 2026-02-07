package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityCita extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText etNombreDueno, etNombreMascota, etRaza, etTiempoCuidado, etFechaCita;
    private Spinner spinnerTipo, spinnerTamano, spinnerCuidador, spinnerCantidad;
    private TextView tvAvisoNoDisponible;
    private Button btnRegister;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private List<String> listaIdsCuidadores = new ArrayList<>();
    private String nombreRecibidoDeLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);

        // --- INICIALIZAR FIREBASE ---
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // --- CONFIGURACIÓN DEL MENÚ Y TOOLBAR ---
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Al tocar el icono de la barra, abre el menú lateral
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(this);

        // --- VINCULAR VISTAS ---
        nombreRecibidoDeLista = getIntent().getStringExtra("nombre_cuidador");
        etFechaCita = findViewById(R.id.etFechaCita);
        etFechaCita.setOnClickListener(v -> mostrarCalendario());
        etNombreDueno = findViewById(R.id.etNombreDueno);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etTiempoCuidado = findViewById(R.id.etTiempoCuidado);
        spinnerTipo = findViewById(R.id.spinnerTipoCita);
        spinnerTamano = findViewById(R.id.spinnerTamanoCita);
        spinnerCuidador = findViewById(R.id.spinnerCuidador);
        tvAvisoNoDisponible = findViewById(R.id.tvAvisoNoDisponible);
        btnRegister = findViewById(R.id.btnRegister);
        spinnerCantidad = findViewById(R.id.spinnerCantidadMascotas);

        // --- CARGAR DATOS ---
        cargarNombreUsuario();
        llenarSpinner(spinnerTipo, new String[]{"Perro", "Gato"});
        llenarSpinner(spinnerTamano, new String[]{"Pequeño", "Mediano", "Grande"});
        llenarSpinner(spinnerCantidad, new String[]{"1", "2", "3", "4", "5"});

        // Listener para filtrar cuidadores automáticamente al cambiar tipo o tamaño
        AdapterView.OnItemSelectedListener filtroListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                buscarCuidadoresDisponibles();
            }
            @Override
            public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerTipo.setOnItemSelectedListener(filtroListener);
        spinnerTamano.setOnItemSelectedListener(filtroListener);

        btnRegister.setOnClickListener(v -> validarYRegistrarCita());
    }

    // --- LÓGICA DE NAVEGACIÓN DEL MENÚ LATERAL ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_perf) {
            startActivity(new Intent(this, ActivityPerfil.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        } else if (id == R.id.nav_register) {
            startActivity(new Intent(this, ActivitySelectionRol.class));
        } else if (id == R.id.nav_logout) {
            cerrarSesion();
        } else if (id == R.id.nav_cui) {
            startActivity(new Intent(this, ActivityListaCuidadores.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // --- ACTUALIZAR VISIBILIDAD DEL MENÚ SEGÚN SESIÓN ---
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

    private void cerrarSesion() {
        mAuth.signOut();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // --- MÉTODOS DE LA CITA ---
    private void mostrarCalendario() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        android.app.DatePickerDialog dpd = new android.app.DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            etFechaCita.setText(fechaSeleccionada);
        }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH));

        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    private void cargarNombreUsuario() {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Usuarios").document(uid).get().addOnSuccessListener(doc -> {
                if (doc.exists()) {
                    etNombreDueno.setText(doc.getString("nombre"));
                }
            });
        }
    }

    private void buscarCuidadoresDisponibles() {
        String tipoBuscado = spinnerTipo.getSelectedItem().toString();
        String tamanoBuscado = spinnerTamano.getSelectedItem().toString();

        mFirestore.collection("Usuarios")
                .whereEqualTo("rol", "cuidador")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> nombresCuidadores = new ArrayList<>();
                    listaIdsCuidadores.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String acepta = doc.getString("acepta");
                        String tamanoMax = doc.getString("tamanoMax");

                        boolean tipoOk = acepta != null && (acepta.equals(tipoBuscado) || acepta.equals("Ambos"));
                        if (tipoOk && esCompatible(tamanoBuscado, tamanoMax)) {
                            nombresCuidadores.add(doc.getString("nombre") + " (" + doc.getString("ciudad") + ")");
                            listaIdsCuidadores.add(doc.getId());
                        }
                    }

                    if (nombresCuidadores.isEmpty()) {
                        tvAvisoNoDisponible.setVisibility(View.VISIBLE);
                        nombresCuidadores.add("Sin cuidadores disponibles");
                    } else {
                        tvAvisoNoDisponible.setVisibility(View.GONE);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCuidadores);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCuidador.setAdapter(adapter);

                    // Auto-seleccionar si venimos de la lista de perfiles
                    if (nombreRecibidoDeLista != null) {
                        for (int i = 0; i < nombresCuidadores.size(); i++) {
                            if (nombresCuidadores.get(i).contains(nombreRecibidoDeLista)) {
                                spinnerCuidador.setSelection(i);
                                nombreRecibidoDeLista = null;
                                break;
                            }
                        }
                    }
                });
    }

    private boolean esCompatible(String miMascota, String capacidadMax) {
        if (capacidadMax == null) return false;
        if (capacidadMax.equals("Grande")) return true;
        if (capacidadMax.equals("Mediano")) return !miMascota.equals("Grande");
        return miMascota.equals("Pequeño");
    }

    private void validarYRegistrarCita() {
        String mascota = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String tiempo = etTiempoCuidado.getText().toString().trim();
        String fecha = etFechaCita.getText().toString().trim();

        if (listaIdsCuidadores.isEmpty() || mascota.isEmpty() || raza.isEmpty() || tiempo.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            registrarCitaEnFirestore(mascota, raza, tiempo);
        }
    }

    private void registrarCitaEnFirestore(String mascota, String raza, String tiempo) {
        String idDueno = mAuth.getCurrentUser().getUid();
        int pos = spinnerCuidador.getSelectedItemPosition();
        String idCuidador = listaIdsCuidadores.get(pos);

        Map<String, Object> cita = new HashMap<>();
        cita.put("fechaCita", etFechaCita.getText().toString());
        cita.put("idDueno", idDueno);
        cita.put("nombreDueno", etNombreDueno.getText().toString());
        cita.put("idCuidador", idCuidador);
        cita.put("nombreCuidador", spinnerCuidador.getSelectedItem().toString());
        cita.put("nombreMascota", mascota);
        cita.put("raza", raza);
        cita.put("tipoMascota", spinnerTipo.getSelectedItem().toString());
        cita.put("tamanoMascota", spinnerTamano.getSelectedItem().toString());
        cita.put("tiempoCuidado", tiempo);
        cita.put("estado", "pendiente");

        mFirestore.collection("Citas").add(cita).addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "Cita enviada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void llenarSpinner(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}