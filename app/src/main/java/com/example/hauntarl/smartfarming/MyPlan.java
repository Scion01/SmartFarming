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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class MyPlan extends AppCompatActivity implements /*ConnectivityReceiver.ConnectivityReceiverListener, */ View.OnClickListener{

    private CheckBox pomeCheck;
    private CheckBox grapesCheck;
    private CheckBox tomatoCheck;
    private CheckBox chiliCheck;
    private CheckBox brinjalCheck;
    private CheckBox okraCheck;
    private CheckBox papyaCheck;
    private CheckBox bottleCheck;
    private CheckBox mangoCheck;
    private CheckBox generalCheck;
    Handler handler = new Handler();

    private Button checkoutButton;
    private int pomeFlag = 0, grapesFlag = 0, tomatoFlag = 0, chiliFlag = 0, brinjalFlag = 0, generalFlag = 0, okraFlag=0, papayaFlag=0, bottleFlag=0, mangoFlag=0;
    private String phone;
    private String email;
    private String nameOfCustomer;
    private SweetAlertDialog userDataDialog;
    private ArrayList<String> selectedPlans = new ArrayList<String>();
    private String finalAmount;


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
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(new ConnectivityReceiver(),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }*/
        setContentView(R.layout.activity_my_plan);
        pomeCheck = findViewById(R.id.pomeCheck);
        grapesCheck = findViewById(R.id.grapesCheck);
        tomatoCheck = findViewById(R.id.tomatoCheck);
        chiliCheck = findViewById(R.id.chiliCheck);
        brinjalCheck = findViewById(R.id.brinjalCheck);
        generalCheck = findViewById(R.id.generalCheck);
        okraCheck = findViewById(R.id.OkraCheck);
        bottleCheck = findViewById(R.id.bottleCheck);
        papyaCheck  = findViewById(R.id.papayaCheck);
        mangoCheck = findViewById(R.id.mangoCheck);

        checkoutButton = findViewById(R.id.checkoutButton);

        checkConnection();


        pomeCheck.setOnClickListener(this);
        grapesCheck.setOnClickListener(this);
        tomatoCheck.setOnClickListener(this);
        chiliCheck.setOnClickListener(this);
        brinjalCheck.setOnClickListener(this);
        generalCheck.setOnClickListener(this);
        checkoutButton.setOnClickListener(this);
        mangoCheck.setOnClickListener(this);
        bottleCheck.setOnClickListener(this);
        papyaCheck.setOnClickListener(this);
        okraCheck.setOnClickListener(this);

        userDataDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        userDataDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        userDataDialog.setTitleText("Loading Plans..");
        userDataDialog.setCancelable(false);
        userDataDialog.show();

        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        phone=sharedPref.getString("phone","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("userInfo");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String[] splitsOfKeys = dataSnapshot.getKey().toString().split(" ");
                if(splitsOfKeys[0].equals(phone)){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().toString().equals("Name")){
                            nameOfCustomer = ds.getValue().toString();
                        }
                        if(ds.getKey().toString().equals("Email")){
                            email = ds.getValue().toString();
                            userDataDialog.cancel();
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
                Toast.makeText(getApplicationContext(),"Database error, please contact Customer Support...",Toast.LENGTH_LONG).show();
                finish();
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
        if (v == pomeCheck && pomeFlag == 0) {
            pomeCheck.setChecked(true);
            pomeFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Pomegranate Plan&&7 months&&1500");
            pendingListViewAddition();
        } else if (v == pomeCheck && pomeFlag == 1) {
            pomeCheck.setChecked(false);
            pomeFlag = 0;
            selectedPlans.remove("Pomegranate Plan&&7 months&&1500");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == okraCheck && okraFlag == 0) {
            okraCheck.setChecked(true);
            okraFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Okra Plan&&4 months&&1000");
            pendingListViewAddition();
        } else if (v == okraCheck && okraFlag == 1) {
            okraCheck.setChecked(false);
            okraFlag = 0;
            selectedPlans.remove("Okra Plan&&4 months&&1000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == mangoCheck && mangoFlag == 0) {
            mangoCheck.setChecked(true);
            mangoFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Mango Plan&&4 months&&1000");
            pendingListViewAddition();
        } else if (v == mangoCheck && mangoFlag == 1) {
            mangoCheck.setChecked(false);
            mangoFlag = 0;
            selectedPlans.remove("Mango Plan&&4 months&&1000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == bottleCheck && bottleFlag == 0) {
            bottleCheck.setChecked(true);
            bottleFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Bottle Gourd Plan&&4 months&&700");
            pendingListViewAddition();
        } else if (v == bottleCheck && bottleFlag == 1) {
            bottleCheck.setChecked(false);
            bottleFlag = 0;
            selectedPlans.remove("Bottle Gourd Plan&&4 months&&700");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == papyaCheck && papayaFlag == 0) {
            papyaCheck.setChecked(true);
            papayaFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Papaya Plan&&12 months&&2000");
            pendingListViewAddition();
        } else if (v == okraCheck && okraFlag == 1) {
            papyaCheck.setChecked(false);
            papayaFlag = 0;
            selectedPlans.remove("Papaya Plan&&12 months&&2000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == grapesCheck && grapesFlag == 0) {
            grapesCheck.setChecked(true);
            grapesFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Grapes Plan&&6 months&&2000");
            pendingListViewAddition();
        } else if (v == grapesCheck && grapesFlag == 1) {
            grapesCheck.setChecked(false);
            grapesFlag = 0;
            selectedPlans.remove("Grapes Plan&&6 months&&2000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }

        if (v == tomatoCheck && tomatoFlag == 0) {
            tomatoCheck.setChecked(true);
            tomatoFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Tomato Plan&&4 months&&1000");
            pendingListViewAddition();
        } else if (v == tomatoCheck && tomatoFlag == 1) {
            tomatoCheck.setChecked(false);
            tomatoFlag = 0;
            selectedPlans.remove("Tomato Plan&&4 months&&1000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }

        if (v == chiliCheck && chiliFlag == 0) {
            chiliCheck.setChecked(true);
            chiliFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Chilli Plan&&6 months&&700");
            pendingListViewAddition();
        } else if (v == chiliCheck && chiliFlag == 1) {
            chiliCheck.setChecked(false);
            chiliFlag = 0;
            selectedPlans.remove("Chilli Plan&&6 months&&700");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }

        if (v == brinjalCheck && brinjalFlag == 0) {
            brinjalCheck.setChecked(true);
            brinjalFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("Brinjal Plan&&7 months&&1000");
            pendingListViewAddition();
        } else if (v == brinjalCheck && brinjalFlag == 1) {
            brinjalCheck.setChecked(false);
            brinjalFlag = 0;
            selectedPlans.remove("Brinjal Plan&&7 months&&1000");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }

        if (v == generalCheck && generalFlag == 0) {
            generalCheck.setChecked(true);
            generalFlag = 1;
            checkoutButton.setEnabled(true);
            selectedPlans.add("General Plan&&1 months&&200");
            pendingListViewAddition();
        } else if (v == generalCheck && generalFlag == 1) {
            generalCheck.setChecked(false);
            generalFlag = 0;
            selectedPlans.remove("General Plan&&1 months&&200");
            pendingListViewAddition();
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0 && bottleFlag==0 && mangoFlag==0 && okraFlag ==0 && papayaFlag==0 )
                checkoutButton.setEnabled(false);
        }
        if (v == checkoutButton) {
            //Toast.makeText(getApplicationContext(),"Coming Soon..",Toast.LENGTH_SHORT).show();
            if(checkConnection() ==1){
              final SweetAlertDialog payAlert=  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
              payAlert.setTitleText("Are you sure?");
              payAlert.setContentText("Pay Rs "+finalAmount+"!");
              payAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        callInstamojoPay(email,phone,finalAmount,"Purchasing plans",nameOfCustomer);
                    }
              });
              payAlert.setConfirmText("Yes, I am sure.");
              payAlert.show();

            }
            else{
                startActivity(new Intent(getApplicationContext(), checkInternet.class));
            }
        }

    }

    private void pendingListViewAddition() {
        int i=0;
        finalAmount = "0";
        String [] plans =  new String[3];
        while(i<selectedPlans.size()){
            plans = selectedPlans.get(i).split("&&");
            finalAmount = String.valueOf(Integer.parseInt(finalAmount)+ Integer.parseInt(plans[2]));
            i++;
        }
        if(!finalAmount.equals("0")){
            checkoutButton.setText("Proceed to pay "+finalAmount);
        }
        else
            checkoutButton.setText("Select a plan");
    }


}

