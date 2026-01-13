package com.petstay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.NaviView);
        Toolbar toolbar = findViewById(R.id.BarraHe);



        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));


        actualizarMenu(navigationView);
        actualizarHeader(navigationView);


        navigationView.setNavigationItemSelectedListener(this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });
    }
    private void actualizarMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_register).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
        } else {
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_register).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
        }

    }

    private void actualizarHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvHeaderNombre);
        TextView tvEmail = headerView.findViewById(R.id.tvHeaderEmail);
        ImageView imgFoto = headerView.findViewById(R.id.imgHeaderFoto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());

            String userId = user.getUid();
            FirebaseFirestore.getInstance().collection("Usuarios").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            tvNombre.setText(nombre);
                        } else {
                            FirebaseFirestore.getInstance().collection("Cuidadores").document(userId).get()
                                    .addOnSuccessListener(doc -> {
                                        if (doc.exists()) {
                                            tvNombre.setText(doc.getString("nombre"));
                                        }
                                    });
                        }
                    });
        } else {
            // Modo Invitado
            tvNombre.setText("Invitado");
            tvEmail.setText("Inicia sesión para continuar");
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_perf) {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        }
        else if (id == R.id.nav_register) {

            startActivity(new Intent(this, ActivityRegistroUsuario.class));
        }

        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.nav_register) {
            startActivity(new Intent(this, ActivitySelectionRol.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}