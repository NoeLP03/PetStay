package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.material.button.MaterialButton; // Asegúrate de este import
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {

    private AppCompatEditText etEmail, etPassword;
    private android.widget.Button btnLogin;
    private Button btnForgot;         // El de "olvidaste contraseña" es un Button normal en tu XML
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // 1. Vinculación con los IDs EXACTOS de tu XML
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgot = findViewById(R.id.btn_forgot_password);

        // 2. Configuración del botón de Login
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    iniciarSesion(email, pass);
                }
            });
        }

        // 3. Configuración del botón Forgot
        if (btnForgot != null) {
            btnForgot.setOnClickListener(v -> {
                // Esto ya no fallará porque ya lo agregamos al Manifest
                Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                startActivity(intent);
            });
        }
    }

    private void iniciarSesion(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityLogin.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityLogin.this, "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}