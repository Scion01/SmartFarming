package com.example.hauntarl.smartfarming;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Accounting extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ArrayList<String> entryList = new ArrayList<String>();
    private ListView listView;
    String phone;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting);
        Bundle extras= getIntent().getExtras();
        final Integer position = extras.getInt("position");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("activeProjects");
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        phone=sharedPref.getString("phone","");
        databaseReference = databaseReference.child(phone);

        listView = findViewById(R.id.accountingList);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_list,R.id.headView,entryList);
        listView.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(i<=position) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {


                        //Toast.makeText(getApplicationContext(),ds.getValue().toString(), Toast.LENGTH_SHORT).show();



                    }
                    i++;
                }
                else
                    return;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
