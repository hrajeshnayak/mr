package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapters.ODetailRecyclerViewAdapter;
import Model.ODetail;
import Util.Constants;

public class MyOrders extends AppCompatActivity implements View.OnClickListener {
    public static final String DEFAULT = "N/A";
    private RecyclerView recyclerView;
    private ODetailRecyclerViewAdapter oDetailRecyclerViewAdapter;
    private List<ODetail> oDetailList;
    private RequestQueue queue;
    private String oId, oRet, oStat, oDate;
    private TextView orderRetName, orderGTotal,orderTotalQty,orderSDate;
    private String oTQ, oGT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView = findViewById(R.id.recyclerViewODetail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRetName = findViewById(R.id.orderRetName);
        orderGTotal = findViewById(R.id.orderGTotal);
        orderTotalQty = findViewById(R.id.orderTotalQty);
        orderSDate = findViewById(R.id.orderSDate);



        Intent iGet = getIntent();
        oId = iGet.getStringExtra("orderId");
        oRet = iGet.getStringExtra("orderRet");
        oStat = iGet.getStringExtra("orderStat");
        oDate = iGet.getStringExtra("oDate");

        oDetailList = new ArrayList<>();
        oDetailList = getODetails();

        orderRetName.setText(oRet);
        orderSDate.setText(oDate);

        oDetailRecyclerViewAdapter = new ODetailRecyclerViewAdapter(this, oDetailList);
        recyclerView.setAdapter(oDetailRecyclerViewAdapter);
        oDetailRecyclerViewAdapter.notifyDataSetChanged();

    }

    private List<ODetail> getODetails() {
        oDetailList.clear();
        String url = Constants.URL_ORDERLIST+oId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray retailsArray = response.getJSONArray("result");
                            for (int i = 0; i < retailsArray.length(); i++) {

                                JSONObject retailObj = retailsArray.getJSONObject(i);

                                ODetail oDetail = new ODetail();


                                oDetail.setOrderProdName(retailObj.getString("prodName"));
                                oDetail.setOrderProdPrice(retailObj.getString("prodPrice"));
                                oDetail.setOrderProdQty(retailObj.getString("quantity"));
                                oDetail.setOrderProdAmount(retailObj.getString("amount"));

                                oDetailList.add(oDetail);
                            }
                            oDetailRecyclerViewAdapter.notifyDataSetChanged();




                            oTQ = response.getString("totalqty");
                            oGT = response.getString("total");

                            orderGTotal.setText(oGT);
                            orderTotalQty.setText(oTQ);




                        } catch (JSONException e) {
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


        return  oDetailList;
    }
    public void ClickMenu(View view){
        //Go to Home
        redirectActivity(this,MainActivity.class);


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