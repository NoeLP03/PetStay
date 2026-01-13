package com.petstay.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegistroCuidador extends AppCompatActivity {

    private EditText txtNombre, txtEmail, txtPassword, txtTelefono, txtDireccion, txtINE;
    private Button btnRegistrar;
    private ImageView imgFoto;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorage;

    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuidador);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("fotos_ine");


        imgFoto = findViewById(R.id.imgINE);
        btnRegistrar = findViewById(R.id.btnRegistrar);


        imgFoto.setOnClickListener(v -> abrirGaleria());

        btnRegistrar.setOnClickListener(view -> validarYRegistrar());
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgFoto.setImageURI(imageUri);
        }
    }

    private void validarYRegistrar() {
        if (imageUri == null) {
            Toast.makeText(this, "Por favor, selecciona una foto de tu INE", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        subirFotoAFirebase(mAuth.getCurrentUser().getUid());
                    }
                });
    }

    private void subirFotoAFirebase(String id) {
        StorageReference fileRef = mStorage.child(id + ".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                guardarDatosEnFirestore(id, downloadUrl);
            });
        }).addOnFailureListener(e -> Toast.makeText(this, "Error al subir foto", Toast.LENGTH_SHORT).show());
    }

    private void guardarDatosEnFirestore(String id, String urlFoto) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", txtNombre.getText().toString());
        map.put("urlIne", urlFoto);
        map.put("rol", "cuidador");

        mFirestore.collection("Cuidadores").document(id).set(map)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Â¡Cuidador Registrado!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}