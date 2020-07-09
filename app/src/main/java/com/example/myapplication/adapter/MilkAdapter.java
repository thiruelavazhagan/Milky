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

public class MilkAdapter extends RecyclerView.Adapter<MilkAdapter.ViewHolder> {

    public static List<Milk> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MilkAdapter(Context context, List<Milk> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_milk_add, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Milk milk = mData.get(position);

        holder.name.setText(milk.getName());
        holder.price.setText(mData.get(position).getAmount());
        holder.qty.setText(mData.get(position).getQty());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextInputEditText price,qty;

        ViewHolder(View itemView) {
            super(itemView);

            price = (TextInputEditText) itemView.findViewById(R.id.price);
            qty =(TextInputEditText) itemView.findViewById(R.id.qtys);
            name = itemView.findViewById(R.id.name);

            price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mData.get(getAdapterPosition()).setAmount(price.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mData.get(getAdapterPosition()).setQty(qty.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            itemView.setOnClickListener(this);
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
