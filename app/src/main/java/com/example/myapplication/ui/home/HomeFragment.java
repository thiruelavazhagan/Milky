package com.example.myapplication.ui.home;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.UpdateUser;
import com.example.myapplication.activity.UserDetails;
import com.example.myapplication.adapter.ContactsAdapter;
import com.example.myapplication.helper.ClickListener;
import com.example.myapplication.helper.RecyclerTouchListener;
import com.example.myapplication.model.MonthlyPayment;
import com.example.myapplication.model.UserFirebaseAdd;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;
    private DatabaseReference databaseReference,date;
    private RecyclerView recyclerView;
    private List<UserFirebaseAdd> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    private Calendar calendar;
    private int cyear,cmon,cdate;
    private String month;
    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("Milky").child("Users");

        date = FirebaseDatabase.getInstance().getReference("Milky").child("MonthlyPayment");

        calendar = Calendar.getInstance();



        /*date.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(String.valueOf(cyear))) {
                   date.child(String.valueOf(cyear));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
      //  mainActivity.fab.hide();
        contactList = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                Intent intent = new Intent(getActivity(), UserDetails.class);
                intent.putExtra("name",contactList.get(position).getName());
                intent.putExtra("phone",contactList.get(position).getPhone());
                intent.putExtra("address",contactList.get(position).getAddress());
                intent.putExtra("milk",contactList.get(position).getMilk());
                intent.putExtra("milkcq",contactList.get(position).getMilkcq());
                intent.putExtra("start",contactList.get(position).getStart());
                intent.putExtra("days",contactList.get(position).getDays());
                intent.putExtra("perday",contactList.get(position).getPerday());
                intent.putExtra("id",contactList.get(position).getId());
                intent.putExtra("assis",contactList.get(position).getAssistance());
                Log.e("sd","id "+contactList.get(position).getId());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                notification("Do you want to delete or update contact", "Contact", position);
            }
        }));


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                String pmonth="";
                String pyear="";

                for (int i=0;i<=contactList.size()-1;i++){

                    cyear=calendar.get(Calendar.YEAR);
                    cmon = calendar.get(Calendar.MONTH);
                    cdate=calendar.get(Calendar.DATE);
                    month = monthName[cmon];

                    final String iid=contactList.get(i).getId();
                    final String nname = contactList.get(i).getName();
                    String mont = contactList.get(i).getStart();
                    String days = contactList.get(i).getDays();
                    double perday = Double.parseDouble(contactList.get(i).getPerday());
                    final String monthpri = monthPrice(mont,days,perday,cyear,cmon,cdate);

                    date.child(String.valueOf(cyear)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.hasChild(month)) {
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("id").setValue(iid);
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("name").setValue(nname);
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("status").setValue("pending");
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("amount").setValue(monthpri);
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("permonth").setValue(monthpri);
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("month").setValue(month);
                                date.child(iid).child(String.valueOf(cyear)).child(month).child("date").setValue("pending");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    if(cmon==0){
                        int premon = 12;

                        pyear = String.valueOf(cyear-1);
                        pmonth =monthName[premon];
                        Log.e("d","sfddf"+pmonth);
                    }
                    else {
                        int premon = calendar.get(Calendar.MONTH)-1;
                        pmonth =monthName[premon];
                        Log.e("d","sfddf"+pmonth);
                        pyear = String.valueOf(cyear);
                    }

                    final String finalPmonth = pmonth;
                    final String finalPyear = pyear;
                    date.child(finalPyear).child(pmonth).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.hasChild(month)) {
                                date.child(iid).child(finalPyear).child(finalPmonth).child("id").setValue(iid);
                                date.child(iid).child(finalPyear).child(finalPmonth).child("name").setValue(nname);
                                date.child(iid).child(finalPyear).child(finalPmonth).child("status").setValue("pending");
                                date.child(iid).child(finalPyear).child(finalPmonth).child("amount").setValue(monthpri);
                                date.child(iid).child(finalPyear).child(finalPmonth).child("permonth").setValue(monthpri);
                                date.child(iid).child(finalPyear).child(finalPmonth).child("month").setValue(finalPmonth);
                                date.child(iid).child(finalPyear).child(finalPmonth).child("date").setValue("pending");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                  //  MonthlyPayment monthlyPayment = new MonthlyPayment(iid,nname,"pending",monthpri);



                }

                Toast.makeText(getActivity(), "sdas", Toast.LENGTH_SHORT).show();
                // do stuff
                return true;

        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("test","sdsd");
        //attaching value event listener
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contactList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    UserFirebaseAdd artist = postSnapshot.getValue(UserFirebaseAdd.class);
                    //adding artist to the list
                    contactList.add(artist);
                }
                mAdapter = new ContactsAdapter(getActivity(), contactList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }
    public void notification(String message, String title, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              /*String fid =contactList.get(position).getId();
              databaseReference.child(fid).removeValue();*/
                dialog.dismiss();
                deletenotification("Are you sure you want to delete "+contactList.get(position).getName(),"Delete",position);


                /*contactList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, contactList.size());*/
            }
        });
        builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getActivity(), UpdateUser.class);
                intent.putExtra("name",contactList.get(position).getName());
                intent.putExtra("phone",contactList.get(position).getPhone());
                intent.putExtra("address",contactList.get(position).getAddress());
                intent.putExtra("milk",contactList.get(position).getMilk());
                intent.putExtra("milkcq",contactList.get(position).getMilkcq());
                intent.putExtra("start",contactList.get(position).getStart());
                intent.putExtra("days",contactList.get(position).getDays());
                intent.putExtra("perday",contactList.get(position).getPerday());
                intent.putExtra("id",contactList.get(position).getId());
                intent.putExtra("ass",contactList.get(position).getAssistance());
                Log.e("sd","id "+contactList.get(position).getId());

                startActivity(intent);
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void deletenotification(String message, String title, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String fid =contactList.get(position).getId();
                databaseReference.child(fid).removeValue();
                contactList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, contactList.size());
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public String monthPrice(String start,String totdays,double peramm,int year,int mon,int date){
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
    }
}
