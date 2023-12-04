package com.prathigram.shopbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import Util.Util;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextAdd1, editTextAdd2, editTextAdd3;

    private EditText editTextCity, editTextPin;
    private Button buttonRegister, buttonLogin;
    private String token;
    private CheckBox checkBox;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextAdd1 = (EditText) findViewById(R.id.editTextAdd1);
        editTextAdd2 = (EditText) findViewById(R.id.editTextAdd2);
        editTextAdd3 = (EditText) findViewById(R.id.editTextAdd3);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextPin = (EditText) findViewById(R.id.editTextPin);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(this);




        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
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





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                if(checkBox.isChecked()) {

                    if ((!editTextName.getText().toString().equals("")) && (!editTextEmail.getText().toString().equals("")) && (!editTextAdd1.getText().toString().equals("")) && (!editTextAdd2.getText().toString().equals(""))
                            && (!editTextAdd3.getText().toString().equals("")) && (!editTextCity.getText().toString().equals("")) && (!editTextPin.getText().toString().equals(""))) {
                        if (editTextPhone.getText().toString().length() == 10) {
                            registerUser();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Phone Number should Consist of 10 characters", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "One or more fields are empty", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please accept Terms and Conditions", Toast.LENGTH_LONG).show();
                }


                break;

            case R.id.buttonLogin:

                Intent registerIntent = new Intent(Register.this, ActivityLogin.class);
                startActivity(registerIntent);

                break;

        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String add1 = editTextAdd1.getText().toString().trim();
        final String add2 = editTextAdd2.getText().toString().trim();
        final String add3 = editTextAdd3.getText().toString().trim();
        final String city = editTextCity.getText().toString().trim();
        final String pincode = editTextPin.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.Register_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response.trim(),Toast.LENGTH_LONG).show();
                        if(response.toString().trim().equals("success"))
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("UserName", phone);
                            editor.commit();

                            Intent registerIntent = new Intent(Register.this, RegisterOtp.class);
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
                params.put("name", name);
                params.put("email", email);
                params.put("phone",phone);
                params.put("password", password);
                params.put("add1", add1);
                params.put("add2", add2);
                params.put("add3", add3);
                params.put("city", city);
                params.put("pincode", pincode);
                params.put("token",token);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void ClickTNC(View view){
        Intent registerIntent = new Intent(Register.this, TermsAndCond.class);
        startActivity(registerIntent);



    }
}