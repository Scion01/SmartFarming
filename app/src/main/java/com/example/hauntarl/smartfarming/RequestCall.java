package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestCall extends AppCompatActivity implements ChildEventListener {

    @BindView(R.id.input_subject)
    EditText _subText;
    @BindView(R.id.input_desc) EditText _descText;
    @BindView(R.id.btn_done)
    Button _doneButton;
    SharedPreferences sharedPref;
    String phone;
    int check;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_call);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference =databaseReference.child("users");
        sharedPref= getSharedPreferences("mypref", 0);
        phone=sharedPref.getString("phone","");
        databaseReference = databaseReference.child(phone);
        databaseReference.addChildEventListener(this);
        _subText.setText("\tCall based query");
        _subText.setEnabled(false);
        _doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection() ==1){

                    done();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), checkInternet.class));
                }
            }
        });
        sharedPref= getSharedPreferences("mypref", 0);
        phone=sharedPref.getString("phone","");
        //Toast.makeText(getApplicationContext(),phone,Toast.LENGTH_SHORT).show();
    }

    public void done()
    {
        if (!validate()) {
            failed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(RequestCall.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding new query...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        success();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("queries");


        databaseReference=databaseReference.child(phone+" "+new Date());
        AddingNewQuery addingNewQuery = new AddingNewQuery(_descText.getText().toString(),_subText.getText().toString(),"noImagesYet",0,"Please wait while we process your request.","None");
        databaseReference.setValue(addingNewQuery);
    }

    public boolean validate() {
        boolean valid = true;

        String sub = _subText.getText().toString();
        String desc = _descText.getText().toString();

        if (sub.isEmpty()) {
            _subText.setError("need to be filled");
            valid = false;
        } else {
            _subText.setError(null);
        }

        if (desc.isEmpty()) {
            _descText.setError("need to be filled");
            valid = false;
        } else {
            _descText.setError(null);
        }

        return valid;
    }
    public void success() {
        _doneButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(getApplicationContext(), QueryActivity.class));
        overridePendingTransition(R.anim.entry_from_right,R.anim.exit_from_right);
        finish();
    }

    public void failed() {
        Toast.makeText(getBaseContext(), "Please check the fields entered!", Toast.LENGTH_LONG).show();

        _doneButton.setEnabled(true);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if(dataSnapshot.getKey().equals("accountIsValid")){
            if (dataSnapshot.getValue().equals(0)){
                Toast.makeText(getApplicationContext(),"For some reasons your account has been deactivated! Please contact Customer Care",Toast.LENGTH_LONG).show();
                finish();
            }
            else
                return;
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