package com.petstay.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityPerfil extends AppCompatActivity {

    private TextView tvNombre, tvEmail, tvPhone;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvNombre = findViewById(R.id.tv_nombre_perfil);
        tvEmail = findViewById(R.id.tv_email_perfil);
        tvPhone = findViewById(R.id.tv_phone_perfil);
        Button btnVolver = findViewById(R.id.btn_cerrar_perfil);

        obtenerDatosUsuario();

        btnVolver.setOnClickListener(v -> finish());
        // Dentro del onCreate de tu ActivityPerfil
        btnVolver.setOnClickListener(v -> {
            finish(); // Esto te regresa al MainActivity y activará el onResume que acabas de crear
        });
    }

    private void obtenerDatosUsuario() {
        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection("Usuarios").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String email = documentSnapshot.getString("email");
                        String telefono = documentSnapshot.getString("telefono");

                        tvNombre.setText("Nombre: " + nombre);
                        tvEmail.setText("Email: " + email);
                        tvPhone.setText("Teléfono: " + telefono);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }
}