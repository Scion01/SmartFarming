package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private int flag=0;
    private DatabaseReference mDatabase;

    @BindView(R.id.input_phone) EditText _phoneText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("users");


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Working or Not?",_phoneText.getText().toString());
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        String phone = _phoneText.getText().toString();
        final String password = _passwordText.getText().toString();
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        else{
            Query phoneQuery = mDatabase.orderByKey().equalTo(phone);
            Log.i("snap2",phoneQuery.toString());
            Log.i("snap","Inside else");

            phoneQuery.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Log.d("dataSnapshot",dataSnapshot.toString());
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                    {
                        Log.i("snap", singleSnapshot.toString());
                        for (DataSnapshot s : singleSnapshot.getChildren())
                        {
                            Log.i("snap1", s.getValue().toString());
                            Log.i("snap1", s.toString());
                            if (s.getValue().equals(password))
                            {
                                Log.i("passwords", singleSnapshot.getValue().toString());
                                Toast.makeText(getApplicationContext(), "Verified!!", Toast.LENGTH_SHORT).show();
                                flag = 1;

                            }
                            else{
                                flag=0;
                                Toast.makeText(getApplicationContext(), "Either Phone or password is wrong!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(getApplicationContext(), DashActivity.class));
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        if(flag == 1) {
            startActivity(new Intent(getApplicationContext(), DashActivity.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Credentials Not Found, try again!!",Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty() || phone.length() != 10) {
            _phoneText.setError("enter a valid Phone number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}