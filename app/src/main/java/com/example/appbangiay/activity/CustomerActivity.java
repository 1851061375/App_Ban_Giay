package com.example.appbangiay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbangiay.R;
import com.example.appbangiay.utility.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerActivity extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextAddress;
    Button buttonBack, buttonCheckOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mapping();
        catchOnButton();
    }

    private void catchOnButton() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String customerName = editTextName.getText().toString().trim();
               final String phone = editTextPhone.getText().toString().trim();
               final String address = editTextAddress.getText().toString().trim();
                if(customerName.length() > 0 && phone.length() > 0 && address.length() > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlBill, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.d("id", response);
                            if(Integer.parseInt(response) > 0) {
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, Server.urlBillDetail, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for(int i = 0; i < MainActivity.arrayListCart.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("idBill", response);
                                                jsonObject.put("idProduct", MainActivity.arrayListCart.get(i).getIdProduct());
                                                jsonObject.put("productName", MainActivity.arrayListCart.get(i).getProductName());
                                                jsonObject.put("count", MainActivity.arrayListCart.get(i).getCount());
                                                jsonObject.put("price", MainActivity.arrayListCart.get(i).getProductPrice());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String,String> hashMap = new HashMap<String, String>();
                                        hashMap.put("cart",jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                queue.add(request);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("customerName", customerName);
                            hashMap.put("phone", phone);
                            hashMap.put("address", address);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                    //show alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Đặt hàng thành công!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.arrayListCart.clear();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    builder.show();
                }
            }
        });
    }

    private void mapping() {
        editTextName = findViewById(R.id.etName);
        editTextPhone = findViewById(R.id.etPhone);
        editTextAddress = findViewById(R.id.etAddress);
        buttonBack = findViewById(R.id.btBack);
        buttonCheckOut = findViewById(R.id.btCheckOut);
    }
}