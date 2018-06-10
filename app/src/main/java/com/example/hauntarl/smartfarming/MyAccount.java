package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyAccount extends AppCompatActivity {

    private Button logout;
    private Button done;
    private EditText curp;
    private EditText newp;
    private TextView phone;
    private EditText conp;
    private String phoneNum;
    private int failedCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        logout=findViewById(R.id.logout);
        done=findViewById(R.id.done);
        curp=findViewById(R.id.curp);
        newp=findViewById(R.id.newp);
        conp=findViewById(R.id.conp);
        phone=findViewById(R.id.phone);
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        phoneNum=sharedPref.getString("phone","");
        phone.setText(phoneNum);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection() ==1){
                    final ProgressDialog progressDialog = new ProgressDialog(MyAccount.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Logging out...");
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference = databaseReference.child("masterControl").child(phoneNum).child("value");
                                    databaseReference.setValue(2);
                                    SharedPreferences preferences =getSharedPreferences("mypref",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 2000);
                }
                else{
                    startActivity(new Intent(getApplicationContext(), checkInternet.class));
                }


            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()==1){
                    if (!validate()) {
                        done.setEnabled(true);
                        return;
                    } else {
                        if (checkConnection() == 1) {
                            final SweetAlertDialog pDialog = new SweetAlertDialog(MyAccount.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF0000"));
                            pDialog.setTitleText("Checking..");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            // change password logic
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference = databaseReference.child("users");
                            databaseReference=databaseReference.child(phoneNum).child("password");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pDialog.cancel();
                                    //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                                    if(dataSnapshot.getValue().equals(curp.getText().toString())) {

                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                                        databaseReference1 = databaseReference1.child("users");
                                        databaseReference1.child(phoneNum).child("password").setValue(newp.getText().toString());
                                        Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_SHORT).show();

                                        final SweetAlertDialog pDialog = new SweetAlertDialog(MyAccount.this, SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText("Updating..");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {

                                                        SharedPreferences preferences =getSharedPreferences("mypref",Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.clear();
                                                        editor.commit();
                                                        DatabaseReference masterValue= FirebaseDatabase.getInstance().getReference();
                                                        masterValue=masterValue.child("masterControl");
                                                        masterValue.child(phoneNum).child("value").setValue(2);
                                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                        pDialog.dismiss();
                                                        finish();
                                                    }
                                                }, 2000);

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"You have entered a wrong current password!",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {
                            startActivity(new Intent(getApplicationContext(), checkInternet.class));
                        }

                    }
                }
                else{
                    startActivity(new Intent(getApplicationContext(), checkInternet.class));
                }

            }
        });

    }
    public boolean validate(){
        String newpass=newp.getText().toString();
        String conpass=conp.getText().toString();
        String curpass=curp.getText().toString();
        boolean valid=true;

        if (newpass.isEmpty() || newpass.length() < 4 || newpass.length() > 10) {
            newp.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            newp.setError(null);
        }
        if (conpass.isEmpty() || !conpass.equals(newpass)) {
            conp.setError("password did not match!");
            valid = false;
        } else {
            conp.setError(null);
        }
        return valid;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {

        finish();
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
