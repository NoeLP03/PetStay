package com.petstay.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityPerfil extends AppCompatActivity {

    // Añadimos las nuevas variables para el encabezado
    private TextView tvNombre, tvEmail, tvPhone, tvNombreGrande, tvRolEtiqueta;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Vinculamos todos los IDs, incluidos los nuevos del diseño profesional
        tvNombreGrande = findViewById(R.id.tv_nombre_grande);
        tvRolEtiqueta = findViewById(R.id.tv_rol_etiqueta);

        tvNombre = findViewById(R.id.tv_nombre_perfil);
        tvEmail = findViewById(R.id.tv_email_perfil);
        tvPhone = findViewById(R.id.tv_phone_perfil);

        Button btnVolver = findViewById(R.id.btn_cerrar_perfil);

        obtenerDatosUsuario();

        // Configuramos el botón para volver
        btnVolver.setOnClickListener(v -> finish());
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

                        // Actualizamos el encabezado
                        tvNombreGrande.setText(nombre != null ? nombre : "Usuario");

                        // Mostramos el rol de forma elegante (Primera letra Mayúscula)
                        if (rol != null) {
                            tvRolEtiqueta.setText("Perfil: " + rol.toUpperCase());
                        }

                        // Actualizamos los datos dentro de la tarjeta (sin el prefijo "Nombre: "
                        // porque el diseño nuevo ya tiene etiquetas arriba)
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