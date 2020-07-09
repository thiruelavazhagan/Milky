package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.myapplication.R;
import com.example.myapplication.adapter.PaymentHistoryAdapter;
import com.example.myapplication.model.PaymentHistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentHistoryActivity extends AppCompatActivity {
    private PaymentHistoryActivity paymentHistoryActivity;
    private PaymentHistory paymentHistory;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<PaymentHistory>paymentHistories;
    private String Id;
    private PaymentHistoryAdapter paymentHistoryAdapter;
    private String cyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        cyear = String.valueOf(year);

        recyclerView = findViewById(R.id.recycler_view);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            Id = extras.getString("Id");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");
        paymentHistories = new ArrayList<>();







    }



    @Override
    protected void onStart() {
        super.onStart();


        databaseReference.child("MonthlyPayment").child(Id).child(cyear).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paymentHistories.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PaymentHistory paymentHistory = postSnapshot.getValue(PaymentHistory.class);
                    paymentHistories.add(paymentHistory);

                }

                paymentHistoryAdapter = new PaymentHistoryAdapter(getApplicationContext(), paymentHistories);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(paymentHistoryAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
