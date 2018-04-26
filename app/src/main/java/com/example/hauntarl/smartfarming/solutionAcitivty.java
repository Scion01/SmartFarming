package com.example.hauntarl.smartfarming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class solutionAcitivty extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView subject;
    private TextView desc;
    private TextView solution;
    private TextView references;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_acitivty);
        Bundle extras= getIntent().getExtras();
        final Integer position = extras.getInt("position");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("queries");

        subject = (TextView) findViewById(R.id.subjectForSolution);
        desc = findViewById(R.id.descForSolution);
        solution = findViewById(R.id.solutionForSolution);
        references = findViewById(R.id.referenceForSolution);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(i<=position) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.getKey().toString().equals("subject"))
                            subject.setText(ds.getValue().toString());
                        if (ds.getKey().toString().equals("description"))
                            desc.setText(ds.getValue().toString());
                        if (ds.getKey().toString().equals("ref"))
                            references.setText(ds.getValue().toString());
                        if (ds.getKey().toString().equals("solution"))
                            solution.setText(ds.getValue().toString());

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
