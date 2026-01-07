package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityLogin extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgot = findViewById(R.id.btn_forgot_password);

        // Acción al presionar "Iniciar Sesión"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Aquí iría tu lógica de validación (Firebase, API, etc.)
                    Toast.makeText(ActivityLogin.this, "Ingresando...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción al presionar "Olvidaste tu contraseña"
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Por ejemplo, abrir una nueva actividad de recuperación
                // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                // startActivity(intent);
                Toast.makeText(ActivityLogin.this, "Ir a recuperación de cuenta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}