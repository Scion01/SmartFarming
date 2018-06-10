package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class solutionAcitivty extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipe;
    private TextView subject;
    private TextView desc;
    private TextView solution;
    private TextView references;
    private TextView references2;
    private TextView references3;
    private String[] multiplerefs;
    private Button makeCall;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_acitivty);
        Bundle extras= getIntent().getExtras();
        final Integer position = extras.getInt("position");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("queries");

        if(checkConnection() ==1){


        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }

        subject = (TextView) findViewById(R.id.subjectForSolution);
        desc = findViewById(R.id.descForSolution);
        solution = findViewById(R.id.solutionForSolution);
        references = findViewById(R.id.referenceForSolution);
        references2 = findViewById(R.id.referenceForSolution2);
        references3 = findViewById(R.id.referenceForSolution3);

        references.setOnClickListener(this);
        references2.setOnClickListener(this);
        references3.setOnClickListener(this);

        makeCall= findViewById(R.id.request_call);
        swipe= findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), solutionAcitivty.class);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                finish();
                            }
                        }, 1000);

            }
        });

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),NotSatisfiedCallRequest.class));
                finish();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
                String phone=sharedPref.getString("phone","");
                String[] inputs=dataSnapshot.getKey().toString().split(" ");
                if(inputs[0].equals(phone)) {

                    if(i<=position )
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().toString().equals("subject"))
                            subject.setText(ds.getValue().toString());
                        if (ds.getKey().toString().equals("desc"))
                            desc.setText(ds.getValue().toString());
                        if (ds.getKey().toString().equals("ref")) {
                            multiplerefs = ds.getValue().toString().split(" ");
                        }
                        if (ds.getKey().toString().equals("solution"))
                            solution.setText(ds.getValue().toString());

                            //Toast.makeText(getApplicationContext(),ds.getValue().toString(), Toast.LENGTH_SHORT).show();



                    }


                    i++;
                }
                else {
                    return;
                }
                if(i==position+1) {
                    String str = "None";
                    if (multiplerefs[0].equals(str)) {
                        references.setText("No references provided");
                        references.setClickable(false);
                    } else {
                        switch (multiplerefs.length) {
                            case 1:
                                references.setText("Click here for 1st Reference");
                                references2.setVisibility(View.INVISIBLE);
                                references3.setVisibility(View.INVISIBLE);
                                references.setHighlightColor(Color.BLUE);
                                references.setTextColor(Color.MAGENTA);
                                break;
                            case 2:
                                references.setText("Click here for 1st Reference");
                                references2.setText("Click here for 2nd Reference");
                                references2.setVisibility(View.VISIBLE);
                                references.setTextColor(Color.MAGENTA);
                                references2.setTextColor(Color.MAGENTA);
                                break;
                            case 3:
                                references.setText("Click here for 1st Reference");
                                references2.setText("Click here for 2nd Reference");
                                references3.setText("Click here for 3rd Reference");
                                references2.setVisibility(View.VISIBLE);
                                references3.setVisibility(View.VISIBLE);
                                references.setTextColor(Color.MAGENTA);
                                references2.setTextColor(Color.MAGENTA);
                                references3.setTextColor(Color.MAGENTA);
                                break;
                        }
                    }
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
    }
    int checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return(1);
        }
        else{
            return(0);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == references){
            Intent intent = new Intent(getApplicationContext(), WebViewForRef.class);
            intent.putExtra("URL", multiplerefs[0]);
            startActivity(intent);
            overridePendingTransition(R.anim.entry_from_left, R.anim.exit_from_left);
        }
        else if(v == references2){
            Intent intent = new Intent(getApplicationContext(), WebViewForRef.class);
            intent.putExtra("URL", multiplerefs[1]);
            startActivity(intent);
            overridePendingTransition(R.anim.entry_from_left, R.anim.exit_from_left);
        }
        else if(v == references3){
            Intent intent = new Intent(getApplicationContext(), WebViewForRef.class);
            intent.putExtra("URL", multiplerefs[2]);
            startActivity(intent);
            overridePendingTransition(R.anim.entry_from_left, R.anim.exit_from_left);
        }
    }
}
