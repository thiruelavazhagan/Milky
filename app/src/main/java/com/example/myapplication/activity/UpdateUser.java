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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MilkAdapter;
import com.example.myapplication.helper.MultiSelectionSpinner;
import com.example.myapplication.helper.Validation;
import com.example.myapplication.model.Assistant;
import com.example.myapplication.model.Milk;
import com.example.myapplication.model.MilkName;
import com.example.myapplication.model.UserFirebaseAdd;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateUser extends AppCompatActivity {
   private String sname,sphone,saddress,smilk,smilkcq,sstart,sperday,sdays,sassspinner;
    private MultiSelectionSpinner spinner;
    private Spinner CollectionSpinner;
    public RecyclerView recyclerView;
    private Validation validation;
    private String[] selec;
    List<Milk> productList;
    MilkAdapter milkAdapter;
    private Button selectMilk;
    private  Button add;
    private String etname, etphone, etaddress, etstart, etmilk,etdayspinner,sid,sass;
    private TextInputEditText name, phone, address;
    private ProgressDialog progress;
    private static boolean check;
    private DatabaseReference databaseReference;
    private UserFirebaseAdd userFirebase;
    private Spinner daySpinner,assspinner;
    private  TextView textView;
    private DatabaseReference milkdata;
    private List<MilkName>milkname;
    private List<String>milknames;
    String[] selectedmilk;
    private List<Assistant>assistants;
    private List<String>assis;
    private HashMap<String,String> hashMap;
    private HashMap<String,String> milkhashMap;
    int pos3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Bundle extras = getIntent().getExtras();

        productList = new ArrayList<>();
        milkname = new ArrayList<>();
        milknames = new ArrayList<>();
        milknames = new ArrayList<>();
        assistants = new ArrayList<>();
        assis = new ArrayList<>();
        hashMap = new HashMap<>();
        milkhashMap = new HashMap<>();


        milkdata = FirebaseDatabase.getInstance().getReference("Milky");
      /*  String[] ass = {"Owner","Assistance 1","Assistance 2"};*/
        String[] collection = {"Prepaid(1-30)", "Prepaid(16-15)", "Postpaid(1-30)","PostPaid(16-15)"};
        String[] day = {"Monthly","Fixed Days"};
        selectedmilk = new String[1];

        recyclerView = (RecyclerView) findViewById(R.id.rec);







        if(extras!=null){
            sname = extras.getString("name");
            sphone =extras.getString("phone");
            saddress =extras.getString("address");
            smilk =extras.getString("milk");
            smilkcq =extras.getString("milkcq");
            sstart =extras.getString("start");
            sperday =extras.getString("perday");
            sdays =extras.getString("days");
            sid =extras.getString("id");
            sass = extras.getString("ass");

        }

        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        CollectionSpinner = (Spinner) findViewById(R.id.spinner);
        daySpinner = (Spinner) findViewById(R.id.spinner2);
        assspinner = (Spinner) findViewById(R.id.spinner4);

        selectMilk = (Button) findViewById(R.id.button);
        add = (Button) findViewById(R.id.button2);



        name = (TextInputEditText) findViewById(R.id.name);
        phone = (TextInputEditText) findViewById(R.id.phone);
        address = (TextInputEditText) findViewById(R.id.address);

        textView = (TextView)findViewById(R.id.textView2);
        textView.setText("Update User");
        add.setText("Update");

        validation = new Validation();


        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");

        name.setText(sname);
        phone.setText(sphone);
        address.setText(saddress);

        int pos = new ArrayList<String>(Arrays.asList(collection)).indexOf(sstart);
        int pos2 = new ArrayList<String>(Arrays.asList(day)).indexOf(sdays);






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








        CollectionSpinner.setSelection(pos);
        daySpinner.setSelection(pos2);



        selectMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                       /* recyclerView.setVisibility(View.GONE);
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
                sassspinner = hashMap.get(assspinner.getSelectedItem().toString().trim());

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

                    UpdateUser.AsyncTaskExample asyncTask=new UpdateUser.AsyncTaskExample();
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
            progress = new ProgressDialog(UpdateUser.this);
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

            userFirebase = new UserFirebaseAdd(sid,etname,etphone,etaddress,etstart,etmilk,milkcq,perday,etdayspinner,sassspinner);
            databaseReference.child("Users").child(sid).setValue(userFirebase);

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
        s=validation.removeLastChar(s);
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


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    MilkName artist = postSnapshot.getValue(MilkName.class);

                    milkname.add(artist);

                }
                for (int i=0;i<=milkname.size()-1;i++){
                    milknames.add(milkname.get(i).getName());
                    milkhashMap.put(milkname.get(i).getName(),milkname.get(i).getId());
                }

                Log.e("smilk","smilk "+smilk);

                if(smilk.contains(",")){
                    selectedmilk= changeIdToName(smilk).split(",");

                }
                else {
                    for (Map.Entry<String, String> entry : milkhashMap.entrySet()) {
                        /*Log.e("sd","sdasdasd"+entry.getValue());
                        Log.e("sd","sdasdasd"+smilk.trim());*/
                        if (entry.getValue().equals(smilk.trim())){
                            selectedmilk[0]=entry.getKey();
                            break;
                        }
                    }

                }


                spinner.setItems(milknames);

                Log.e("aa","sd "+Arrays.toString(selectedmilk));

                spinner.setSelection(selectedmilk);

                milkForm();




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
        assspinner.setAdapter(adapter3);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(sass)) {

                pos3= new ArrayList<String>(assis).indexOf(entry.getKey());
                break;
            }

        }

        assspinner.setSelection(pos3);
    }

    public String changeIdToName(String value){
        String data = "";
        String[]mid = value.split(",");
        for (int i =0;i<=mid.length-1;i++) {
            for (Map.Entry<String, String> entry : milkhashMap.entrySet()) {
                Log.e("sd","Value "+entry.getValue());
                Log.e("sd","o Value "+mid[i].trim());
                if (entry.getValue().equals(mid[i].trim())){

                    data = data+entry.getKey()+",";
                    break;
                }

            }

        }
        data = validation.removeLastChar(data);
        Log.e("data","sds"+data);
        return data;
    }

    public void milkForm(){
        if(smilkcq.contains(",")){

            String[] data =smilkcq.split(",");
            String[] data2;
            String nam="";
            productList.clear();
            for(String s:data){
                data2 = s.split("@");
                Log.e("tets","s "+Arrays.toString(data2));

                for (Map.Entry<String, String> entry : milkhashMap.entrySet()) {
                        /*Log.e("sd","sdasdasd"+entry.getValue());
                        Log.e("sd","sdasdasd"+smilk.trim());*/
                    if (entry.getValue().equals(data2[0].trim())) {
                        nam = entry.getKey();
                        break;
                    }
                }

                productList.add(new Milk(nam,data2[1],data2[2]));

            }
            setrec();

        }

        else {
            String[] data;
            data = smilkcq.split("@");
            String nams = "";
            productList.clear();
            for (Map.Entry<String, String> entry : milkhashMap.entrySet()) {
                        /*Log.e("sd","sdasdasd"+entry.getValue());
                        Log.e("sd","sdasdasd"+smilk.trim());*/
                if (entry.getValue().equals(data[0].trim())) {
                    nams = entry.getKey();
                    break;
                }
            }
            productList.add(new Milk(nams, data[1], data[2]));
            setrec();
        }
    }
    public String milksWithId(String da){
        String name = "";
        String mid;
        String[]sp=da.split(",");
        for (int i=0;i<=sp.length-1;i++){
            mid = milkhashMap.get(sp[i].trim());
            name = name+mid+",";
        }
        name = validation.removeLastChar(name);
        return name;
    }
}
