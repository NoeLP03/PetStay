package com.petstay.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityRegistroUsuario extends AppCompatActivity {

    // 1. Declaración de las vistas
    private ImageView imgFotoUsuario;
    private EditText editNombreUsuario;
    private EditText editEmailUsuario;
    private EditText editPasswordUsuario;
    private EditText editTelefonoUsuario;
    private Button btnRegistrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout (asumiendo que tu archivo XML se llama activity_main.xml)
        setContentView(R.layout.activity_registro_usuario);

        // 2. Inicialización de las vistas (Binding)
        // Usamos los IDs que definiste en el archivo XML
        imgFotoUsuario = findViewById(R.id.imgFotoUsuario);
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editPasswordUsuario = findViewById(R.id.editPasswordUsuario);
        editTelefonoUsuario = findViewById(R.id.editTelefonoUsuario);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        // 3. Establecer el Listener para el botón de registro
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    /**
     * Método que se llama al presionar el botón de "Registrar Usuario".
     * Aquí se extraen los datos y se realiza la lógica de registro.
     */
    private void registrarUsuario() {
        // Obtener los valores de los campos de texto como Strings
        String nombre = editNombreUsuario.getText().toString();
        String email = editEmailUsuario.getText().toString();
        String password = editPasswordUsuario.getText().toString();
        String telefono = editTelefonoUsuario.getText().toString();

        // **LÓGICA DE VALIDACIÓN BÁSICA**
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos obligatorios (Nombre, Email, Contraseña).", Toast.LENGTH_LONG).show();
            return;
        }

        // Aquí iría la lógica real:
        // - Validar el formato del email y la longitud de la contraseña.
        // - Guardar los datos en una base de datos local (SQLite, Room).
        // - Enviar los datos a un servidor (Firebase, API REST).

        // Muestra un mensaje de éxito (ejemplo)
        String mensaje = "¡Usuario " + nombre + " registrado exitosamente!";
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    // Opcionalmente, puedes añadir lógica para la ImageView aquí (ej: abrir la galería)
    // imgFotoUsuario.setOnClickListener(new View.OnClickListener() { ... });
}