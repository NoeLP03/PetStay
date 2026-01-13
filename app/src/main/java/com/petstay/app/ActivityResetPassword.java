package com.petstay.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityResetPassword extends AppCompatActivity {


    private TextInputEditText etEmail;
    private MaterialButton btnReset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email_recovery);
        btnReset = findViewById(R.id.btn_reset_password);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    etEmail.setError("Ingresa tu correo registrado");
                } else {
                    enviarCorreoRecuperacion(email);
                }
            }
        });
    }

    private void enviarCorreoRecuperacion(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mostrarDialogoExito();
                    } else {
                        Toast.makeText(ActivityResetPassword.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void mostrarDialogoExito() {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Correo Enviado")
                .setMessage("Hemos enviado un enlace a tu correo para que restablezcas tu contraseña. Revisa tu bandeja de entrada o spam.")
                .setCancelable(false)
                .setPositiveButton("Entendido", (dialog, which) -> {
                    finish();
                })
                .show();
    }
}