package com.example.myapplication.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Milk;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserMilkAdapter extends RecyclerView.Adapter<UserMilkAdapter.ViewHolder> {

    public static List<Milk> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public UserMilkAdapter(Context context, List<Milk> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_user_milk_add, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Milk milk = mData.get(position);

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView price,qty;

        ViewHolder(View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.textView5);
            qty = (TextView) itemView.findViewById(R.id.textView6);
            name = itemView.findViewById(R.id.name);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        Milk item = mData.get(id);
        return item.getName();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
