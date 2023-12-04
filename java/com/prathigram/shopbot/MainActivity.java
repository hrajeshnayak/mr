package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.RetailRecyclerViewAdapter;
import Model.Retail;
import Util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //initialize variable
    DrawerLayout drawerLayout;
    LinearLayout contentView;
    static final float END_SCALE = 0.6f;

    //Assign Dashboard variables

    public static final String DEFAULT = "N/A";

    private RecyclerView recyclerView;
    private RetailRecyclerViewAdapter retailRecyclerViewAdapter;
    private List<Retail> retailList;
    private RequestQueue queue;
    String tuLat, tuLng = "";
    private Button addNewRet;
    static final String CHANNEL_ID = "kb_id";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        contentView = findViewById(R.id.content);

        queue = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retailList = new ArrayList<>();

        retailList = getGroData();

        retailRecyclerViewAdapter = new RetailRecyclerViewAdapter(this, retailList );
        recyclerView.setAdapter(retailRecyclerViewAdapter);
        retailRecyclerViewAdapter.notifyDataSetChanged();

        addNewRet = findViewById(R.id.addNewRet);
        addNewRet.setOnClickListener(this);

        //Creating notification channel for devices on and above Android O
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableLights(true);
                channel.setLightColor(R.color.colorAccent);
                channel.enableVibration(true);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }



    }

    private void addNewRetailer() {

        if (tuLat.equals("0")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //set tittle
            builder.setTitle("No Location Found");
            //set message
            builder.setMessage("Please Set Your Location to Add new Retailer?");
            //positive yes button


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent nl = new Intent(MainActivity.this, NewLocation.class);
                    startActivity(nl);
                    finish();

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



        }else {

            Intent sdl = new Intent(MainActivity.this, AddNewRetailer.class);
            sdl.putExtra("lat", tuLat);
            sdl.putExtra("lng",tuLng);
            startActivity(sdl);

        }


    }

    private List<Retail> getGroData() {
        retailList.clear();
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String mphone = sharedPreferences.getString("UserName",DEFAULT);
        String url = Constants.URL_UDTL+mphone;



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray retailsArray = response.getJSONArray("result");

                            for (int i = 0; i < retailsArray.length(); i++) {

                                JSONObject retailObj = retailsArray.getJSONObject(i);

                                Retail retail = new Retail();
                                retail.setRetName(retailObj.getString("retname"));
                                retail.setRetAdd(retailObj.getString("retadd"));
                                retail.setRetCity(retailObj.getString("retcity"));
                                retail.setRetPhone(retailObj.getString("retphone"));
                                retail.setRetCode(retailObj.getString("retcode"));
                                retail.setRetImage(retailObj.getString("retimage"));
                                retail.setRetDesc(retailObj.getString("retdesc"));


                                //Log.d("Movies: ", movie.getTitle());
                                retailList.add(retail);


                            }
                            retailRecyclerViewAdapter.notifyDataSetChanged();

                            tuLat = response.getString("uLat");
                            tuLng = response.getString("uLng");







                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //queue.add(jsonObjectRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        return retailList;

    }
    @Override
    public void onBackPressed() {
        recreate();


    }



    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
        animateNavigationDrawer();

    }

    public void ClickCart(View view){
        //Go to Home
        redirectActivity(this,MyCart.class);


    }
    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.BLUE);
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


    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        //close drawer
        closeDrawer(drawerLayout);

    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        //Recreate Activity
        recreate();
    }

    public void ClickDashboard(View view){
        //redirect activity to dashboard
        redirectActivity(this, Retailers.class);
    }
    public void ClickAboutUs(View view){
        //redirect activity to about Us
        redirectActivity(this,AboutUs.class);
    }
    public void ClickProfile(View view){
        //redirect activity to about Us
        redirectActivity(this,Profile.class);
    }

    public void ClickLogout(View view){
        //Close App
        logout(this);
    }

    public static void logout(final Activity activity) {
        //initialise alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set tittle
        builder.setTitle("Logout");
        //set message
        builder.setMessage("Are you sure you want to logout?");
        //positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish Activity
                activity.finishAffinity();
                // Exit app
                System.exit(0);

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

    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addNewRet:
                addNewRetailer();

                break;

        }





    }
}