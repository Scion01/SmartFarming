package com.example.hauntarl.smartfarming;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EntryActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    ArrayList<String> entryList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("activeProjects");

        listView = findViewById(R.id.listView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_list,R.id.headView,entryList);
        listView.setAdapter(arrayAdapter);



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
