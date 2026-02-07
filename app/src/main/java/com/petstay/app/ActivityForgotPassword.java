package com.petstay.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPassword extends AppCompatActivity {

    private AppCompatEditText etEmail, etPhone;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // --- CONFIGURACIÓN DE LA TOOLBAR (Flecha atrás) ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Al tocar la flecha, simplemente cerramos la actividad para volver al Login
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- INICIALIZAR VISTAS ---
        etEmail = findViewById(R.id.et_email_recovery);
        etPhone = findViewById(R.id.et_phone_recovery);
        btnVerify = findViewById(R.id.btn_send_recovery);

        btnVerify.setOnClickListener(v -> validarDatos());
    }

    private void validarDatos() {
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("El correo es obligatorio");
            return;
        }

        // Firebase reset password solo requiere el email,
        // pero validamos el teléfono como medida de seguridad extra de tu diseño
        if (phone.isEmpty() || phone.length() < 8) {
            etPhone.setError("Ingresa un número de teléfono válido");
            return;
        }

        enviarSolicitud(email);
    }

    private void enviarSolicitud(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Se ha enviado un enlace a tu correo", Toast.LENGTH_LONG).show();
                        finish(); // Regresa al Login
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}