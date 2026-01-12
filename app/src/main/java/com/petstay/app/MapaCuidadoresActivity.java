package com.petstay.app;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

    public class MapaCuidadoresActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mapa_cuidadores);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Ejemplo: marcador de CDMX (luego aquí ponemos ubicaciones de cuidadores)
            LatLng cdmx = new LatLng(19.4326, -99.1332);
            mMap.addMarker(new MarkerOptions().position(cdmx).title("Ciudad de México"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cdmx, 12));
        }
    }


