package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistroCuidadorPaso2 extends AppCompatActivity {

    private Spinner spinnerTipo, spinnerCantidad, spinnerTamano;
    private Button btnFinalizar, btnRegresar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cuidador_paso2);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // --- CONFIGURACIÓN TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- VINCULAR VISTAS ---
        spinnerTipo = findViewById(R.id.spinnerTipoMascota);
        spinnerCantidad = findViewById(R.id.spinnerCantidad);
        spinnerTamano = findViewById(R.id.spinnerTamano);
        btnFinalizar = findViewById(R.id.btnFinalizarRegistro);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Llenar Spinners
        llenarSpinner(spinnerTipo, new String[]{"Perro", "Gato", "Ambos"});
        llenarSpinner(spinnerCantidad, new String[]{"1", "2", "3", "4", "5"});
        llenarSpinner(spinnerTamano, new String[]{"Pequeño", "Mediano", "Grande"});

        btnRegresar.setOnClickListener(v -> finish());
        btnFinalizar.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void llenarSpinner(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void mostrarDialogoConfirmacion() {
        // Asegúrate de tener creado res/layout/dialog_confirmacion_registro.xml
        View view = getLayoutInflater().inflate(R.layout.dialog_confirmacion_registro, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Button btnConfirmar = view.findViewById(R.id.btnConfirmarDialogo);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDialogo);

        btnConfirmar.setOnClickListener(v -> {
            dialog.dismiss();
            registrarTodo();
        });
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void registrarTodo() {
        Bundle datos = getIntent().getExtras();
        if (datos == null) {
            Toast.makeText(this, "Error: Datos no recibidos", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = datos.getString("email");
        String password = datos.getString("password");

        if (email == null || password == null) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> map = new HashMap<>();

                        // --- DATOS DEL PASO 1 ---
                        map.put("nombre", datos.getString("nombre"));
                        map.put("email", email);
                        map.put("curp", datos.getString("curp"));
                        map.put("telefono", datos.getString("telefono"));
                        map.put("ciudad", datos.getString("ciudad"));
                        map.put("direccion", datos.getString("calle") + " #" + datos.getString("numeroCasa") + ", " + datos.getString("colonia"));
                        map.put("rol", "cuidador");

                        // --- DATOS DEL PASO 2 ---
                        map.put("acepta", spinnerTipo.getSelectedItem().toString());
                        map.put("capacidad", spinnerCantidad.getSelectedItem().toString());
                        map.put("tamanoMax", spinnerTamano.getSelectedItem().toString());

                        mFirestore.collection("Usuarios").document(uid).set(map)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "¡Bienvenido a PetStay!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                });
                    } else {
                        Toast.makeText(this, "Error al crear cuenta: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}