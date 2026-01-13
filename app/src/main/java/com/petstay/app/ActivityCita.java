package com.petstay.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityCita extends AppCompatActivity {

    // 1. Declaramos las variables basadas en tus IDs del XML
    private EditText etNombreDueno, etNombreMascota, etRaza, etTiempoCuidado, etSucursal;
    private Button btnRegister;

    // Firebase
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);

        // 2. Inicializamos Firebase
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 3. Vinculamos con los IDs del XML
        etNombreDueno = findViewById(R.id.etNombreDueno);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etTiempoCuidado = findViewById(R.id.etTiempoCuidado);
        etSucursal = findViewById(R.id.etSucursal);
        btnRegister = findViewById(R.id.btnRegister);

        // 4. Configuramos el botón
        btnRegister.setOnClickListener(v -> {
            validarYRegistrarCita();
        });
    }

    private void validarYRegistrarCita() {
        String dueno = etNombreDueno.getText().toString().trim();
        String mascota = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String tiempo = etTiempoCuidado.getText().toString().trim();
        String sucursal = etSucursal.getText().toString().trim();

        // Validación simple
        if (dueno.isEmpty() || mascota.isEmpty() || raza.isEmpty() || tiempo.isEmpty() || sucursal.isEmpty()) {
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            subirCitaAFirestore(dueno, mascota, raza, tiempo, sucursal);
        }
    }

    private void subirCitaAFirestore(String dueno, String mascota, String raza, String tiempo, String sucursal) {
        // Obtenemos el ID del usuario actual para saber quién hizo la cita
        String idUsuario = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "anonimo";

        Map<String, Object> map = new HashMap<>();
        map.put("nombreDueno", dueno);
        map.put("nombreMascota", mascota);
        map.put("raza", raza);
        map.put("tiempoCuidado", tiempo);
        map.put("sucursal", sucursal);
        map.put("idUsuario", idUsuario); // Referencia al usuario que agenda
        map.put("estado", "pendiente");   // Estado inicial de la cita

        // Guardamos en una colección llamada "Citas"
        mFirestore.collection("Citas").add(map)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Cita agendada correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerramos la pantalla al terminar
                })
                .addOnFailureListener(e -> {
                    // Esto te dirá exactamente por qué falló (ej. Permisos, Red, etc.)
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}