package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Util.Constants;
import Util.Util;

public class EditCartItem extends AppCompatActivity implements View.OnClickListener{
    private TextView itemName, selProdPrice,nowQty;
    private EditText newQty;
    private RequestQueue queue;
    private String listId;
    private Button editQty,deleteItem;
    public static final String DEFAULT = "N/A";
    String pName = "";
    String pPrice="";
    String nwQty="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart_item);

        queue = Volley.newRequestQueue(this);

        Intent iGet = getIntent();
        listId = iGet.getStringExtra("listId");

        itemName = (TextView)findViewById(R.id.itemName);
        selProdPrice = (TextView)findViewById(R.id.selProdPrice);
        nowQty = (TextView)findViewById(R.id.nowQty);
        newQty = (EditText)findViewById(R.id.newQty);
        editQty = (Button)findViewById(R.id.editQty);
        editQty.setOnClickListener(this);
        deleteItem = (Button)findViewById(R.id.deleteItem);
        deleteItem.setOnClickListener(this);
        getMovieDetails();



    }

    private void getMovieDetails() {

        String url = Constants.URL_CARTITEMDTL+listId;
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

            pName = collegeData.getString("prodName");
            pPrice = collegeData.getString("prodPrice");
            nwQty = collegeData.getString("quantity");

            itemName.setText(pName);
            selProdPrice.setText(pPrice);
            nowQty.setText(nwQty);


        }catch (JSONException e) {
            e.printStackTrace();
        }

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
        switch (v.getId()){
            case R.id.editQty:


                if ((!newQty.getText().toString().equals(""))&& (!newQty.getText().toString().equals("0"))&& (newQty.getText().toString().length()<3))
                {
                    editQtyCart();

                }else
                {
                    Toast.makeText(getApplicationContext(),
                            "Add qty more than 0 or delete item", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.deleteItem:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(R.string.qfordelitem);
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        deleteCartItem();


                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;



        }

    }

    private void deleteCartItem() {

        String urlD = Constants.URL_CARTDELITEM+listId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if(response.trim().equalsIgnoreCase("success"))
                {
                    Intent registerIntent = new Intent(EditCartItem.this, MyCart.class);
                    startActivity(registerIntent);
                    finish();

                }


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

    private void editQtyCart() {
        final String newQuantity = newQty.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CARTEDTQTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if(response.trim().equalsIgnoreCase("success"))
                        {
                            Intent registerIntent = new Intent(EditCartItem.this, MyCart.class);
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
                params.put("nqty", newQuantity);
                params.put("listid", listId);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }
}