package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Constants;

public class Profile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout contentView;
    static final float END_SCALE = 0.6f;
    public static final String DEFAULT = "N/A";
    private TextView uName, uEmail, uAdd1, uAdd2, uAdd3, uCity, uPin, otpStatus, locStatus;
    private Button buttonOtp, buttonLoc, buttonNewLoc;

    String tuName, tuEmail, tuAdd1, tuAdd2, tuAdd3, tuCity, tuPin, totpStatus, tlocStatus="";
    String tuLat, tuLng = "";
    private RequestQueue queue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        contentView = findViewById(R.id.content);

        queue = Volley.newRequestQueue(this);

        uName = findViewById(R.id.uName);
        uEmail = findViewById(R.id.uEmail);
        uAdd1 = findViewById(R.id.uAdd1);
        uAdd2 = findViewById(R.id.uAdd2);
        uAdd3 = findViewById(R.id.uAdd3);
        uCity = findViewById(R.id.uCity);
        uPin = findViewById(R.id.uPin);
        otpStatus = findViewById(R.id.otpStatus);
        locStatus = findViewById(R.id.locStatus);
        buttonOtp = findViewById(R.id.buttonOtp);
        buttonLoc = findViewById(R.id.buttonCurLoc);
        


        getUserData();



    }

    private void getUserData() {
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String mphone = sharedPreferences.getString("UserName",DEFAULT);
        String url = Constants.URL_PDTL+mphone;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void showJSON(String response) {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject collegeData = result.getJSONObject(0);

            tuName = collegeData.getString("name");
            tuEmail = collegeData.getString("memail");
            totpStatus = collegeData.getString("mphver");
            tuAdd1 = collegeData.getString("add1");
            tuAdd2 = collegeData.getString("add2");
            tuAdd3 = collegeData.getString("add3");
            tuCity = collegeData.getString("city");
            tuPin = collegeData.getString("pincode");
            tuLat = collegeData.getString("lat");
            tuLng = collegeData.getString("lng");

            uName.setText(tuName);
            uEmail.setText(tuEmail);
            uAdd1.setText(tuAdd1);
            uAdd2.setText(tuAdd2);
            uAdd3.setText(tuAdd3);
            uCity.setText(tuCity);
            uPin.setText(tuPin);


            if (totpStatus.equals("nv")) {
                otpStatus.setText(R.string.nver);
                buttonOtp.setText(R.string.verOTP);
                buttonOtp.setBackgroundResource(R.color.okButton);
                buttonOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.redirectActivity(Profile.this, VerifyOtp.class);
                    }
                });
            } else {
                otpStatus.setText(R.string.veri);
                buttonOtp.setEnabled(false);
                buttonOtp.setText(R.string.veri);
                buttonOtp.setBackgroundResource(R.color.sold);
            }

            if (tuLat.equals("0")) {
                locStatus.setText(R.string.lna);
                buttonLoc.setText("Set Location");
                buttonLoc.setBackgroundResource(R.color.available);
                buttonLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       setNewLoc();
                    }
                });
            } else {
                locStatus.setText(R.string.locAv);
                buttonLoc.setText("View Delivery Address");
                buttonLoc.setBackgroundResource(R.color.textColor);
                buttonLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDelLoc();
                    }
                });

            }



        }catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void setNewLoc() {

        Intent nl = new Intent(Profile.this, NewLocation.class);
        startActivity(nl);

    }



    private void setDelLoc() {
        Intent sdl = new Intent(Profile.this, MapsActivity.class);
        sdl.putExtra("lat", tuLat);
        sdl.putExtra("lng",tuLng);
        startActivity(sdl);

    }


    public void ClickMenu(View view){
        //Open Drawer
        MainActivity.openDrawer(drawerLayout);
        animateNavigationDrawer();
    }

    public void ClickCart(View view){
        //Go to Home
        MainActivity.redirectActivity(this,MyCart.class);


    }
    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.RED);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }
    public void ClickLogo(View view){
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //Redirect Activity to home
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickProfile(View view){
        //redirect activity to about Us
        recreate();
    }

    public void ClickDashboard(View view){
        //Recreate View
        recreate();
    }

    public void ClickAboutUs(View view){
        //Redirect to about Us
        MainActivity.redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close Drawer
        MainActivity.closeDrawer(drawerLayout);

    }

    @Override
    public void onBackPressed() {
        Intent nl = new Intent(Profile.this, MainActivity.class);
        startActivity(nl);
        finish();

    }
}