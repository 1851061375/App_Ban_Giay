package com.example.appbangiay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbangiay.R;
import com.example.appbangiay.adapter.CartAdapter;
import com.example.appbangiay.model.Cart;
import com.example.appbangiay.utility.Connect;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ListView listViewCart;
    TextView textViewAlert;
    static TextView textViewTotalPrice;
    Button buttonBuy, buttonAgain;
    CartAdapter cartAdapter;
    Toolbar toolbarCart;
    Connect checkCart;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        mapping();
        actionToolbar();
        checkCartList();
        getTotalPrice();
        catchOnItemListView();
        catchOnButton();
    }

    private void catchOnButton() {
        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.arrayListCart.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
                    startActivity(intent);
                }
                else {
                    checkCart.showToast_short(getApplicationContext(), "Giỏ hàng hiện tại đang trống");
                }
            }
        });
    }

    private void catchOnItemListView() {
        listViewCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Xóa sản phẩm này khỏi giỏ hàng?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MainActivity.arrayListCart.size() <= 0) {
                            textViewAlert.setVisibility(View.VISIBLE);
                        }
                        else {
                            MainActivity.arrayListCart.remove(position);
                            cartAdapter.notifyDataSetChanged();
                            getTotalPrice();
                            checkCartList();
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void getTotalPrice() {
        long totalPrice = 0;
        ArrayList<Cart> item = MainActivity.arrayListCart;
        for(int i= 0; i < item.size(); i++) {
            totalPrice += item.get(i).getProductPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###");
        textViewTotalPrice.setText(decimalFormat.format(totalPrice)+ "đ");
    }

    private void checkCartList() {
        if(MainActivity.arrayListCart .size() <= 0) {
            listViewCart.deferNotifyDataSetChanged();
            textViewAlert.setVisibility(View.VISIBLE);
            listViewCart.setVisibility(View.INVISIBLE);
        }
        else {
            listViewCart.deferNotifyDataSetChanged();
            textViewAlert.setVisibility(View.INVISIBLE);
            listViewCart.setVisibility(View.VISIBLE);
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        listViewCart = findViewById(R.id.lvCart);
        textViewAlert = findViewById(R.id.tvAlert);
        textViewTotalPrice = findViewById(R.id.tvTotalPrice);
        buttonBuy = findViewById(R.id.btCheckOut);
        buttonAgain = findViewById(R.id.btAgain);
        toolbarCart = findViewById(R.id.tbCart);
        cartAdapter = new CartAdapter(CartActivity.this, MainActivity.arrayListCart);
        listViewCart.setAdapter(cartAdapter);
    }
}
