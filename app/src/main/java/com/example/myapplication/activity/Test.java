package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.model.UserFirebaseAdd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    List<UserFirebaseAdd> userf;
    DatabaseReference databaseReference;
    UserFirebaseAdd userFirebaseAdd;
    private ProgressDialog progress;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        databaseReference = FirebaseDatabase.getInstance().getReference("Milky");

        userf = new ArrayList<>();



        for (int i=0;i<=12000;i++){
            String id = String.valueOf(i);
            Log.e("t","sds"+id);

            userFirebaseAdd = new UserFirebaseAdd(id,"sasddsd","dsadsdsd","sdasds","asAAS","ASassAS","ASasas","asASAS","SASDSD","asd");
            databaseReference.child("Users").child(id).setValue(userFirebaseAdd);

        }

    }

    private class AsyncTaskExample extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("Please wait...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            check = true;


            return check;
        }

        @Override
        protected void onPostExecute(Boolean check) {
            super.onPostExecute(check);
            if (check) {

                progress.hide();

            } else {
                progress.show();
            }
        }
    }

}
