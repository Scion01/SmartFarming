package com.example.hauntarl.smartfarming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class DashActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public GridView gridView;
    int images[] =
            {
                    //R.drawable.crop,
                    //R.drawable.active,
                    //R.drawable.plan,
                    //R.drawable.redeem,
                    R.drawable.chat,
                    R.drawable.plan,
                    R.drawable.recom,
                    R.drawable.product,
                    R.drawable.call,
                    R.drawable.my_account,
                    //R.drawable.sharefarm,
                    R.drawable.about_us

            };
    
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
       pay.put("send_sms", true);
      pay.put("send_email", true);
 } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }
    
    InstapayListener listener;

    
    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        // Call the function callInstamojo to start payment here

        if(checkConnection() ==1){


        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading..");
        pDialog.setCancelable(false);
        pDialog.show();

        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        String phone=sharedPref.getString("phone","");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("masterControl").child(phone).child("value");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals(Integer.toString(0))){
                    Toast.makeText(getApplicationContext(),"Your account has been deactivated, please get help from Customer Care!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Master Control test passed ",Toast.LENGTH_SHORT).show();
                    pDialog.cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = databaseReference1.child("Subscriptions");
        databaseReference1 = databaseReference1.child(phone);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().toString().equals("isActive")){
                    if(dataSnapshot.getValue().toString().equals(0)){
                        startActivity(new Intent(getApplicationContext(),MyPlan.class));
                        overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Subscription Active!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        gridView = findViewById(R.id.gridView1);

        GridActivity gridAdapter = new GridActivity(getApplicationContext(),images);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //position would tell u everything
        switch(position){
            case 0:
                //Toast.makeText(getApplicationContext(),"entry selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),QueryActivity.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
            case 1:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyPlan.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;

            case 2:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Recommendations.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
            case 3:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),ProductQuery.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);

                return ;
            case 4:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),RequestCall.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);

                return ;
            case 5:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyAccount.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
            case 6:
                //Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AboutUs.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
        }
    }
}
