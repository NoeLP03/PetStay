package com.petstay.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ActivityListaCuidadores extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    private CuidadorAdapter adapter;
    private List<Cuidador> listaCuidadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cuidadores);

        mFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rvCuidadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        obtenerCuidadores();
    }

    private void obtenerCuidadores() {
        listaCuidadores = new ArrayList<>();
        mFirestore.collection("Usuarios")
                .whereEqualTo("rol", "cuidador")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Cuidador c = doc.toObject(Cuidador.class);
                        listaCuidadores.add(c);
                    }
                    adapter = new CuidadorAdapter(listaCuidadores);
                    recyclerView.setAdapter(adapter);
                });
    }
}