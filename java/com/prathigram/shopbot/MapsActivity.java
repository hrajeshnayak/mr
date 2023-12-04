package com.prathigram.shopbot;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    private String tuLat,tuLng;
    private Double latitude, longitude;
    private Button uSetNLoc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent sdl = getIntent();
        tuLat = sdl.getStringExtra("lat");
        tuLng = sdl.getStringExtra("lng");

        if(!tuLat.equalsIgnoreCase("0")) {
            latitude = Double.parseDouble(tuLat);
            longitude = Double.parseDouble(tuLng);
        }else{
            latitude = 13.0526982;
            longitude = 77.6032183;
        }
        uSetNLoc = findViewById(R.id.uSetNLoc);
        uSetNLoc.setOnClickListener(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(latitude, longitude);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Delivery Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphome)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

    }

    @Override
    public void onBackPressed() {
        Intent nl = new Intent(MapsActivity.this, Profile.class);
        startActivity(nl);
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uSetNLoc:

                Intent nl = new Intent(MapsActivity.this, Profile.class);
                startActivity(nl);
                finish();






                break;


        }

    }
}