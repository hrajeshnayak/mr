package com.prathigram.shopbot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.CatRecyclerViewAdapter;
import Adapters.RetailRecyclerViewAdapter;
import Model.Cat;
import Model.Retail;
import Util.Constants;

public class ShowCategory extends AppCompatActivity implements View.OnClickListener {



    //Assign ShowCat variables
    public static final String DEFAULT = "N/A";

    private RecyclerView recyclerView;
    private CatRecyclerViewAdapter catRecyclerViewAdapter;
    private List<Cat> catList;
    private RequestQueue queue;
    private Cat cat;
    private Retail retail;
    private String retCode, retCode1;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    TextView cartBadge;
    String itemQty="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        queue = Volley.newRequestQueue(this);

        Intent iGet = getIntent();
        retCode1 = iGet.getStringExtra("retCode");
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("retCode", retCode1);

        editor.commit();

        cartBadge = findViewById(R.id.cartBadge);





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        catList = new ArrayList<>();

        catList = getCatData();

        catRecyclerViewAdapter = new CatRecyclerViewAdapter(this, catList );
        recyclerView.setAdapter(catRecyclerViewAdapter);
        catRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showInputDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String search = newSearchEdt.getText().toString().trim();

                    if(search.length()>15){

                        Toast.makeText(getApplicationContext(),
                                "Please enter smaller search word", Toast.LENGTH_LONG).show();

                    }else{



                    Intent intent = new Intent(getApplicationContext(), ShowSearchProducts.class);
                    intent.putExtra("searchItem", search);
                    intent.putExtra("retCode", retCode);
                    startActivity(intent);



                    // movieRecyclerViewAdapter.notifyDataSetChanged();//Very important!!
                }}
                dialog.dismiss();


            }
        });




    }

    private List<Cat> getCatData() {
        catList.clear();
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        retCode = sharedPreferences.getString("retCode",DEFAULT);
        String ucode = sharedPreferences.getString("UserName",DEFAULT);

        String url = Constants.URL_UFDTL+retCode+"&ucode="+ucode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray retailsArray = response.getJSONArray("result");

                            for (int i = 0; i < retailsArray.length(); i++) {

                                JSONObject retailObj = retailsArray.getJSONObject(i);

                                Cat cat = new Cat();

                                cat.setCatName(retailObj.getString("catname"));
                                cat.setCatCode(retailObj.getString("catcode"));
                                cat.setCatImage(retailObj.getString("catimage"));
                                cat.setCatDesc(retailObj.getString("cattdesc"));



                                //Log.d("Movies: ", movie.getTitle());
                                catList.add(cat);


                            }
                            catRecyclerViewAdapter.notifyDataSetChanged();

                            itemQty = response.getString("totalqty");
                            if(!itemQty.equals("null")){

                                cartBadge.setText(itemQty);


                            }


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






        return catList;
    }
    public void ClickMenu(View view){
        //Go to Home
        redirectActivity(this,MainActivity.class);


    }

    public void ClickCart(View view){
        //Go to Home
        redirectActivity(this,MyCart.class);


    }




   /* public void ClickLogo(View view){
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
        redirectActivity(this, MainActivity.class);
    }

    public void ClickDashboard(View view){
        //redirect activity to Retailers
        redirectActivity(this, Retailers.class);
    }
    public void ClickAboutUs(View view){
        //redirect activity to about Us
        redirectActivity(this,AboutUs.class);
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

    }*/

    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent nl = new Intent(ShowCategory.this, MainActivity.class);
        startActivity(nl);
        finish();

    }



    @Override
    public void onClick(View v) {





    }
}