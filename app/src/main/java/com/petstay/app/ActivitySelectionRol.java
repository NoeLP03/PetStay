package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class ActivitySelectionRol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_rol);

        // --- CONFIGURACIÓN DE BARRA SUPERIOR ---
        Toolbar toolbar = findViewById(R.id.BarraHe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Flecha atrás regresa al login o inicio
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- VINCULAR TARJETAS ---
        CardView cardUsuario = findViewById(R.id.cardUsuario);
        CardView cardCuidador = findViewById(R.id.cardCuidador);

        cardUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRegistroUsuario.class));
        });

        cardCuidador.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRegistroCuidador.class));
        });
    }
}