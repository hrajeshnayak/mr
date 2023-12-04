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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Model.Retail;
import UI.CustomInfoWindow;
import Util.Util;

public class AddNewRetailer extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue queue;
    private String tuLat,tuLng;
    private Double uDLat, uDLon;
    private Double fRLat,fRLng;
    private String retCode;
    public static final String DEFAULT = "N/A";
    private String ucode,rcode;


    private BitmapDescriptor[] iconColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newretailer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent sd1= getIntent();
        tuLat = sd1.getStringExtra("lat");
        tuLng = sd1.getStringExtra("lng");
        uDLat = Double.parseDouble(tuLat);
        uDLon = Double.parseDouble(tuLng);






        iconColors = new BitmapDescriptor[] {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)



        };

        queue = Volley.newRequestQueue(this);
        getRetailerList();

    }

    private void getRetailerList() {




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Util.GetRetailer,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray retailsArray = response.getJSONArray("result");
                    for (int i = 0; i < retailsArray.length(); i++){
                        JSONObject retailObj = retailsArray.getJSONObject(i);

                        Retail retail = new Retail();
                        retail.setRetName(retailObj.getString("retName"));
                        retail.setRetAdd(retailObj.getString("retAdd"));
                        retail.setRetCity(retailObj.getString("retCity"));
                        retail.setRetPhone(retailObj.getString("retPhone"));
                        retail.setRetCode(retailObj.getString("retCode"));
                        retail.setRetImage(retailObj.getString("retImage"));
                        retail.setRetDesc(retailObj.getString("retDesc"));
                        retail.setRetPin(retailObj.getString("retPin"));
                       Double lat = Double.parseDouble(retailObj.getString("retLat"));
                       Double lon = Double.parseDouble(retailObj.getString("retLng"));
                       retail.setRetLat(lat);
                       retail.setRetLng(lon);


                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopnew));
                        markerOptions.title(retail.getRetName());
                        markerOptions.position(new LatLng(retail.getRetLat(),retail.getRetLng()));
                        markerOptions.snippet("Addr: "+retail.getRetAdd()+"  City: "+retail.getRetCity());

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(retail.getRetCode());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 11));
                        marker.showInfoWindow();








                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);







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
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        LatLng userLoc = new LatLng(uDLat,uDLon);
        mMap.addMarker(new MarkerOptions().position(userLoc).title("Your Delivery Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphome)));




        //  mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Ask for permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }else {
                // we have permission!
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  locationListener);

        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTag()!=null){
            retCode = marker.getTag().toString();
            fRLat = marker.getPosition().latitude;
            fRLng = marker.getPosition().longitude;
            String maxDis = marker.getSnippet();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //set tittle
            builder.setTitle("Add This Retailer to Favorite?");
            //set message
            builder.setMessage("Are you sure you want to continue?");
            //positive yes button


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    addToRetFavorite(retCode,fRLat,fRLng);

                }
            });
            //Negative No Button
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dismiss Dialog
                    dialog.dismiss();


                }
            });
            //Show dialog
            builder.show();


        }



    }

    private void addToRetFavorite(String retCode, Double fRLat, Double fRLng) {
        LatLng uLatLng = new LatLng(uDLat,uDLon);
        LatLng rLatLng = new LatLng(fRLat,fRLng);
        double distUR = SphericalUtil.computeDistanceBetween(uLatLng,rLatLng);


        if (distUR>5000){


                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Out Of Retailer Coverage");
                builder.setMessage("Your Area is out of Retailer Delivery Coverage");

                // add a button
                builder.setPositiveButton("OK", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();



        }else {
            SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
            ucode = sharedPreferences.getString("UserName",DEFAULT);
            rcode = retCode;
            addRetToFav();

        }







    }

    private void addRetToFav() {
        ucode = ucode.trim();
        rcode = rcode.trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.AddRetToFav,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success"))
                        {
                            Toast.makeText(getApplicationContext(),response+"fully added",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

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

                params.put("ucode",ucode);
                params.put("rcode", rcode);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}