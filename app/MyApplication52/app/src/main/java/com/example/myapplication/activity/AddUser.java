package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.MilkAdapter;
import com.example.myapplication.helper.MultiSelectionSpinner;
import com.example.myapplication.R;
import com.example.myapplication.helper.Validation;
import com.example.myapplication.model.Milk;
import com.example.myapplication.model.UserFirebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUser extends AppCompatActivity {
    private MultiSelectionSpinner spinner;
    private Spinner CollectionSpinner;
    public RecyclerView recyclerView;
    private Validation validation;
    private String[] selec;
    List<Milk> productList;
    MilkAdapter milkAdapter;
    private Button selectMilk, add;
    private String etname, etphone, etaddress, etstart, etmilk;
    private TextInputEditText name, phone, address;
    private ProgressDialog progress;
    private boolean check;
    private DatabaseReference databaseReference;
    private UserFirebase userFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        productList = new ArrayList<>();

        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        String[] collection = {"Start", "End", "Between"};

        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        CollectionSpinner = (Spinner) findViewById(R.id.spinner);

        selectMilk = (Button) findViewById(R.id.button);
        add = (Button) findViewById(R.id.button2);

        recyclerView = (RecyclerView) findViewById(R.id.rec);

        name = (TextInputEditText) findViewById(R.id.name);
        phone = (TextInputEditText) findViewById(R.id.phone);
        address = (TextInputEditText) findViewById(R.id.address);

        validation = new Validation();


        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");



//-----------------------------Payment Start spinner--------------------------------------

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, collection) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                tv.setTextSize(20);

                // Return the view
                return tv;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CollectionSpinner.setAdapter(adapter);
//-----------------------------multi selection Spinner-----------------------------------------


        spinner.setItems(array);
        spinner.setSelection(0);


//------------------------------add value to recyclerview from spinner--------------------------

        selectMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();*/
                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                String s = spinner.getSelectedItemsAsString();
                if (!s.contains(",")) {
                    productList.clear();
                    productList.add(new Milk(s, "1", "1"));

                    if (!s.equalsIgnoreCase("")) {
                        setrec();

                    } else {
                        Toast.makeText(getApplicationContext(), "Select atleast one milk", Toast.LENGTH_SHORT).show();
                        productList.clear();
                        milkAdapter.notifyDataSetChanged();
                    }

                } else {
                    selec = s.split(",");
                    productList.clear();
                    for (String se : selec) {
                        productList.add(new Milk(se, "1", "1"));
                        Log.e("sese", "sd" + se);
                    }
                    setrec();
                }


            }


        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etname = name.getText().toString().trim();
                etphone = phone.getText().toString().trim();
                etaddress = address.getText().toString().trim();
                etmilk = spinner.getSelectedItemsAsString().trim();
                etstart = CollectionSpinner.getSelectedItem().toString().trim();

                if (validation.isEmpty(etname)) {
                    name.setError("Enter name");
                    name.requestFocus();
                } else if (validation.isValidPhone(etphone) && validation.isEmpty(etphone)) {
                    phone.setError("Enter valid phone");
                    phone.requestFocus();
                } else if (validation.isEmpty(etaddress)) {
                    address.setError("Enter address");
                    address.requestFocus();
                } else if (etmilk.equalsIgnoreCase("")) {
                    Snackbar.make(v, "Select atleast one milk", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute();
                }


            }
        });


    }

    public void setrec() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        milkAdapter = new MilkAdapter(this, productList);
        // milkAdapter.setClickListener(this);
        recyclerView.setAdapter(milkAdapter);
    }

    private class AsyncTaskExample extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AddUser.this);
            progress.setMessage("Please wait...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Boolean doInBackground(String... strings) {

                check =true;
                String id = databaseReference.push().getKey();
                String milkcq=milkCQ().trim();
                String perday = perDay().trim();
                milkcq = milkcq.replace(" ","");
                etmilk = etmilk.replace(" ","");

                userFirebase = new UserFirebase(id,etname,etphone,etaddress,etstart,etmilk,milkcq,perday);
                databaseReference.child("Users").child(id).setValue(userFirebase);

            return check;
        }
        @Override
        protected void onPostExecute(Boolean check) {
            super.onPostExecute(check);
            if(check) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.hide();
                    }
                }, 2000);


            }else {
                progress.show();
            }
        }
    }

    public String milkCQ(){
        String s ="";
        for (int i = 0; i < MilkAdapter.mData.size(); i++) {
            String milkcq = MilkAdapter.mData.get(i).getName()+"@"+MilkAdapter.mData.get(i).getAmount()+"@"+MilkAdapter.mData.get(i).getQty();
            s=s+milkcq+",";

        }
        s=validation.lastLetter(s).trim();
        return s;
    }

    public String perDay(){
        String s="";
        double price=0.0,qty,tot=0.0;
        for (int i = 0; i < MilkAdapter.mData.size(); i++) {
            price = Double.parseDouble(MilkAdapter.mData.get(i).getAmount())*Double.parseDouble(MilkAdapter.mData.get(i).getQty());
            tot=tot+price;

        }
        s=String.valueOf(tot).trim();
        return s;
    }


}
