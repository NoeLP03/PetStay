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

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.NaviView);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.BarraHe);



        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));





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




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_perf) {
            Intent intent = new Intent(this, ActivityPerfil.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_login) {
            startActivity(new Intent(this, ActivityLogin.class));
        }
        else if (id == R.id.nav_register) {
            startActivity(new Intent(this, ActivitySelectionRol.class));
        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void actualizarMenu() {
        NavigationView navigationView = findViewById(R.id.NaviView);
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Buscamos los ítems de forma segura
            MenuItem loginItem = menu.findItem(R.id.nav_login);
            MenuItem registerItem = menu.findItem(R.id.nav_register);
            MenuItem logoutItem = menu.findItem(R.id.nav_logout);
            MenuItem profileItem = menu.findItem(R.id.nav_perf);

            if (user != null) {
                // USUARIO LOGUEADO: Ocultar acceso, mostrar salida
                if (loginItem != null) loginItem.setVisible(false);
                if (registerItem != null) registerItem.setVisible(false);
                if (logoutItem != null) logoutItem.setVisible(true);
                if (profileItem != null) profileItem.setVisible(true);
            } else {
                // USUARIO INVITADO: Mostrar acceso, ocultar salida
                if (loginItem != null) loginItem.setVisible(true);
                if (registerItem != null) registerItem.setVisible(true);
                if (logoutItem != null) logoutItem.setVisible(false);
                if (profileItem != null) profileItem.setVisible(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Esta función se activará cada vez que regreses del Login o Registro
        actualizarMenu();
    }


}