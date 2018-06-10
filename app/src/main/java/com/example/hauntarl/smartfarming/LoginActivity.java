package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private int flag=0;
    private DatabaseReference mDatabase;
    private String sentEmail;
    private String sentPassword;
    private int check;
    private int userAlreadySignedIn=0;
    ClientProtocolException e0;
    IOException e1;
    HttpResponse response;

    @BindView(R.id.input_phone) EditText _phoneText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_forgotPassword) TextView _forgotLink;
    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String postReceiverUrl = "http://smartfarming.payasmedia.com/send_mail.php";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(postReceiverUrl);
            try {
                // Add your data
                Log.d("Inside Aysnc","true");
                Log.d("Email",sentEmail);
                Log.d("Pwd",sentPassword);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("password", sentPassword));
                nameValuePairs.add(new BasicNameValuePair("email", sentEmail));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                Log.d("Response",response.toString());

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e0=e;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e1=e;
            }
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("users");

        check=1;

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

        _forgotLink.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if (checkConnection() == 1) {
                                                   if (_phoneText.getText().toString().length() == 10) {
                                                       try {
                                                           check=1;
                                                           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                           databaseReference = databaseReference.child("users").child(_phoneText.getText().toString());
                                                           databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   if (dataSnapshot.getValue() == null) {
                                                                       //Toast.makeText(getApplicationContext(),"Not Found",Toast.LENGTH_SHORT).show();
                                                                       SweetAlertDialog sweetAlertDialog3= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                                       sweetAlertDialog3.setTitleText("Oops...");
                                                                       sweetAlertDialog3.setContentText("Please check the entered phone number.");
                                                                       sweetAlertDialog3.setCancelable(false);
                                                                       sweetAlertDialog3.show();
                                                                   } else {
                                                                       //Toast.makeText(getApplicationContext(),"Found password",Toast.LENGTH_SHORT).show();
                                                                       for(DataSnapshot ds1: dataSnapshot.getChildren()) {
                                                                           //Toast.makeText(getApplicationContext(), ds1.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                                           sentPassword = ds1.getValue().toString();
                                                                       }
                                                                       DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
                                                                       databaseReference2 = databaseReference2.child("userInfo");
                                                                       databaseReference2.addChildEventListener(new ChildEventListener() {
                                                                           @Override
                                                                           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                                               for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                                                   //Toast.makeText(getApplicationContext(),ds.toString(),Toast.LENGTH_SHORT).show();
                                                                                   if(ds.getKey().toString().equals("Email")){
                                                                                       if(check==1)
                                                                                       sentEmail=ds.getValue().toString();
                                                                                   }
                                                                                   else if(ds.getKey().toString().equals("Phone")){
                                                                                       if(ds.getValue().toString().equals(_phoneText.getText().toString())) {
                                                                                           check=0;
                                                                                           //Toast.makeText(getApplicationContext(),sentEmail+sentPassword,Toast.LENGTH_LONG).show();
                                                                                           SendPostReqAsyncTask sendPostReqAsyncTask= new SendPostReqAsyncTask();
                                                                                           sendPostReqAsyncTask.execute("GO");
                                                                                           //Toast.makeText(getApplicationContext(),"Sending..",Toast.LENGTH_SHORT).show();
                                                                                           SweetAlertDialog sweetAlertDialog22=new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                                                   sweetAlertDialog22.setTitleText("Password sent!");
                                                                                                   sweetAlertDialog22.setContentText("Please check your inbox and spam as well!");
                                                                                                   sweetAlertDialog22.setCancelable(false);
                                                                                                   sweetAlertDialog22.show();
                                                                                           return;
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
                                                                       return;
                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(DatabaseError databaseError) {

                                                               }
                                                           });
                                                       }
                                                       catch (Exception e){
                                                           SweetAlertDialog sweetAlertDialog2= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                           sweetAlertDialog2.setTitleText("Oops...");
                                                           sweetAlertDialog2.setContentText("Please check the entered phone number!");
                                                           sweetAlertDialog2.setCancelable(false);
                                                           sweetAlertDialog2.show();

                                                       }

                                                   } else {
                                                       SweetAlertDialog sweetAlertDialog1= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                       sweetAlertDialog1.setTitleText("Oops...");
                                                       sweetAlertDialog1.setContentText("Please enter the phone number!");
                                                       sweetAlertDialog1.setCancelable(false);
                                                       sweetAlertDialog1.show();

                                                   }
                                               }
                                               else{
                                                   startActivity(new Intent(getApplicationContext(), checkInternet.class));
                                               }
                                           }
                                       }
        );
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

    public void login() {
        final String phone = _phoneText.getText().toString();
        final String password = _passwordText.getText().toString();
        Log.d(TAG, "Login");
        if(checkConnection() ==1){

            if (!validate()) {
                onLoginFailed();
                return;
            }
            else{
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference = databaseReference.child("masterControl").child(phone).child("value");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equals(Integer.toString(0))){
                            Toast.makeText(getApplicationContext(),"Your account has been deactivated, please get help from Customer Care!",Toast.LENGTH_LONG).show();
                        }
                        else if(dataSnapshot.getValue().toString().equals(Integer.toString(1))){
                            userAlreadySignedIn=1;
                            Toast.makeText(getApplicationContext(),"The account is already active! Please sign out from other devices first",Toast.LENGTH_LONG).show();
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
                                                SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
                                                SharedPreferences.Editor editor= sharedPref.edit();
                                                editor.putInt("logged", 1);
                                                editor.putString("phone",phone);
                                                editor.commit();
                                                flag = 1;
                                                new android.os.Handler().postDelayed(
                                                        new Runnable() {
                                                            public void run() {
                                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                                databaseReference = databaseReference.child("masterControl").child(phone).child("value");
                                                                databaseReference.setValue(1);
                                                            }
                                                        }, 2000);

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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
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
        else if(userAlreadySignedIn==1){

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