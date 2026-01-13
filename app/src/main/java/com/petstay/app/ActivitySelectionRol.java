package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

public class ActivitySelectionRol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_rol);

        // Asegúrate de usar el cast correcto
        CardView cardUsuario = findViewById(R.id.cardUsuario);
        CardView cardCuidador = findViewById(R.id.cardCuidador);

        if (cardUsuario != null) {
            cardUsuario.setOnClickListener(v -> {
                startActivity(new Intent(this, ActivityRegistroUsuario.class));
            });
        }

        if (cardCuidador != null) {
            cardCuidador.setOnClickListener(v -> {
                startActivity(new Intent(this, ActivityRegistroCuidador.class));
            });
        }
    }
}