package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MyPlan extends AppCompatActivity implements /*ConnectivityReceiver.ConnectivityReceiverListener, */ View.OnClickListener{

    private CheckBox pomeCheck;
    private CheckBox grapesCheck;
    private CheckBox tomatoCheck;
    private CheckBox chiliCheck;
    private CheckBox brinjalCheck;
    private CheckBox generalCheck;
    Handler handler = new Handler();

    private Button checkoutButton;
    private int pomeFlag = 0, grapesFlag = 0, tomatoFlag = 0, chiliFlag = 0, brinjalFlag = 0, generalFlag = 0;


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
        checkoutButton = findViewById(R.id.checkoutButton);

        checkConnection();


        pomeCheck.setOnClickListener(this);
        grapesCheck.setOnClickListener(this);
        tomatoCheck.setOnClickListener(this);
        chiliCheck.setOnClickListener(this);
        brinjalCheck.setOnClickListener(this);
        generalCheck.setOnClickListener(this);
        checkoutButton.setOnClickListener(this);
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
        } else if (v == pomeCheck && pomeFlag == 1) {
            pomeCheck.setChecked(false);
            pomeFlag = 0;
            if (grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0)
                checkoutButton.setEnabled(false);
        }

        if (v == grapesCheck && grapesFlag == 0) {
            grapesCheck.setChecked(true);
            grapesFlag = 1;
            checkoutButton.setEnabled(true);
        } else if (v == grapesCheck && grapesFlag == 1) {
            grapesCheck.setChecked(false);
            grapesFlag = 0;
            if (pomeFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0)
                checkoutButton.setEnabled(false);
        }

        if (v == tomatoCheck && tomatoFlag == 0) {
            tomatoCheck.setChecked(true);
            tomatoFlag = 1;
            checkoutButton.setEnabled(true);
        } else if (v == tomatoCheck && tomatoFlag == 1) {
            tomatoCheck.setChecked(false);
            tomatoFlag = 0;
            if (pomeFlag == 0 && grapesFlag == 0 && chiliFlag == 0 && brinjalFlag == 0 && generalFlag == 0)
                checkoutButton.setEnabled(false);
        }

        if (v == chiliCheck && chiliFlag == 0) {
            chiliCheck.setChecked(true);
            chiliFlag = 1;
            checkoutButton.setEnabled(true);
        } else if (v == chiliCheck && chiliFlag == 1) {
            chiliCheck.setChecked(false);
            chiliFlag = 0;
            if (pomeFlag == 0 && grapesFlag == 0 && tomatoFlag == 0 && brinjalFlag == 0 && generalFlag == 0)
                checkoutButton.setEnabled(false);
        }

        if (v == brinjalCheck && brinjalFlag == 0) {
            brinjalCheck.setChecked(true);
            brinjalFlag = 1;
            checkoutButton.setEnabled(true);
        } else if (v == brinjalCheck && brinjalFlag == 1) {
            brinjalCheck.setChecked(false);
            brinjalFlag = 0;
            if (pomeFlag == 0 && grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && generalFlag == 0)
                checkoutButton.setEnabled(false);
        }

        if (v == generalCheck && generalFlag == 0) {
            generalCheck.setChecked(true);
            generalFlag = 1;
            checkoutButton.setEnabled(true);
        } else if (v == generalCheck && generalFlag == 1) {
            generalCheck.setChecked(false);
            generalFlag = 0;
            if (pomeFlag == 0 && grapesFlag == 0 && tomatoFlag == 0 && chiliFlag == 0 && brinjalFlag == 0)
                checkoutButton.setEnabled(false);
        }
        if (v == checkoutButton) {
            Toast.makeText(getApplicationContext(),"Coming Soon..",Toast.LENGTH_SHORT).show();
            /*if(checkConnection() ==1){

            startActivity(new Intent(getApplicationContext(), gateway.class));
            finish();
            }
            else{
                startActivity(new Intent(getApplicationContext(), checkInternet.class));
            }*/
        }

    }


}

