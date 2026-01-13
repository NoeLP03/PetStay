package com.petstay.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityResetPassword extends AppCompatActivity {

    private EditText newPass;
    private EditText confirmPass;
    private Button btnUpdate;
    private ImageView isShowPassword1;
    private ImageView isShowPassword2;
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Inicializar vistas
        newPass = findViewById(R.id.et_new_password);
        confirmPass = findViewById(R.id.et_confirm_password);
        btnUpdate = findViewById(R.id.btn_update_password);
        isShowPassword1 = findViewById(R.id.iv_show_password1);
        isShowPassword2 = findViewById(R.id.iv_show_password2);

        // Configurar ojos para mostrar/ocultar contraseña
        isShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible1 = !isPasswordVisible1;
                togglePasswordVisibility(newPass, isShowPassword1, isPasswordVisible1);
            }
        });

        isShowPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible2 = !isPasswordVisible2;
                togglePasswordVisibility(confirmPass, isShowPassword2, isPasswordVisible2);
            }
        });

        // Configurar botón de actualizar
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = newPass.getText().toString();
                String pass2 = confirmPass.getText().toString();

                if (validarContrasenas(pass1, pass2)) {
                    mostrarDialogoExito();
                }
            }
        });

        // Configurar botón Back (moderno)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mostrarDialogoSalir();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView, boolean isVisible) {
        if (isVisible) {
            // Mostrar texto
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            imageView.setColorFilter(Color.parseColor("#666666"));
        } else {
            // Ocultar texto
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(android.R.drawable.ic_menu_view);
            imageView.setColorFilter(Color.parseColor("#666666"));
        }
        // Mover cursor al final
        editText.setSelection(editText.getText().length());
    }

    private boolean validarContrasenas(String p1, String p2) {
        // Limpiar errores anteriores
        newPass.setError(null);
        confirmPass.setError(null);

        // Validar campos vacíos
        if (p1.isEmpty() || p2.isEmpty()) {
            Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar longitud (8 caracteres como dice tu XML)
        if (p1.length() < 8) {
            newPass.setError("Mínimo 8 caracteres");
            newPass.requestFocus();
            return false;
        }

        // Validar mayúscula
        if (!p1.matches(".*[A-Z].*")) {
            newPass.setError("Debe contener al menos una mayúscula");
            newPass.requestFocus();
            return false;
        }

        // Validar número
        if (!p1.matches(".*\\d.*")) {
            newPass.setError("Debe contener al menos un número");
            newPass.requestFocus();
            return false;
        }

        // Validar carácter especial
        if (!p1.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            newPass.setError("Debe contener al menos un carácter especial");
            newPass.requestFocus();
            return false;
        }

        // Validar que coincidan
        if (!p1.equals(p2)) {
            confirmPass.setError("Las contraseñas no coinciden");
            confirmPass.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarDialogoExito() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("¡Cambio Exitoso!")
                .setMessage("Tu contraseña ha sido actualizada correctamente. Ahora puedes iniciar sesión con tus nuevas credenciales.")
                .setCancelable(false)
                .setPositiveButton("Ir al Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ActivityResetPassword.this, ActivityLogin.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }

    private void mostrarDialogoSalir() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("¿Salir sin guardar?")
                .setMessage("Los cambios no guardados se perderán.")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}