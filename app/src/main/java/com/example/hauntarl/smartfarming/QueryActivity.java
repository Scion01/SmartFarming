package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QueryActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    String finalString;
    private Integer check=0;

    ArrayList<String> entryList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        if(checkConnection() ==1){


        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("queries");
        //at this point all data is transferred by this number

        listView = findViewById(R.id.queryListView);
        finalString= null;

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_list,R.id.headView,entryList);
        listView.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                finalString=" ";
                SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
                String phone=sharedPref.getString("phone","");
                String[] inputs=dataSnapshot.getKey().toString().split(" ");
                if(inputs[0].equals(phone)) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.getKey().toString().equals("subject")) {
                            finalString = finalString + ds.getValue().toString();
                            //Toast.makeText(getApplicationContext(), ds.getValue().toString(), Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getApplicationContext(),ds.toString(),Toast.LENGTH_SHORT).show();
                    /*if(ds.getKey().toString().equals("solved")) {
                        finalString = finalString + "   ";
                        if(Double.parseDouble(ds.getValue().toString())==0)
                            finalString = finalString + "Solution Pending";
                        else
                            finalString = finalString + "Solved!!";
                    }*/


                    }
                    entryList.add(finalString);
                    arrayAdapter.notifyDataSetChanged();
                    findViewById(R.id.removeView).setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
                pDialog.cancel();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Cannot access cloud services at this point, Sorry!",Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewQueryActivity.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), solutionAcitivty.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.entry_from_left, R.anim.exit_from_left);

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

}
