package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegistroCuidadorExitoso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que este nombre sea idéntico al de tu archivo XML en la carpeta layout
        setContentView(R.layout.activity_registrocuidadorexitoso);

        Button btnIrMenu = findViewById(R.id.btnIrMenu);

        if (btnIrMenu != null) {
            btnIrMenu.setOnClickListener(v -> {
                // 1. Creamos el Intent hacia el MainActivity
                Intent intent = new Intent(ActivityRegistroCuidadorExitoso.this, MainActivity.class);

                // 2. Limpiamos TODA la pila de actividades (Registro Paso 1, Paso 2, etc.)
                // Esto hace que el MainActivity sea ahora la única pantalla activa
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // 3. Iniciamos la actividad y cerramos la actual
                startActivity(intent);
                finish();
            });
        }
    }
}