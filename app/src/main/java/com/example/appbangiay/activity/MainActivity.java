package com.example.appbangiay.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbangiay.R;
import com.example.appbangiay.adapter.CategoryAdapter;
import com.example.appbangiay.adapter.ProductAdapter;
import com.example.appbangiay.model.Cart;
import com.example.appbangiay.model.Category;
import com.example.appbangiay.model.Product;
import com.example.appbangiay.utility.Connect;
import com.example.appbangiay.utility.GridSpacingItemDecoration;
import com.example.appbangiay.utility.RecyclerItemClickListener;
import com.example.appbangiay.utility.Server;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    ArrayList<Category> arrCategory;
    CategoryAdapter categoryAdapter;
    int typeC = 0;
    String categoryName = "";
    ArrayList<Product> arrProduct;
    ProductAdapter productAdapter;
    TextView textViewTitle;

    public static  ArrayList<Cart> arrayListCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        if(Connect.isNetworkAvailable(getApplicationContext())) {
            actionBar();
            actionViewFlipper();
            getCategory();
            getProduct(typeC);
            catchOnItemListView();
            catchOnItemRecyclerView();
        }
        else {
            Connect.showToast_short(getApplicationContext(), "Bạn cần kết nối ");
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void catchOnItemRecyclerView() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                        intent.putExtra("productDetail", arrProduct.get(position));
                        startActivity(intent);
                        //id Product == position

                        Log.d("productDetail", arrProduct.get(position)+"");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void catchOnItemListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadProductByType(position);
                TextView textTitle = (TextView) view.findViewById(R.id.tvCategory);
                String text = textTitle.getText().toString();
                textViewTitle.setText(text);
            }
        });
    }

    private void loadProductByType(int type) {
        arrProduct.clear();
        getProduct(type);
        productAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void getProduct(int typeC) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlProduct,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0, typeP = 0,price = 0;
                String productName = "",  picture = "", description = "";
                if(response != null) {
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            productName = jsonObject.getString("name");
                            price = jsonObject.getInt("price");
                            picture = jsonObject.getString("picture");
                            typeP = jsonObject.getInt("type");
                            description = jsonObject.getString("description");
                            if(typeC == 0) {
                                arrProduct.add(new Product(id, productName, price, picture, typeP, description));
                            }
                            else {
                                if(typeC == typeP)
                                arrProduct.add(new Product(id, productName, price, picture, typeP, description));
                            }
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Connect.showToast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlCategory,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            typeC = jsonObject.getInt("type");
                            categoryName = jsonObject.getString("name");
                            arrCategory.add(new Category(typeC, categoryName));
                            categoryAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Connect.showToast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void actionViewFlipper() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("https://cf.shopee.vn/file/dcf9d72c67246ef184d7dfac73332e0f_xxhdpi");
        arr.add("https://cf.shopee.vn/file/672106017f2208ee1db5bdb005b9fa58_xxhdpi");
        arr.add("https://cf.shopee.vn/file/19a06e501142a662ef37f3494d82a861_xxhdpi");
        arr.add("https://cf.shopee.vn/file/1972459c3f5806c584a646d62ede39e8_xxhdpi");
        arr.add("https://cf.shopee.vn/file/445d90096173b6489ae7a7cab5e5e20c_xxhdpi");
        for (int i = 0; i < arr.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(arr.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private  void  actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void mapping() {
        drawerLayout = findViewById(R.id.dl);
        toolbar = findViewById(R.id.tb);
        viewFlipper = findViewById(R.id.vl);
        recyclerView = findViewById(R.id.rvProduct);
        navigationView = findViewById(R.id.nvCategory);
        listView = findViewById(R.id.lvCategory);
        textViewTitle = (TextView) findViewById(R.id.tvTitle);
        //Category
        arrCategory = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(arrCategory, getApplicationContext());
        listView.setAdapter(categoryAdapter);
        //Product
        arrProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(arrProduct, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 6, true));
        recyclerView.setAdapter(productAdapter);
        //Cart
        if(arrayListCart == null) {
            arrayListCart = new ArrayList<>();
        }
    }
}