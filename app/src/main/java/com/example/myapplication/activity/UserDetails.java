package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserDetailMilk;
import com.example.myapplication.model.Assistant;
import com.example.myapplication.model.MilkDetail;
import com.example.myapplication.model.MilkName;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserDetails extends AppCompatActivity {
    private String sname,sphone,saddress,smilk,smilkcq,sstart,sperday,sdays,sid,spermon,sassistant;
    private List<MilkDetail> productList;
    private List<Assistant> assistants;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Button payment,history;
    private Calendar c;
    private UserDetailMilk userDetailMilk;
    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
    private TextView tname,tphone,taddress,tstart,tamount,tmon,tdist;
    private int mon,year,date;
    private String month;
    private String status="";
    private List<MilkName>milkNames;
    private HashMap<String,String>hashMap;
    private HashMap<String,String>assishashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_detail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");

        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);

        tname = findViewById(R.id.textView14);
        tphone = findViewById(R.id.textView17);
        taddress = findViewById(R.id.textView18);
        tstart = findViewById(R.id.textView20);
        tmon = findViewById(R.id.textView22);
        tamount = findViewById(R.id.textView24);
        tdist = findViewById(R.id.textView11);

        history = findViewById(R.id.button3);
        payment = findViewById(R.id.button4);


        milkNames = new ArrayList<>();
        hashMap = new HashMap<>();
        assishashMap = new HashMap<>();
        assistants = new ArrayList<>();


        Bundle extras = getIntent().getExtras();
        c = Calendar.getInstance();



       // c.add(Calendar.YEAR,-1);
        year=c.get(Calendar.YEAR);
        mon = c.get(Calendar.MONTH);
        date=c.get(Calendar.DATE);
        Log.e("year","year "+year);

        month=monthName[mon];
        Log.e("month","month "+month);


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
            sassistant =extras.getString("assis");
            Log.e("id","id "+sid);


            if(sstart.equalsIgnoreCase("Postpaid(1-30)")){
                if(mon==0){
                    c.add(Calendar.YEAR,-1);
                    year = c.get(Calendar.YEAR);
                    mon=11;
                    month=monthName[mon];
                }
                else {
                    c.add(Calendar.MONTH,-1);
                    mon = c.get(Calendar.MONTH);
                    Log.e("test","sd"+mon);
                    month=monthName[mon];
                }

            }
            else if(sstart.equalsIgnoreCase("PostPaid(16-15)")){
                if(mon==0){
                    c.add(Calendar.YEAR,-1);
                    year = c.get(Calendar.YEAR);
                    mon=11;
                    month=monthName[mon];
                }
                else {
                    c.add(Calendar.MONTH,-1);
                    mon = c.get(Calendar.MONTH);
                    Log.e("test","sd"+mon);
                    month=monthName[mon];
                }
            }


            /*if(smilkcq.contains(",")){

                String[] data =smilkcq.split(",");
                String[] data2;
                productList.clear();
                for(String s:data){
                    data2 = s.split("@");
                    Log.e("tets","s "+ Arrays.toString(data2));

                    productList.add(new MilkDetail(data2[0],data2[1],data2[2]));

                }
                setrec();

            }
            else {
                String[] data;
                data = smilkcq.split("@");
                productList.clear();
                productList.add(new MilkDetail(data[0], data[1], data[2]));
                setrec();
            }*/
            tname.setText(sname);
            tphone.setText(sphone);
            taddress.setText(saddress);
            tstart.setText(sstart);
            tmon.setText(month);
            tamount.setText(spermon);


            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*startActivity(new Intent(getApplicationContext(), PaymentHistoryActivity.class));*/
                    Intent intent = new Intent(getApplicationContext(),PaymentHistoryActivity.class);
                    intent.putExtra("Id",sid);
                    startActivity(intent);

                }
            });

            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!status.equalsIgnoreCase("paid")){
                        deletenotification("Are you  Sure you want to change payment status to paid of Rs "+spermon+" for user "+sname,"Payment Status",0);
                    }
                    else {
                        Snackbar.make(v, "User already made his payment", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
            });



        }
    }
    public void setrec() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userDetailMilk = new UserDetailMilk(this, productList);
        recyclerView.setAdapter(userDetailMilk);
    }
    /*public String monthPrice(String start,String totdays,double peramm,int year,int mon,int date){
        double perMonth=0.0;

        Calendar mycal = new GregorianCalendar(year, mon, date);

        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        Log.e("tot days",""+daysInMonth);

        if(totdays.equalsIgnoreCase("Monthly")) {
            if (start.equalsIgnoreCase("Prepaid(1-30)")) {
                perMonth = peramm*daysInMonth;

            } else if (start.equalsIgnoreCase("Prepaid(16-15)")) {
                perMonth = peramm*daysInMonth;

            } else if (start.equalsIgnoreCase("Postpaid(1-30)")) {
                perMonth = peramm*daysInMonth;

            } else if (start.equalsIgnoreCase("PostPaid(16-15)")) {
                perMonth = peramm*daysInMonth;
            }
        }
        else {
            perMonth = peramm*31;
        }

        return String.valueOf(perMonth);
    }*/
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("MonthlyPayment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status = dataSnapshot.child(sid).child(String.valueOf(year)).child(month).child("status").getValue(String.class);
                spermon = dataSnapshot.child(sid).child(String.valueOf(year)).child(month).child("amount").getValue(String.class);
                tamount.setText(spermon);
             //   Log.e("status","ss"+status);
                Log.e("dfdf","fd"+status);
                if(status != null){

                    if(status.equalsIgnoreCase("paid")){
                        payment.setBackgroundColor(Color.parseColor("#228B22"));
                        payment.setText("Paid");

                    }
                    else if(status.equalsIgnoreCase("crossed")) {
                        payment.setBackgroundColor(Color.parseColor("#ff0006"));
                        payment.setText("Crossed");
                    }
                    else {
                        payment.setBackgroundColor(Color.parseColor("#D81B60"));
                        payment.setText("Pending");
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"User Not yet added",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Milk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                milkNames.clear();
                hashMap.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MilkName milkName = postSnapshot.getValue(MilkName.class);
                    milkNames.add(milkName);
                }
                for (int i=0;i<=milkNames.size()-1;i++){
                 hashMap.put(milkNames.get(i).getName(),milkNames.get(i).getId());
                }
              milkForm();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Assistant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assistants.clear();
                assishashMap.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assistant assistant = postSnapshot.getValue(Assistant.class);
                    assistants.add(assistant);
                }
                for (int i=0;i<=assistants.size()-1;i++){
                    assishashMap.put(assistants.get(i).getId(),assistants.get(i).getName());
                }
                tdist.setText(assishashMap.get(sassistant));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    public void deletenotification(String message, String title, final int position) {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(c);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseReference.child("MonthlyPayment").child(sid).child(String.valueOf(year)).child(month).child("status").setValue("paid");
                databaseReference.child("MonthlyPayment").child(sid).child(String.valueOf(year)).child(month).child("date").setValue("Paid "+formattedDate);

                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

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

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        /*Log.e("sd","sdasdasd"+entry.getValue());
                        Log.e("sd","sdasdasd"+smilk.trim());*/
                    if (entry.getValue().equals(data2[0].trim())) {
                        nam = entry.getKey();
                        break;
                    }
                }

                productList.add(new MilkDetail(nam,data2[1],data2[2]));

            }
            setrec();

        }

        else {
            String[] data;
            data = smilkcq.split("@");
            String nams = "";
            productList.clear();
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        /*Log.e("sd","sdasdasd"+entry.getValue());
                        Log.e("sd","sdasdasd"+smilk.trim());*/
                if (entry.getValue().equals(data[0].trim())) {
                    nams = entry.getKey();
                    break;
                }
            }
            productList.add(new MilkDetail(nams, data[1], data[2]));
            setrec();
        }
    }

}
