package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        // Vinculamos el botón por su ID
        Button btnVolver = findViewById(R.id.btn_volver_inicio);

        if (btnVolver != null) {
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // CAMBIO AQUÍ: Ahora apunta a MainActivity
                    Intent intent = new Intent(ActivityLogOut.this, MainActivity.class);

                    // Limpiamos el historial para que no pueda regresar al logout con el botón atrás
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}