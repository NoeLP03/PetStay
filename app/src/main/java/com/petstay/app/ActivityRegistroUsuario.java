package com.petstay.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistroUsuario extends AppCompatActivity {

    private ImageView imgFotoUsuario;
    private EditText editNombreUsuario;
    private EditText editEmailUsuario;
    private EditText editPasswordUsuario;
    private EditText editTelefonoUsuario;
    private Button btnRegistrarUsuario;

    // Instancias de Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Inicializar Vistas
        imgFotoUsuario = findViewById(R.id.imgFotoUsuario);
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editPasswordUsuario = findViewById(R.id.editPasswordUsuario);
        editTelefonoUsuario = findViewById(R.id.editTelefonoUsuario);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = editNombreUsuario.getText().toString().trim();
        String email = editEmailUsuario.getText().toString().trim();
        String password = editPasswordUsuario.getText().toString().trim();
        String telefono = editTelefonoUsuario.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String id = mAuth.getCurrentUser().getUid();

                        guardarDatosEnFirestore(id, nombre, email, telefono);
                    } else {
                        Toast.makeText(ActivityRegistroUsuario.this, "Error al registrar: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosEnFirestore(String id, String nombre, String email, String telefono) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nombre", nombre);
        map.put("email", email);
        map.put("telefono", telefono);

        mFirestore.collection("Usuarios").document(id).set(map)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActivityRegistroUsuario.this, "Â¡Usuario guardado exitosamente!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ActivityRegistroUsuario.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}