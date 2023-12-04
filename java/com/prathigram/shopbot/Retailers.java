package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.OrderRecyclerViewAdapter;
import Model.Cat;
import Model.Order;
import Util.Constants;

public class Retailers extends AppCompatActivity implements View.OnClickListener{
    //initialize variable
    DrawerLayout drawerLayout;
    LinearLayout contentView;
    static final float END_SCALE = 0.6f;

    public static final String DEFAULT = "N/A";

    private RecyclerView recyclerView;
    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private List<Order> orderList;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);
        //assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        contentView = findViewById(R.id.content);
        queue = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderList = getOrderData();

        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(this, orderList);
        recyclerView.setAdapter(orderRecyclerViewAdapter);
        orderRecyclerViewAdapter.notifyDataSetChanged();



    }

    private List<Order> getOrderData() {
        orderList.clear();
        SharedPreferences sharedPreferences=getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String mphone = sharedPreferences.getString("UserName",DEFAULT);
        String url = Constants.URL_ORDERDTLS+mphone;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray retailsArray = response.getJSONArray("result");

                            for (int i = 0; i < retailsArray.length(); i++) {

                                JSONObject retailObj = retailsArray.getJSONObject(i);

                                Order order = new Order();

                                order.setOrderId(retailObj.getString("orderId"));
                                order.setOrderRet(retailObj.getString("orderRet"));
                                order.setOrderStatus(retailObj.getString("orderStatus"));

                                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                Date parsedDate = parser.parse(retailObj.getString("orderDate"));
                                String dd1 = dateFormatter.format(parsedDate);
                                order.setOrderDate(dd1);




                                orderList.add(order);

                            }
                            orderRecyclerViewAdapter.notifyDataSetChanged();


                        }catch (JSONException | ParseException e) {
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







        return orderList;
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
        MainActivity.redirectActivity(this,Profile.class);
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
    public void onClick(View v) {

    }
}