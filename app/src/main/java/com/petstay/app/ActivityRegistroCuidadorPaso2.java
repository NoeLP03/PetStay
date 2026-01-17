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
        if (datos == null) return;

        mAuth.createUserWithEmailAndPassword(datos.getString("email"), datos.getString("password"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> map = new HashMap<>();

                        // --- DATOS DEL PASO 1 ---
                        map.put("nombre", datos.getString("nombre"));
                        map.put("email", datos.getString("email")); // <--- ¡ESTA LÍNEA HACÍA FALTA!
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
                                    Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                    finishAffinity();
                                });
                    } else {
                        // Es bueno agregar un error por si el correo ya existe
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}