package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class ActivitySelectionRol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_rol);

        MaterialCardView cardUsuario = findViewById(R.id.cardUsuario);
        MaterialCardView cardCuidador = findViewById(R.id.cardCuidador);

        cardUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRegistroUsuario.class));
        });

        cardCuidador.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRegistroCuidador.class));
        });
    }
}