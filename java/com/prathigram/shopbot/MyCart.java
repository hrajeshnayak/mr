package com.prathigram.shopbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.CartRecyclerViewAdapter;
import Model.Cart;
import Model.Prod;
import Util.Constants;

public class MyCart extends AppCompatActivity implements View.OnClickListener{

    public static final String DEFAULT = "N/A";
    private RecyclerView recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private List<Cart> cartList;
    private RequestQueue queue;
    private TextView retNameCart, cartTotal, cartTotalQty;
    private Button checkOut;
    private String cartId ="";
    private String retName = "";
    private String cT,cTQ = "";
    int total = 0;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private String search;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        retNameCart = (TextView)findViewById(R.id.retNameCart);
        cartTotal = (TextView)findViewById(R.id.cartTotal);
        cartTotalQty = (TextView)findViewById(R.id.cartTotalQty);
        checkOut = (Button)findViewById(R.id.checkOut);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast

                        //Toast.makeText(MyCart.this, token, Toast.LENGTH_SHORT).show();
                    }
                });







        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartList = getCartDetails();

        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(this,cartList);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
        cartRecyclerViewAdapter.notifyDataSetChanged();


    }

    private List<Cart> getCartDetails() {
        cartList.clear();


        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String ucode = sharedPreferences.getString("UserName",DEFAULT);
        String url = Constants.URL_CARTDTLS+ucode;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {



                        try{
                            JSONArray prodArray = response.getJSONArray("result");


                            for (int i = 0; i < prodArray.length(); i++) {


                                JSONObject prodObj = prodArray.getJSONObject(i);

                                Cart cart = new Cart();

                                cart.setListId(prodObj.getString("listId"));
                                cart.setCartName(prodObj.getString("prodName"));
                                cart.setCartCode(prodObj.getString("prodCode"));
                                cart.setCartQty(prodObj.getString("quantity"));
                                cart.setCartPrice(prodObj.getString("prodPrice"));
                                cart.setCartAmnt(prodObj.getString("amount"));


                                cartList.add(cart);


                            }
                            cartRecyclerViewAdapter.notifyDataSetChanged();



                            retName = response.getString("retname");
                            final String otpStatus = response.getString("mphver");

                            if (!retName.equals("null")) {
                                retNameCart.setText(retName);

                                cT = response.getString("total");
                                cartTotal.setText(cT);

                                cTQ = response.getString("totalqty");
                                cartTotalQty.setText(cTQ);
                                checkOut.setText("Check-Out");
                                checkOut.setBackgroundResource(R.color.available);
                                checkOut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(otpStatus.equalsIgnoreCase("v")) {

                                            addMessage();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Verify Phone number to Complete Order", Toast.LENGTH_LONG).show();
                                            Intent registerIntent = new Intent(MyCart.this, Profile.class);
                                            startActivity(registerIntent);
                                            finish();
                                        }

                                    }
                                });


                            }
                            else{

                                checkOut.setEnabled(false);
                                checkOut.setText("No Items in cart");
                                checkOut.setBackgroundResource(R.color.sold);
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

        return cartList;



    }

    private void addMessage() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cart, null);
        final EditText messageEdt = view.findViewById(R.id.messageEdt);
        Button messageButton =view.findViewById(R.id.messageButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!messageEdt.getText().toString().isEmpty()) {

                    search = messageEdt.getText().toString().trim();

                    if(search.length()>250){

                        Toast.makeText(getApplicationContext(),
                                "Please enter smaller message", Toast.LENGTH_LONG).show();

                    }else{

                        checkOutCart();


                    }}
                dialog.dismiss();


            }
        });


    }

    private void showJSON(String response) {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject collegeData = result.getJSONObject(0);

            retName = collegeData.getString("prodName");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ClickMenu(View view){
        //Go to Home
        redirectActivity(this,MainActivity.class);


    }

    public void ClickCart(View view){
        //Go to Home
        recreate();


    }

    @Override
    public void onBackPressed() {
        Intent nl = new Intent(MyCart.this, MainActivity.class);
        startActivity(nl);
        finish();

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

    private void checkOutCart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyCart.this);
        //set tittle
        builder.setTitle("Order Submission");
        //set message
        builder.setMessage(
                "1. Order Placed cannot be Cancelled\n" +
                        "2. Order Placed after 7:00 PM may be delivered Next Day\n" +
                        "3. Delivery Charge may apply for orders less than Rs. 300.00\n"+
                        "Are you sure you want to proceed?\n");
        //positive yes button


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proceedToSubmit();


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

    private void proceedToSubmit() {
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String ucode = sharedPreferences.getString("UserName",DEFAULT);
        String urlC = Constants.URL_CARTCHKOUT+ucode+"&message="+search+"&token="+token;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if(response.trim().equalsIgnoreCase("success"))
                {
                    Intent registerIntent = new Intent(MyCart.this, MainActivity.class);
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
}