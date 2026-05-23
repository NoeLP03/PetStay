package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistroUsuario extends AppCompatActivity {

    private EditText editNombreUsuario, editEmailUsuario, editPasswordUsuario, editTelefonoUsuario;
    private Button btnRegistrarUsuario;
    private ImageView btnAtrasRegistro;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Vincular componentes de la pantalla
        btnAtrasRegistro = findViewById(R.id.btnAtrasRegistro);
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editPasswordUsuario = findViewById(R.id.editPasswordUsuario);
        editTelefonoUsuario = findViewById(R.id.editTelefonoUsuario);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        // Acción del botón de regreso (Flecha superior izquierda)
        btnAtrasRegistro.setOnClickListener(v -> finish());

        // Acción del botón Registrarse
        btnRegistrarUsuario.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = editNombreUsuario.getText().toString().trim();
        String email = editEmailUsuario.getText().toString().trim();
        String password = editPasswordUsuario.getText().toString().trim();
        String telefono = editTelefonoUsuario.getText().toString().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de contraseña
        if (password.length() < 6) {
            editPasswordUsuario.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Crear usuario en Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser() != null) {
                            String id = mAuth.getCurrentUser().getUid();
                            guardarDatosEnFirestore(id, nombre, email, telefono);
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                        Toast.makeText(this, "Error de registro: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosEnFirestore(String id, String nombre, String email, String telefono) {
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", id);
        usuarioMap.put("nombre", nombre);
        usuarioMap.put("email", email);
        usuarioMap.put("telefono", telefono);
        usuarioMap.put("rol", "dueño"); // Rol fijo para identificarlo en las consultas

        mFirestore.collection("Usuarios").document(id).set(usuarioMap)
                .addOnSuccessListener(aVoid -> {
                    // Redirección limpia tras éxito
                    Intent intent = new Intent(ActivityRegistroUsuario.this, ActivityRegistroExitoso.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}