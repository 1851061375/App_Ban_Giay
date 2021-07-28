package com.example.appbangiay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbangiay.R;
import com.example.appbangiay.model.Cart;
import com.example.appbangiay.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbarDetail;
    ImageView imageViewDetail;
    TextView textViewName, textViewPrice, textViewDetail;
    Spinner spinner;
    Button button;
    int id = 0, productPrice = 0;
    String picture = "", productName = "", description = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        mapping();
        actionToolbar();
        getProductDetail();
        catchEvenSpinner();
        catchAddCartButton();
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

    private void catchAddCartButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = Integer.parseInt(spinner.getSelectedItem().toString());
                if(MainActivity.arrayListCart.size() > 0) {
                    boolean exist = false;
                     ArrayList<Cart> item = MainActivity.arrayListCart;
                    for(int i = 0; i < item.size(); i++) {
                        if(item.get(i).getIdProduct() == id) {
                            item.get(i).setCount(item.get(i).getCount()+newCount);
                            item.get(i).setProductPrice(productPrice*item.get(i).getCount());
                            exist = true;
                        }
                    }
                    if(!exist) {
                        int newPrice = newCount * productPrice;
                        MainActivity.arrayListCart.add(new Cart(id, newCount, productName, newPrice, picture));
                    }
                }
                else {
                    int newPrice = newCount * productPrice;
                    MainActivity.arrayListCart.add(new Cart(id, newCount, productName, newPrice, picture));
                }
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void catchEvenSpinner() {
        Integer[] arr = new Integer[] {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        spinner.setAdapter(arrayAdapter);
    }

    private void getProductDetail() {
        Product product = (Product) getIntent().getSerializableExtra("productDetail");
        id = product.getId();
        productName = product.getName();
        productPrice = product.getPrice();
        picture = product.getPicture();
        description = product.getDescription();
        DecimalFormat decimalFormat = new DecimalFormat("Giá: " + "###,###,###" + "đ");
        textViewPrice.setText(decimalFormat.format(productPrice));
        Picasso.get().load(picture).into(imageViewDetail);
        textViewName.setText(productName);
        textViewDetail.setText(description);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void mapping() {
        toolbarDetail = (Toolbar) findViewById(R.id.tbProduct);
        imageViewDetail = (ImageView) findViewById(R.id.ivProductDetail);
        textViewName = (TextView) findViewById(R.id.tvProductName);
        textViewPrice = (TextView) findViewById(R.id.tvProductPrice);
        textViewDetail = (TextView) findViewById(R.id.tvDetail);
        spinner = (Spinner) findViewById(R.id.spinnerCount);
        button = (Button) findViewById(R.id.btBuy);
    }
}