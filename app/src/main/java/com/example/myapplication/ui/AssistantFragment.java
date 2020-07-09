package com.example.myapplication.ui;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AssistantNameAdapter;
import com.example.myapplication.adapter.MilkNameAdapter;
import com.example.myapplication.helper.ClickListener;
import com.example.myapplication.helper.RecyclerTouchListener;
import com.example.myapplication.model.Assistant;
import com.example.myapplication.model.MilkName;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistantFragment extends Fragment {
    private DatabaseReference databaseReference;
    private List<Assistant>assistants;
    private AssistantNameAdapter assistantNameAdapter;
    private RecyclerView recyclerView;
    private SearchView searchView;


    public AssistantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_assistant_view, container, false);
        setHasOptionsMenu(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Milky").child("Assistant");
        assistants = new ArrayList<>();


        recyclerView = root.findViewById(R.id.recycler_view);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                notification("Do you want to delete or update Distributor", "Distributor", position);
            }
        }));


        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_milk_menu, menu);
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
                assistantNameAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                assistantNameAdapter.getFilter().filter(query);
                return false;
            }
        });

    }
    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         return true;
     }
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                alert();

                break;

        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart() {
        super.onStart();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                assistants.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Assistant artist = postSnapshot.getValue(Assistant.class);
                    //adding artist to the list
                    assistants.add(artist);
                }
                assistantNameAdapter = new AssistantNameAdapter(getActivity(), assistants);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(assistantNameAdapter);
                assistantNameAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }






    public void notification(String message, String title, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              /*String fid =contactList.get(position).getId();
              databaseReference.child(fid).removeValue();*/
                dialog.dismiss();
                deletenotification("Are You Sure You Want to delete "+assistants.get(position).getName(),"Delete",position);

                /*contactList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, contactList.size());*/
            }
        });
        builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                alertUpdate(position);


                // User cancelled the dialog
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void deletenotification(String message, String title, final int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseReference.child(assistants.get(position).getId()).removeValue();

                // User cancelled the dialog
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void alert(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_alert_item, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        TextView textView = dialogView.findViewById(R.id.textView);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
        textView.setText("Add Distributor");
        editText.setHint("Distrubutor");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReference.push().getKey();
                Assistant assistant = new Assistant(id,editText.getText().toString().trim());
                databaseReference.child(id).setValue(assistant);
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void alertUpdate(final int position){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_alert_item, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        TextView textView = dialogView.findViewById(R.id.textView);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        editText.setText(assistants.get(position).getName());
        textView.setText("Update Distributor");
        editText.setHint("Distrubutor");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Assistant assistant = new Assistant(assistants.get(position).getId(),editText.getText().toString().trim());
                databaseReference.child(assistants.get(position).getId()).setValue(assistant);
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}
