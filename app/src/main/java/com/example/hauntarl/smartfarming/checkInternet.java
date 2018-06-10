package com.example.hauntarl.smartfarming;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class checkInternet extends AppCompatActivity implements View.OnClickListener {
    private Button checkInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);
        checkInternet= findViewById(R.id.checkForConnection);
        checkInternet.setOnClickListener(this);
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
    public void onBackPressed() {
        //super.onBackPressed();
// dont call **super**, if u want disable back button in current screen.
    }

    @Override
    public void onClick(View v) {
        if(v== checkInternet){
            if(checkConnection() ==1){
                Toast.makeText(getApplicationContext(),"You are back Online",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"No Internet Access",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
