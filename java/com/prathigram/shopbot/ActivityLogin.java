package com.prathigram.shopbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Util.Util;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    public static final String USER_NAME = "USER_NAME";



    private EditText editTextUserName;
    private EditText editTextPassword;

    private Button buttonULogin;
    private Button buttonURegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUserName = (EditText)findViewById(R.id.username);
        editTextPassword = (EditText)findViewById(R.id.password);

        buttonULogin = (Button) findViewById(R.id.uLogin);
        buttonULogin.setOnClickListener(this);

        buttonURegister = (Button) findViewById(R.id.uRegister);
        buttonURegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uLogin:
                if (   ( !editTextUserName.getText().toString().equals("")) && ( !editTextPassword.getText().toString().equals("")) ) {
                    if (editTextUserName.getText().toString().length() == 10) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserName", editTextUserName.getText().toString());

                        editor.commit();
                        Login();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Username should be 10 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.uRegister:

                Intent loginIntent = new Intent(ActivityLogin.this, Register.class);
                startActivity(loginIntent);
                break;



        }

    }

    private void Login() {
        final String phone = editTextUserName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.Login_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if(response.equalsIgnoreCase("success"))
                        {
                            Intent registerIntent = new Intent(ActivityLogin.this, MainActivity.class);
                            startActivity(registerIntent);
                            finish();

                        }else{
                            Toast.makeText(ActivityLogin.this,response,Toast.LENGTH_LONG).show();
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

                params.put("username",phone);
                params.put("password", password);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}