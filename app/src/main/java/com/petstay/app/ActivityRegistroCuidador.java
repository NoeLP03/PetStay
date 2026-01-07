package com.petstay.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityRegistroCuidador extends AppCompatActivity {

    EditText txtNombre, txtEmail, txtPassword, txtTelefono, txtDireccion, txtINE;
    Button btnRegistrar;
    ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidador);

        // Conectar con los elementos del layout
        txtNombre = findViewById(R.id.editNombre);
        txtEmail = findViewById(R.id.editEmail);
        txtPassword = findViewById(R.id.editPassword);
        txtTelefono = findViewById(R.id.editTelefono);
        txtDireccion = findViewById(R.id.editDireccionCuidador);
        imgFoto = findViewById(R.id.imgINE);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {

        String nombre = txtNombre.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String ine = txtINE.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() ||
                telefono.isEmpty() || direccion.isEmpty() || ine.isEmpty()) {

            Toast.makeText(this,
                    "Por favor, completa todos los campos.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Usuario registrado correctamente 🎉",
                Toast.LENGTH_LONG).show();
    }
}
