package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ActivityRegistroCuidador extends AppCompatActivity {

    private EditText txtNombre, txtEmail, txtPassword, txtTelefono, txtCurp, txtColonia, txtCalle, txtNumCasa, txtCP;
    private Spinner spinnerCiudad;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidador);

        // --- CONFIGURACIÓN DE TOOLBAR (Flecha atrás) ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Al tocar la flecha, regresa a la selección de rol
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- VINCULAR VISTAS ---
        txtNombre = findViewById(R.id.editNombre);
        txtEmail = findViewById(R.id.editEmail);
        txtPassword = findViewById(R.id.editPassword);
        txtTelefono = findViewById(R.id.editTelefono);
        txtCurp = findViewById(R.id.editCurp);
        txtColonia = findViewById(R.id.editColonia);
        txtCalle = findViewById(R.id.editCalle);
        txtNumCasa = findViewById(R.id.editNumeroCasa);
        txtCP = findViewById(R.id.editCP);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        btnSiguiente = findViewById(R.id.btnRegistrar);

        // --- LLENAR SPINNER CIUDADES ---
        String[] ciudades = {"Culiacán", "Mazatlán", "Los Mochis", "Guasave", "Guamúchil", "Navolato", "Escuinapa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ciudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapter);

        // --- BOTÓN SIGUIENTE ---
        btnSiguiente.setOnClickListener(v -> {
            if (validarCampos()) {
                irAlPaso2();
            }
        });
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String pass = txtPassword.getText().toString().trim();
        String curp = txtCurp.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || pass.length() < 6) {
            Toast.makeText(this, "Nombre, email y contraseña (mínimo 6 caracteres) son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (curp.length() < 18) {
            Toast.makeText(this, "La CURP debe tener 18 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Puedes agregar más validaciones aquí si lo deseas (Teléfono, CP, etc.)
        return true;
    }

    private void irAlPaso2() {
        Intent intent = new Intent(this, ActivityRegistroCuidadorPaso2.class);

        // Pasamos toda la información al siguiente paso
        intent.putExtra("nombre", txtNombre.getText().toString().trim());
        intent.putExtra("email", txtEmail.getText().toString().trim());
        intent.putExtra("password", txtPassword.getText().toString().trim());
        intent.putExtra("telefono", txtTelefono.getText().toString().trim());
        intent.putExtra("curp", txtCurp.getText().toString().trim());
        intent.putExtra("ciudad", spinnerCiudad.getSelectedItem().toString());
        intent.putExtra("colonia", txtColonia.getText().toString().trim());
        intent.putExtra("calle", txtCalle.getText().toString().trim());
        intent.putExtra("numeroCasa", txtNumCasa.getText().toString().trim());
        intent.putExtra("cp", txtCP.getText().toString().trim());

        startActivity(intent);
    }
}