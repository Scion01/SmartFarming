package com.example.hauntarl.smartfarming;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EntryActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    String finalString;

    ArrayList<String> entryList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("activeProjects");
        //at this point all data is transferred by this number

        listView = findViewById(R.id.listView);
        finalString= null;


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_list,R.id.headView,entryList);
        listView.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    finalString="";
                    for(DataSnapshot ds1: ds.getChildren()) {
                        Log.i("ds1", ds1.toString());
                        finalString=finalString+"  "+ds1.getValue().toString();



                    }
                    entryList.add(finalString);
                    arrayAdapter.notifyDataSetChanged();
                    //findViewById(R.id.removeText).setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                }
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



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewentryActivity.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                finish();
            }
        });


    }


}
