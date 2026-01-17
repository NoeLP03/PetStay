package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityCita extends AppCompatActivity {

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

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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

        cargarNombreUsuario();

        llenarSpinner(spinnerTipo, new String[]{"Perro", "Gato"});
        llenarSpinner(spinnerTamano, new String[]{"Pequeño", "Mediano", "Grande"});
        llenarSpinner(spinnerCantidad, new String[]{"1", "2", "3", "4", "5"});

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

    private void mostrarCalendario() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int anio = cal.get(java.util.Calendar.YEAR);
        int mes = cal.get(java.util.Calendar.MONTH);
        int dia = cal.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog dpd = new android.app.DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            etFechaCita.setText(fechaSeleccionada);
        }, anio, mes, dia);

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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, nombresCuidadores);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCuidador.setAdapter(adapter);

                    if (nombreRecibidoDeLista != null) {
                        for (int i = 0; i < nombresCuidadores.size(); i++) {
                            if (nombresCuidadores.get(i).contains(nombreRecibidoDeLista)) {
                                spinnerCuidador.setSelection(i);
                                // Lo limpiamos para que no se re-seleccione si el usuario cambia el filtro manualmente después
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
            Toast.makeText(this, "Completa todos los campos, incluyendo la fecha", Toast.LENGTH_SHORT).show();
        } else if (spinnerCuidador.getSelectedItem().toString().equals("Sin cuidadores disponibles")) {
            Toast.makeText(this, "No hay cuidadores válidos para estos filtros", Toast.LENGTH_SHORT).show();
        } else {
            registrarCitaEnFirestore(mascota, raza, tiempo);
        }
    }

    private void registrarCitaEnFirestore(String mascota, String raza, String tiempo) {
        String idDueno = mAuth.getCurrentUser().getUid();
        int pos = spinnerCuidador.getSelectedItemPosition();
        String idCuidador = listaIdsCuidadores.get(pos);
        String nombreCuidador = spinnerCuidador.getSelectedItem().toString();
        String cantidad = spinnerCantidad.getSelectedItem().toString();

        Map<String, Object> cita = new HashMap<>();
        cita.put("fechaCita", etFechaCita.getText().toString());
        cita.put("idDueno", idDueno);
        cita.put("nombreDueno", etNombreDueno.getText().toString());
        cita.put("idCuidador", idCuidador);
        cita.put("nombreCuidador", nombreCuidador);
        cita.put("nombreMascota", mascota);
        cita.put("raza", raza);
        cita.put("cantidadMascotas", cantidad);
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