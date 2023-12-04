package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Model.Prod;
import Util.Constants;
import Util.Util;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener{

    private TextView itemName, catSelName, availStatus, selProdPrice;
    private EditText reqQty;
    private RequestQueue queue;
    private String retCode, prodCode;
    private Button oConfirm;
    public static final String DEFAULT = "N/A";
    String pName = "";
    String cName="";
    String aStat="";
    String pPrice="";
    String pCode="";
    String rcode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        queue = Volley.newRequestQueue(this);



        Intent iGet = getIntent();
        retCode = iGet.getStringExtra("retCode");
        prodCode = iGet.getStringExtra("prodCode");

        itemName = findViewById(R.id.itemName);
        catSelName = findViewById(R.id.catSelName);
        availStatus = findViewById(R.id.availStatus);
        selProdPrice = findViewById(R.id.selProdPrice);
        reqQty = findViewById(R.id.reqQty);
        oConfirm = findViewById(R.id.oConfirm);


        getMovieDetails();

    }


    private void getMovieDetails() {

        String url2 = Constants.URL_PRODDTL + retCode + Constants.URL_PRODDTLRT + prodCode;


        StringRequest stringRequest = new StringRequest(url2, new Response.Listener<String>() {
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

            pName = collegeData.getString("prodName");
            cName = collegeData.getString("catName");
            aStat = collegeData.getString("prodAvail");
            pPrice = collegeData.getString("prodPrice");
            pCode = collegeData.getString("prodCode");
            rcode = collegeData.getString("retCode");

            itemName.setText(pName);
            catSelName.setText(cName);
            availStatus.setText(aStat);
            selProdPrice.setText(pPrice);

            if (aStat.equals("AVAILABLE")) {
                oConfirm.setText(R.string.btn_add_to_cart);
                oConfirm.setBackgroundResource(R.color.available);
                oConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proOrder();
                    }
                });
            } else {
                oConfirm.setEnabled(false);
                oConfirm.setText(R.string.btn_out_of_stock);
                oConfirm.setBackgroundResource(R.color.sold);
            }



        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void proOrder() {



        String temp = reqQty.getText().toString();
        int qty = 0;

        if (!temp.equalsIgnoreCase("")) {
        qty = Integer.parseInt(temp);
        if (qty>0){
            adProToCart();
        }else{
            Toast.makeText(getApplicationContext(),
                    "add Positive value", Toast.LENGTH_SHORT).show();

        }



        }else{
            Toast.makeText(getApplicationContext(),
                    "Enter some value", Toast.LENGTH_SHORT).show();


        }


    }

    private void adProToCart() {
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        final String ucode = sharedPreferences.getString("UserName",DEFAULT);
        final String quantity = reqQty.getText().toString().trim();
        final String retcode = rcode.toString().trim();
        final String prodcode = pCode.toString().trim();
        final String prodname = pName.toString().trim();
        final String price = pPrice.toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.AddProToCart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success"))
                        {
                            Toast.makeText(getApplicationContext(),"Item Added to Cart",Toast.LENGTH_LONG).show();

                            Intent registerIntent = new Intent(ProductDetail.this, ShowCategory.class);

                            registerIntent.putExtra("retCode", retcode);
                            startActivity(registerIntent);
                            finish();

                        }else{
                            Toast.makeText(ProductDetail.this,response,Toast.LENGTH_LONG).show();
                            Intent registerIntent = new Intent(ProductDetail.this, ShowCategory.class);

                            registerIntent.putExtra("retCode", retcode);
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

                params.put("ucode",ucode);
                params.put("retcode", retcode);
                params.put("prodcode", prodcode);
                params.put("prodname", prodname);
                params.put("quantity", quantity);
                params.put("price", price);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void ClickMenu(View view){
        //Go to Home
        redirectActivity(this,MainActivity.class);


    }
    public void ClickCart(View view){
        //Go to Home
        redirectActivity(this,MyCart.class);


    }



    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}