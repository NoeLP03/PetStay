package com.petstay.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityResetPassword extends AppCompatActivity {

    private EditText newPass, confirmPass;
    private Button btnUpdate;
    private ImageView isShowPassword1, isShowPassword2;
    private boolean isPasswordVisible1 = false, isPasswordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // --- CONFIGURACIÓN TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> mostrarDialogoSalir());

        // Inicializar vistas
        newPass = findViewById(R.id.et_new_password);
        confirmPass = findViewById(R.id.et_confirm_password);
        btnUpdate = findViewById(R.id.btn_update_password);
        isShowPassword1 = findViewById(R.id.iv_show_password1);
        isShowPassword2 = findViewById(R.id.iv_show_password2);

        isShowPassword1.setOnClickListener(v -> {
            isPasswordVisible1 = !isPasswordVisible1;
            togglePasswordVisibility(newPass, isShowPassword1, isPasswordVisible1);
        });

        isShowPassword2.setOnClickListener(v -> {
            isPasswordVisible2 = !isPasswordVisible2;
            togglePasswordVisibility(confirmPass, isShowPassword2, isPasswordVisible2);
        });

        btnUpdate.setOnClickListener(v -> {
            String p1 = newPass.getText().toString();
            String p2 = confirmPass.getText().toString();
            if (validarContrasenas(p1, p2)) {
                actualizarEnFirebase(p1);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mostrarDialogoSalir();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(android.R.drawable.ic_menu_view);
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean validarContrasenas(String p1, String p2) {
        if (p1.length() < 8 || !p1.matches(".*[A-Z].*") || !p1.matches(".*\\d.*") || !p1.matches(".*[!@#$%^&*()].*")) {
            newPass.setError("No cumple los requisitos de seguridad");
            return false;
        }
        if (!p1.equals(p2)) {
            confirmPass.setError("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    private void actualizarEnFirebase(String nuevaContra) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(nuevaContra).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mostrarDialogoExito();
                } else {
                    Toast.makeText(this, "Error: Reautenticación requerida", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void mostrarDialogoExito() {
        new AlertDialog.Builder(this)
                .setTitle("¡Éxito!")
                .setMessage("Contraseña actualizada correctamente.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", (dialog, which) -> finish())
                .show();
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(this)
                .setTitle("¿Salir?")
                .setMessage("No se guardarán los cambios.")
                .setPositiveButton("Salir", (dialog, which) -> finish())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}