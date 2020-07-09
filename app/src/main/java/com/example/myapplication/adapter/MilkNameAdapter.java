package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.MilkName;
import com.example.myapplication.model.UserFirebaseAdd;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MilkNameAdapter extends RecyclerView.Adapter<MilkNameAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<MilkName> contactList;
    private List<MilkName> contactListFiltered;
   /* private ContactsAdapterListener listener;*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone,start;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            thumbnail = view.findViewById(R.id.thumbnail);



        }
    }


    public MilkNameAdapter(Context context, List<MilkName> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.milk_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MilkName contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());

/*
        Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<MilkName> filteredList = new ArrayList<>();
                    for (MilkName row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<MilkName>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

   /* public interface ContactsAdapterListener {
        void onContactSelected(UserFirebase userFirebase);
        void onLongPress(int position,UserFirebase userFirebase);
    }*/
}
