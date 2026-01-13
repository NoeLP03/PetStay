package com.petstay.app;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityForgotPassword extends AppCompatActivity {

    // Cambiamos TextInputEditText por AppCompatEditText
    private androidx.appcompat.widget.AppCompatEditText etEmail, etPhone;

    // Cambiamos MaterialButton por Button normal
    private android.widget.Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Ahora estos findViewById sí encontrarán el tipo de vista correcto
        etEmail = findViewById(R.id.et_email_recovery);
        etPhone = findViewById(R.id.et_phone_recovery);
        btnVerify = findViewById(R.id.btn_send_recovery);

        // La línea roja debería desaparecer después de un "Rebuild Project"
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
    }

    private void validarDatos() {
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("El correo es obligatorio");
            return;
        }

        if (phone.isEmpty() || phone.length() < 8) {
            etPhone.setError("Ingresa un número de teléfono válido");
            return;
        }

        // Simulación de envío de datos
        enviarSolicitud(email, phone);
    }

    private void enviarSolicitud(String email, String phone) {
        com.google.firebase.auth.FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show();
                        finish(); // Regresa al Login automáticamente
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}