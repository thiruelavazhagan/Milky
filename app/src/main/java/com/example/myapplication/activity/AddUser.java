package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.ContactsAdapter;
import com.example.myapplication.adapter.MilkAdapter;
import com.example.myapplication.helper.MultiSelectionSpinner;
import com.example.myapplication.R;
import com.example.myapplication.helper.Validation;
import com.example.myapplication.model.Assistant;
import com.example.myapplication.model.Milk;
import com.example.myapplication.model.MilkName;
import com.example.myapplication.model.UserFirebase;
import com.example.myapplication.model.UserFirebaseAdd;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUser extends AppCompatActivity {
    private MultiSelectionSpinner spinner;
    private Spinner CollectionSpinner;
    public  RecyclerView recyclerView;
    private Validation validation;
    private String[] selec;
    List<Milk> productList;
    MilkAdapter milkAdapter;
    private Button selectMilk;
    private  Button add;
    private String etname, etphone, etaddress, etstart, etmilk,etdayspinner,eass;
    private TextInputEditText name, phone, address;
    private ProgressDialog progress;
    private static boolean check;
    private DatabaseReference databaseReference;
    private UserFirebaseAdd userFirebase;
    private Spinner daySpinner,assistance;
    private DatabaseReference milkdata;
    private List<MilkName>milkname;
    private List<String>milknames;
    private List<Assistant>assistants;
    private List<String>assis;
    private HashMap<String,String>hashMap;
    private HashMap<String,String>milkhashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        productList = new ArrayList<>();

        /*String[] ass = {"Owner","Assistant 1","Assistant 2"};*/
        String[] collection = {"Prepaid(1-30)", "Prepaid(16-15)", "Postpaid(1-30)","PostPaid(16-15)"};
        String[] day = {"Monthly","Fixed Days"};

        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        CollectionSpinner = (Spinner) findViewById(R.id.spinner);
        assistance = (Spinner)findViewById(R.id.spinner3);
        daySpinner = (Spinner) findViewById(R.id.spinner2);

        selectMilk = (Button) findViewById(R.id.button);
        add = (Button) findViewById(R.id.button2);

        recyclerView = (RecyclerView) findViewById(R.id.rec);

        name = (TextInputEditText) findViewById(R.id.name);
        phone = (TextInputEditText) findViewById(R.id.phone);
        address = (TextInputEditText) findViewById(R.id.address);

        validation = new Validation();
        milkname = new ArrayList<>();
        milknames = new ArrayList<>();
        assistants = new ArrayList<>();
        assis = new ArrayList<>();
        hashMap = new HashMap<>();
        milkhashMap = new HashMap<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");
        milkdata = FirebaseDatabase.getInstance().getReference("Milky");



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


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, day) {
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
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter2);



        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assis) {
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
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assistance.setAdapter(adapter3);






//-----------------------------multi selection Spinner-----------------------------------------





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
                        Snackbar.make(v, "Select atleast one milk", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        productList.clear();
                        milkAdapter.notifyDataSetChanged();
                        /*recyclerView.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);*/
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
                etmilk = milksWithId(spinner.getSelectedItemsAsString().trim());

                etstart = CollectionSpinner.getSelectedItem().toString().trim();
                etdayspinner = daySpinner.getSelectedItem().toString().trim();
                eass = hashMap.get(assistance.getSelectedItem().toString().trim());

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

                String milkcq=milkCQ().trim();
                String perday = perDay().trim();
                milkcq = milkcq.replace(" ","");
                etmilk = etmilk.replace(" ","");
             /*   for (int i=0;i>=10;i++){
                    String id = databaseReference.push().getKey();
                    userFirebase = new UserFirebaseAdd(id,etname,etphone,etaddress,etstart,etmilk,milkcq,perday,etdayspinner);
                    databaseReference.child("Users").child(id).setValue(userFirebase);
                }*/
                String id = databaseReference.push().getKey();
                userFirebase = new UserFirebaseAdd(id,etname,etphone,etaddress,etstart,etmilk,milkcq,perday,etdayspinner,eass);
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
                        finish();
                    }
                }, 2000);


            }else {
                progress.show();
            }
        }
    }

    public String milkCQ(){

        String s ="";
        String milkid="";
        for (int i = 0; i < MilkAdapter.mData.size(); i++) {
            Log.e("milkname","milk "+MilkAdapter.mData.get(i).getName().trim());
            milkid = milkhashMap.get(MilkAdapter.mData.get(i).getName().trim());

            String milkcq = milkid+"@"+MilkAdapter.mData.get(i).getAmount()+"@"+MilkAdapter.mData.get(i).getQty();
            s=s+milkcq+",";

        }
        s=removeLastChar(s);
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

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        milkdata.child("Milk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                milkname.clear();
                milknames.clear();
                milkhashMap.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    MilkName artist = postSnapshot.getValue(MilkName.class);

                    milkname.add(artist);

                }
                for (int i=0;i<=milkname.size()-1;i++){

                    milknames.add(milkname.get(i).getName());
                    milkhashMap.put(milkname.get(i).getName(),milkname.get(i).getId());
                }
                for (Map.Entry<String, String> e : milkhashMap.entrySet()) {
                    //to get key
                   Log.e("df","key "+e.getKey());
                    Log.e("df","value "+e.getValue());

                }
                spinner.setItems(milknames);
                spinner.setSelection(0);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        milkdata.child("Assistant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assistants.clear();
                assis.clear();
                hashMap.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assistant assistant = postSnapshot.getValue(Assistant.class);
                    assistants.add(assistant);
                }
                for (int i=0;i<=assistants.size()-1;i++){
                    assis.add(assistants.get(i).getName());
                    hashMap.put(assistants.get(i).getName(),assistants.get(i).getId());
                }
                /*for (Map.Entry<String, String> e : hashMap.entrySet()) {
                    //to get key
                    Log.e("df","key "+e.getKey());
                    Log.e("df","value "+e.getValue());

                }*/

                spinner();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void spinner(){
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assis) {
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
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assistance.setAdapter(adapter3);
    }

    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }
    public String milksWithId(String da){
        String name = "";
        String mid;
        String[]sp=da.split(",");
        for (int i=0;i<=sp.length-1;i++){
            mid = milkhashMap.get(sp[i].trim());
            name = name+mid+",";
        }
        name = removeLastChar(name);
     return name;
    }


}
