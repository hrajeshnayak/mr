package com.prathigram.shopbot;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Util.Util;

public class NewLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {
    private GoogleMap mMap;
    private Button uSetLoc;
    public static final String DEFAULT = "N/A";
    Location currentLocation;
    LatLng newLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String uLat="";
    private String uLon="";
    private static final int REQUEST_CODE = 101;
    private Button butSkipLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        uSetLoc = findViewById(R.id.uSetLoc);
        uSetLoc.setOnClickListener(this);
        butSkipLoc = findViewById(R.id.butSkipLoc);
        butSkipLoc.setOnClickListener(this);


    }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapNew);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(NewLocation.this);
                }else{
                    currentLocation.setLatitude(13.0526982);
                    currentLocation.setLongitude(77.6032183);

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapNew);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(NewLocation.this);


                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Set Location").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphome));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerDragListener(this);
        uLat = String.valueOf(latLng.latitude);
        uLon = String.valueOf(latLng.longitude);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        newLocation = marker.getPosition();
        mMap.clear();


        MarkerOptions markerOptions = new MarkerOptions().position(newLocation).title("Set Location").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphome));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(newLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 17));
        mMap.addMarker(markerOptions);


        uLat = String.valueOf(newLocation.latitude);
        uLon = String.valueOf(newLocation.longitude);





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uSetLoc:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //set tittle
                builder.setTitle("Save Delivery Address");
                //set message
                builder.setMessage("1. Please Set correct Delivery Address\n\n" +
                        "2. Retailers will be searched from this address\n\n"+
                        "3. Address Once saved Cannot be changed\n\n" +
                        "\nAre you sure you want to continue?");
                //positive yes button


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addLocToProfile();

                    }
                });
                //Negative No Button
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss Dialog
                        recreate();
                        finish();

                    }
                });
                //Show dialog
                builder.show();




                break;

            case R.id.butSkipLoc:
                Intent n2 = new Intent(NewLocation.this, MainActivity.class);
                startActivity(n2);
                finish();
                break;
        }



    }

    private void addLocToProfile() {

        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        final String uCode = sharedPreferences.getString("UserName",DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.AddLocToProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response + " in Saving to DB",Toast.LENGTH_LONG).show();

                        if(response.equals("success"))
                        {
                            Intent registerIntent = new Intent(NewLocation.this, MainActivity.class);
                            startActivity(registerIntent);
                            finish();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ucode", uCode);
                params.put("ulat", uLat);
                params.put("ulon", uLon);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    public void onBackPressed() {
        Intent nl = new Intent(NewLocation.this, ActivityLogin.class);
        startActivity(nl);
        finish();

    }
}