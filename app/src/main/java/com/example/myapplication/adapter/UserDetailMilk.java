package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Milk;
import com.example.myapplication.model.MilkDetail;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserDetailMilk extends RecyclerView.Adapter<UserDetailMilk.ViewHolder> {

    public static List<MilkDetail> mData;
    private LayoutInflater mInflater;


    // data is passed into the constructor
    public UserDetailMilk(Context context, List<MilkDetail> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.milk_row_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MilkDetail milk = mData.get(position);

        holder.name.setText(milk.getName());
        holder.price.setText(milk.getAmount());
        holder.qty.setText(milk.getQty());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price, qty;

        ViewHolder(View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.textView9);
            qty = (TextView) itemView.findViewById(R.id.textView10);
            name = itemView.findViewById(R.id.textView8);

        }


    }




}
