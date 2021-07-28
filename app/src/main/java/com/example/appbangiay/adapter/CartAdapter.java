package com.example.appbangiay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbangiay.R;
import com.example.appbangiay.activity.CartActivity;
import com.example.appbangiay.activity.MainActivity;
import com.example.appbangiay.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cart> cartArrayList;

    public CartAdapter(Context context, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @Override
    public int getCount() {
        return cartArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView textViewCartName, textViewCartPrice;
        public ImageView imageViewCart;
        public Button buttonDecrease, buttonIncrease, buttonValue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_item, null);
            viewHolder.imageViewCart =  convertView.findViewById(R.id.ivCartItem);
            viewHolder.textViewCartName = convertView.findViewById(R.id.tvCartName);
            viewHolder.textViewCartPrice = convertView.findViewById(R.id.tvCartPrice);
            viewHolder.imageViewCart = convertView.findViewById(R.id.ivCartItem);
            viewHolder.buttonDecrease = convertView.findViewById(R.id.btDecrease);
            viewHolder.buttonIncrease = convertView.findViewById(R.id.btIncrease);
            viewHolder.buttonValue = convertView.findViewById(R.id.btValue);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (CartAdapter.ViewHolder) convertView.getTag();
        }
        Cart cart = (Cart) getItem(position);
        viewHolder.textViewCartName.setText(cart.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("Giá: " + "###,###,###" + "đ");
        viewHolder.textViewCartPrice.setText(decimalFormat.format(cart.getProductPrice()));
        Picasso.get().load(cart.getPicture()).into(viewHolder.imageViewCart);
        viewHolder.buttonValue.setText(cart.getCount()+"");
        //Add button cart
        viewHolder.buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldPrice = MainActivity.arrayListCart.get(position).getProductPrice();
                int oldCount = Integer.parseInt(viewHolder.buttonValue.getText().toString());
                int newCount = Integer.parseInt(viewHolder.buttonValue.getText().toString()) + 1;
                int newPrice = newCount * oldPrice / oldCount;
                MainActivity.arrayListCart.get(position).setCount(newCount);
                MainActivity.arrayListCart.get(position).setProductPrice(newPrice);
                viewHolder.buttonValue.setText(cart.getCount()+"");
                viewHolder.textViewCartPrice.setText(decimalFormat.format(newPrice));
                CartActivity.getTotalPrice();
            }
        });
        //decrease button cart
        viewHolder.buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldCount = Integer.parseInt(viewHolder.buttonValue.getText().toString());
                if(oldCount > 1) {
                    int oldPrice = MainActivity.arrayListCart.get(position).getProductPrice();
                    int newCount = Integer.parseInt(viewHolder.buttonValue.getText().toString()) - 1;
                    int newPrice = newCount * oldPrice / oldCount;
                    MainActivity.arrayListCart.get(position).setCount(newCount);
                    MainActivity.arrayListCart.get(position).setProductPrice(newPrice);
                    viewHolder.buttonValue.setText(cart.getCount()+"");
                    viewHolder.textViewCartPrice.setText(decimalFormat.format(newPrice));
                    CartActivity.getTotalPrice();
                }

            }
        });
        return convertView;
    }
}
