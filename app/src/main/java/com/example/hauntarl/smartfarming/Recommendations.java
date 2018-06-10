package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recommendations extends AppCompatActivity   {

    private TextView noRecom;
    private ListView listView;
    DatabaseReference firebaseDatabase;
    private List<String> name;
    private List<String> desc;
    private List<Integer> isSolved;
    private List<String> cost;
    private Integer count;
    private Integer countForStopping;
    public Integer buyButtonClicked;
    int checkCount=-1;
    String keyForUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        if(checkConnection() ==1){


        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }
        keyForUpdate=new String();
        isSolved = new ArrayList<Integer>();
        desc = new ArrayList<String>();
        name= new ArrayList<String>();
        cost = new ArrayList<String>();
        count=0;
        checkCount=-1;
        buyButtonClicked=0;
        countForStopping=0;
        noRecom = (TextView) findViewById(R.id.noRecomText);
        listView = findViewById(R.id.listViewForRecom);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(getApplicationContext(),"Row Clicked @" +String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });



        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading..");
        pDialog.setCancelable(false);
        pDialog.show();

        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        String phone=sharedPref.getString("phone","");

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = firebaseDatabase.child("Recommendation");

        try {
            firebaseDatabase = firebaseDatabase.child(phone);
            Query query = firebaseDatabase.orderByChild("isBought").equalTo(0);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()==false) {
                        pDialog.cancel();
                        listView.setVisibility(View.INVISIBLE);
                        noRecom.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        countForStopping++;
                        listView.setVisibility(View.VISIBLE);
                        noRecom.setVisibility(View.INVISIBLE);
                        if(ds.getKey().toString().equals("Name")){
                            name.add(ds.getValue().toString());
                        }
                        else if(ds.getKey().toString().equals("Description")){
                            desc.add(ds.getValue().toString());
                        }
                        else if(ds.getKey().toString().equals("Cost")){
                            cost.add(ds.getValue().toString());
                        }
                        else if(ds.getKey().toString().equals("isBought")){
                            isSolved.add(Integer.valueOf(ds.getValue().toString()));
                            count++;
                        }
                        if(countForStopping >= dataSnapshot.getChildrenCount()){
                            //Toast.makeText(getApplicationContext(),String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                            MyAdapter adapter = new MyAdapter(getApplication(),name,desc,isSolved,cost);
                            listView.setAdapter(adapter);

                            pDialog.cancel();
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
        catch (Exception e){
            Log.d("Error1",e.toString());
            pDialog.cancel();
        }
        DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase1= firebaseDatabase1.child("Recommendation");
        firebaseDatabase1= firebaseDatabase1.child(phone);

    }
    ExpandableRelativeLayout expandableLayout;
    public void expandableButtons(View view) {
        expandableLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        expandableLayout.toggle(); // toggle expand and collapse

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

    class MyAdapter extends ArrayAdapter<String>{
        List<String> name = new ArrayList<String>();
        List<String> desc = new ArrayList<String>();
        List<Integer> isSolved = new ArrayList<Integer>();;
        List<String> cost = new ArrayList<String>();;
        public MyAdapter(@NonNull Context context, List<String> name1, List<String> desc1,List<Integer> isSolved1, List<String> cost1 ) {
            super(context, R.layout.layout_for_recommendations,R.id.name,name1);
            this.name=name1;
            this.desc=desc1;
            this.cost=cost1;
            this.isSolved=isSolved1;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View row = layoutInflater.inflate(R.layout.layout_for_recommendations,parent,false);

            final TextView myDesc = (TextView) row.findViewById(R.id.descOfProduct);
            final Button myName = (Button) row.findViewById(R.id.expandableButton);

            final TextView myCost = (TextView) row.findViewById(R.id.costOfProduct);
            Button buyButton = (Button) row.findViewById(R.id.buyButton);
            final int pos=position;
            myName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExpandableRelativeLayout expandableLayout;

                    expandableLayout = (ExpandableRelativeLayout) row.findViewById(R.id.expandableLayout);
                    expandableLayout.toggle(); // toggle expand and collapse

                }
        });
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"Button Clicked @" +String.valueOf(pos), Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(Recommendations.this, SweetAlertDialog.WARNING_TYPE)
    .setTitleText("Are you sure u want to buy this?")
                            .setContentText("Pay Rs. "+cost.get(position))
                            .setCancelText("No, cancel!")
                            .setConfirmText("Yes, i want to buy!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    buyWithCaution(position);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                }
            });

            myCost.setText(cost.get(position));
            myName.setText(name.get(position));
            myDesc.setText(desc.get(position));

            return row;
        }

    }

    void buyWithCaution(final int position){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff558b"));
        pDialog.getProgressHelper().setBarWidth(20);
        pDialog.getProgressHelper().setCircleRadius(200);
        pDialog.setTitleText("Please wait..");
        pDialog.setCancelable(false);
        pDialog.show();

        final int posz=position;
        checkCount=-1;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference= databaseReference.child("TransactionRequests");
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        String phone=sharedPref.getString("phone","");
        databaseReference = databaseReference.child(phone);
        databaseReference=databaseReference.push();
        databaseReference.setValue(new transactionRequest("default address",name.get(position),desc.get(position),cost.get(position),0,"None"));

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = firebaseDatabase.child("Recommendation");
        firebaseDatabase = firebaseDatabase.child(phone);

        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getApplicationContext(),dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                checkCount++;

                if(checkCount == posz ) {
                    keyForUpdate = dataSnapshot.getKey().toString();
                    //Toast.makeText(getApplicationContext(), keyForUpdate, Toast.LENGTH_SHORT).show();
                    DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance().getReference();
                    SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
                    String phone1=sharedPref.getString("phone","");
                    firebaseDatabase1 = firebaseDatabase1.child("Recommendation");
                    firebaseDatabase1 = firebaseDatabase1.child(phone1);
                    firebaseDatabase1 = firebaseDatabase1.child(keyForUpdate);
                    firebaseDatabase1.child("isBought").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pDialog.cancel();
                            SweetAlertDialog sw=new SweetAlertDialog(Recommendations.this, SweetAlertDialog.SUCCESS_TYPE);
                                    sw.setTitleText("Thank You!");
                                    sw.setContentText("Processing your request!");
                                    sw.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    });
                                    sw.setCancelable(false);
                                    sw.show();


                        }
                    });
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

}
