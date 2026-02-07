package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistroUsuario extends AppCompatActivity {

    private EditText editNombreUsuario, editEmailUsuario, editPasswordUsuario, editTelefonoUsuario;
    private Button btnRegistrarUsuario;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // --- CONFIGURACIÓN TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- VINCULAR VISTAS ---
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editPasswordUsuario = findViewById(R.id.editPasswordUsuario);
        editTelefonoUsuario = findViewById(R.id.editTelefonoUsuario);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        btnRegistrarUsuario.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = editNombreUsuario.getText().toString().trim();
        String email = editEmailUsuario.getText().toString().trim();
        String password = editPasswordUsuario.getText().toString().trim();
        String telefono = editTelefonoUsuario.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            editPasswordUsuario.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = mAuth.getCurrentUser().getUid();
                        guardarDatosEnFirestore(id, nombre, email, telefono);
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosEnFirestore(String id, String nombre, String email, String telefono) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nombre", nombre);
        map.put("email", email);
        map.put("telefono", telefono);
        map.put("rol", "dueño"); // <--- IMPORTANTE: Definimos el rol aquí

        mFirestore.collection("Usuarios").document(id).set(map)
                .addOnSuccessListener(aVoid -> {
                    // Navegamos a la pantalla de éxito
                    Intent intent = new Intent(ActivityRegistroUsuario.this, ActivityRegistroExitoso.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar en base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}