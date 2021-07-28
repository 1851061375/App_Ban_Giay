package com.example.appbangiay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiay.R;
import com.example.appbangiay.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder> {
    Context context;
    ArrayList<Product> productArrayList;

    public ProductAdapter(ArrayList<Product> productArrayList, Context context) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.textViewName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("Giá: " + "###,###,###" + "đ");

        holder.textViewPrice.setText(decimalFormat.format(product.getPrice()));
        Picasso.get().load(product.getPicture())
                    .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public  class ItemHolder extends  RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewName, textViewPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            textViewName = (TextView) itemView.findViewById(R.id.tvname);
            textViewPrice = (TextView) itemView.findViewById(R.id.tvprice);
        }
    }
}
