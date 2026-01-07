package com.petstay.app;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityForgotPassword extends AppCompatActivity {

    private TextInputEditText etEmail, etPhone;
    private MaterialButton btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.et_email_recovery);
        etPhone = findViewById(R.id.et_phone_recovery);
        btnVerify = findViewById(R.id.btn_send_recovery);

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
        // Aquí conectarías con tu base de datos o API
        Toast.makeText(this, "Verificando datos para: " + email, Toast.LENGTH_LONG).show();

        // Ejemplo: Si los datos son correctos, podrías enviar un código SMS o email
    }
}