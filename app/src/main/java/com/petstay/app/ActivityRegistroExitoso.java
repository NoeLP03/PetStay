package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegistroExitoso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroexitoso); // Asegúrate de que el XML se llame así

        Button btnIrMenu = findViewById(R.id.btnIrMenu);

        btnIrMenu.setOnClickListener(v -> {
            // Al presionar el botón, vamos al MainActivity
            Intent intent = new Intent(ActivityRegistroExitoso.this, MainActivity.class);
            // Limpiamos el historial para que no pueda volver atrás al registro
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}