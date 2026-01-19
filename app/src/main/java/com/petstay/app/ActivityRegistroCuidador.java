package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegistroCuidador extends AppCompatActivity {

    private EditText txtNombre, txtEmail, txtPassword, txtTelefono, txtCurp, txtColonia, txtCalle, txtNumCasa, txtCP;
    private Spinner spinnerCiudad;
    private Button btnSiguiente, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidador);

        Button btnVolver = findViewById(R.id.btnVolverInicio);
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

        String[] ciudades = {"Culiacán", "Mazatlán", "Los Mochis", "Guasave", "Guamúchil", "Navolato", "Escuinapa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ciudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapter);

        btnSiguiente.setOnClickListener(v -> {
            if (validarCampos()) {
                irAlPaso2();
            }
        });

        btnVolver.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean validarCampos() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPassword.getText().toString().trim();

        if (txtNombre.getText().toString().isEmpty() || email.isEmpty() || pass.length() < 6) {
            Toast.makeText(this, "Nombre, email y contraseña (6 caracteres) son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtNombre.getText().toString().isEmpty() || txtCurp.getText().toString().length() < 18) {
            Toast.makeText(this, "Por favor revisa el nombre y que la CURP tenga 18 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void irAlPaso2() {
        Intent intent = new Intent(this, ActivityRegistroCuidadorPaso2.class);

        intent.putExtra("nombre", txtNombre.getText().toString());
        intent.putExtra("email", txtEmail.getText().toString());
        intent.putExtra("password", txtPassword.getText().toString());
        intent.putExtra("telefono", txtTelefono.getText().toString());
        intent.putExtra("curp", txtCurp.getText().toString());
        intent.putExtra("ciudad", spinnerCiudad.getSelectedItem().toString());
        intent.putExtra("colonia", txtColonia.getText().toString());
        intent.putExtra("calle", txtCalle.getText().toString());
        intent.putExtra("numeroCasa", txtNumCasa.getText().toString());
        intent.putExtra("cp", txtCP.getText().toString());

        startActivity(intent);
    }
}