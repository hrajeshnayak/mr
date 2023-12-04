package com.prathigram.shopbot;

import android.app.Activity;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.CatRecyclerViewAdapter;
import Adapters.ProdRecyclerViewAdapter;
import Model.Cat;
import Model.Prod;
import Model.Retail;
import Util.Constants;

public class ShowProduct extends AppCompatActivity implements View.OnClickListener {

    //Assign ShowCat variables
    public static final String DEFAULT = "N/A";

    private RecyclerView recyclerView;
    private ProdRecyclerViewAdapter prodRecyclerViewAdapter;
    private List<Prod> prodList;
    private RequestQueue queue;
    private Prod prod;

    private String catCode, catName1;
    TextView catNameSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        catNameSel = (TextView)findViewById(R.id.catNameSel);

        queue = Volley.newRequestQueue(this);

        Intent iGet = getIntent();
        catCode = iGet.getStringExtra("catCode");
        catName1 = iGet.getStringExtra("catName");
        catNameSel.setText(catName1);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPro);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        prodList = new ArrayList<>();
        prodList = getProdData();

        prodRecyclerViewAdapter = new ProdRecyclerViewAdapter(this,prodList);
        recyclerView.setAdapter(prodRecyclerViewAdapter);
        prodRecyclerViewAdapter.notifyDataSetChanged();


    }

    private List<Prod> getProdData() {

        prodList.clear();
        String url1 = Constants.URL_CDTL+catCode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url1,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray prodArray = response.getJSONArray("result");

                            for (int i = 0; i < prodArray.length(); i++) {

                                JSONObject prodObj = prodArray.getJSONObject(i);

                                Prod prod = new Prod();

                                prod.setProdCode(prodObj.getString("prodCode"));
                                prod.setProdName(prodObj.getString("prodName"));
                                prod.setProdAvail(prodObj.getString("prodAvail"));
                                prod.setProdPrice(prodObj.getString("prodPrice"));
                                prod.setRetCode(prodObj.getString("retCode"));
                                prod.setCatName(prodObj.getString("catName"));



                                prodList.add(prod);


                            }
                            prodRecyclerViewAdapter.notifyDataSetChanged();




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


        return prodList;
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