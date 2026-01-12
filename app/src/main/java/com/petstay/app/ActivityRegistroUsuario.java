package com.petstay.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityRegistroUsuario extends AppCompatActivity {


    private ImageView imgFotoUsuario;
    private EditText editNombreUsuario;
    private EditText editEmailUsuario;
    private EditText editPasswordUsuario;
    private EditText editTelefonoUsuario;
    private Button btnRegistrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro_usuario);

        imgFotoUsuario = findViewById(R.id.imgFotoUsuario);
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editPasswordUsuario = findViewById(R.id.editPasswordUsuario);
        editTelefonoUsuario = findViewById(R.id.editTelefonoUsuario);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }


    private void registrarUsuario() {

        String nombre = editNombreUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String password = editPasswordUsuario.getText().toString();
        String telefono = editTelefonoUsuario.getText().toString();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos obligatorios (Nombre, Email, Contraseña).", Toast.LENGTH_LONG).show();
            return;
        }


        String mensaje = "¡Usuario " + nombre + " registrado exitosamente!";
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

}