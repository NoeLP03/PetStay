package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityResetPassword extends AppCompatActivity {

    private TextInputEditText etNewPass, etConfirmPass;
    private MaterialButton btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPass = findViewById(R.id.et_new_password);
        etConfirmPass = findViewById(R.id.et_confirm_password);
        btnUpdate = findViewById(R.id.btn_update_password);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = etNewPass.getText().toString();
                String pass2 = etConfirmPass.getText().toString();

                if (validarContrasenas(pass1, pass2)) {
                    // Acción si todo está bien
                    Toast.makeText(ActivityResetPassword.this, "¡Contraseña actualizada!", Toast.LENGTH_SHORT).show();

                    // Al terminar, enviamos al usuario al Login de nuevo
                    Intent i = new Intent(ActivityResetPassword.this, ActivityLogin.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }

    private boolean validarContrasenas(String p1, String p2) {
        if (p1.isEmpty() || p2.isEmpty()) {
            Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (p1.length() < 6) {
            etNewPass.setError("Mínimo 6 caracteres");
            return false;
        }

        if (!p1.equals(p2)) {
            etConfirmPass.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    private void mostrarDialogoExito() {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("¡Cambio Exitoso!")
                .setMessage("Tu contraseña ha sido actualizada correctamente. Ahora puedes iniciar sesión con tus nuevas credenciales.")
                .setCancelable(false) // Evita que el usuario lo cierre tocando fuera
                .setPositiveButton("Ir al Login", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // Navegar al Login
                        Intent i = new Intent(ActivityResetPassword.this, ActivityLogin.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }
}