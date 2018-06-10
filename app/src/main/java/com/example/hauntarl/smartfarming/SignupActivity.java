package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG = "SignupActivity";
    private int flag=0;
    private Spinner areaUnitSpinner1, areaUnitSpinner2, areaUnitSpinner3;
    String[] areaUnits = new String[]  {"None","Acre","Bigha","Biswa","Guntha","Hectare","Sq-m","Sq-km"};
    String[] district = new String[]  {"<select>","Ahmednagar","Akola","Amravati","Aurangabaad","Beed","Bhandara","Buldhana","Chandrapur","Dhule","Gadchiroli","Gondia","Hingoli","Jadgaon","Jalna","Kolhapur","Latur","Mumbai City","Mumbai Suburban","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Parbhani","Pune","Raigad","Ratnagiri","Sangli","Satara","Sindhudurg","Solapur","Thane","Wardha","Washim","Yatavmal","Palghar"};

    String areaUnitSelected1, areaUnitSelected2, getAreaUnitSelected3;
    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_phone) EditText _phoneText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_totland) EditText _totland;
    @BindView(R.id.input_culland) EditText _culland;
    @BindView(R.id.input_newpass) EditText _newpass;
    @BindView(R.id.input_conpass) EditText _conpass;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private int cancelled;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        areaUnitSpinner1 = (Spinner) findViewById(R.id.areaSpinner1);
        areaUnitSpinner2 = (Spinner) findViewById(R.id.areaSpinner2);
        areaUnitSpinner3 = (Spinner) findViewById(R.id.areaSpinner3);

        ArrayAdapter aa4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,areaUnits);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaUnitSpinner1.setAdapter(aa4);
        //ArrayAdapter aa5 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,areaUnits);
        //aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaUnitSpinner2.setAdapter(aa4);
        ArrayAdapter aa6 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,district);
        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaUnitSpinner3.setAdapter(aa6);

        areaUnitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaUnitSpinner1.setSelection(i);
                areaUnitSpinner2.setSelection(i);
                areaUnitSelected1=areaUnits[i];
                areaUnitSelected2=areaUnits[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        areaUnitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaUnitSpinner1.setSelection(i);
                areaUnitSpinner2.setSelection(i);
                areaUnitSelected1=areaUnits[i];
                areaUnitSelected2=areaUnits[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        areaUnitSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaUnitSpinner3.setSelection(i);
                getAreaUnitSelected3=district[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cancelled=0;
        if(checkConnection() ==1){

        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }

       _phoneText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, final int count) {
               if(checkConnection() ==1){

               }
               else{
                   startActivity(new Intent(getApplicationContext(), checkInternet.class));
               }
                if(s.length()==10){
                    final SweetAlertDialog pDialog = new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Checking Unique Phone number..");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    new CountDownTimer(5000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            pDialog.cancel();
                            if(cancelled==0) {
                                new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText("The Phone number is unique!")
                                        .show();
                                cancelled=0;
                            }
                            else
                                cancelled=0;

                        }
                    }.start();
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
                    databaseReference3=databaseReference3.child("users");
                    Query phoneQuery = databaseReference3.orderByKey().equalTo(s.toString());
                    phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()==null ){

                            }
                            else{
                                //Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                _phoneText.setText("");
                                pDialog.cancel();
                                SweetAlertDialog cancelSignup=new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        cancelSignup.setTitleText("Oops...");
                                        cancelSignup.setContentText("The Phone Number is already taken, please use a different phone number!");
                                        cancelSignup.setCancelable(false);
                                        cancelSignup.show();

                                cancelled=1;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });



        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        new EulaClass(this).show();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                // Finish the registration screen and return to the Login activity
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


    public void signup() {
        if(checkConnection() ==1){
           if (!validate()) {
                onSignupFailed();
                return;
            }

            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String phone = _phoneText.getText().toString();
            String totland = _totland.getText().toString();
            String culland = _culland.getText().toString();
            String newpass = _newpass.getText().toString();
            String conpass = _conpass.getText().toString();

            // TODO: Implement your own signup logic here.
            databaseReference = databaseReference.child("userInfo");
            databaseReference2 = databaseReference2.child("users");

            SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putInt("logged", 1);
            editor.putString("phone",phone);
            editor.commit();

            newUser n = new newUser(name,phone,email,totland,areaUnitSelected1,culland,areaUnitSelected2,newpass, getAreaUnitSelected3);
            databaseReference.child(phone+" "+new Date()).setValue(n, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getApplicationContext(),"User Added",Toast.LENGTH_SHORT).show();
                }
            });

            newUserForAuth n1 = new newUserForAuth(newpass);
            databaseReference2.child(phone).setValue(n1, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getApplicationContext(),"User Added for Auth",Toast.LENGTH_SHORT).show();
                }
            });
            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference();
            databaseReference4 = databaseReference4.child("masterControl");
            databaseReference4.child(phone).child("value").setValue(1);


            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);

        }
        else{
            startActivity(new Intent(getApplicationContext(), checkInternet.class));
        }


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phoneText.getText().toString();
        String totland = _totland.getText().toString();
        String culland = _culland.getText().toString();
        String newpass = _newpass.getText().toString();
        String conpass = _conpass.getText().toString();

        if (areaUnitSelected1.equals("None")) {
            Snackbar.make(_signupButton, "Please select proper area values!", Snackbar.LENGTH_LONG).show();
            valid = false;
            //return  valid;
        }
        /*if (areaUnitSelected2.equals("None")  ) {
            Snackbar.make(_signupButton, "Please select proper area unit!", Snackbar.LENGTH_LONG).show();
            valid = false;
            //return  valid;
        }*/
        if( totland.isEmpty())
        {
            _totland.setError("enter a value");
            valid=false;
        }

        if( culland.isEmpty() || Integer.valueOf(culland)>=Integer.valueOf(totland))
        {

            _culland.setText(null);
            _culland.setError("invalid value");
            Snackbar.make(_signupButton, "invalid value for cultivated land", Snackbar.LENGTH_LONG).show();
            valid=false;
        }
        if (getAreaUnitSelected3.equals("<select>")) {
            Snackbar.make(_signupButton, "Pls select proper district!", Snackbar.LENGTH_LONG).show();
            valid = false;
        }


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (phone.isEmpty() || phone.length() != 10) {
            _phoneText.setError("enter a valid Phone number");
            valid = false;
        }


        if (newpass.isEmpty() || newpass.length() < 4 || newpass.length() > 10) {
            _newpass.setText(null);
            _newpass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        if (conpass.isEmpty() || !conpass.equals(newpass)) {
            _conpass.setText(null);
            _conpass.setError("password did not match!");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onClick(View view) {
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}