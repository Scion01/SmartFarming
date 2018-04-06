package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG = "SignupActivity";
    private Spinner areaUnitSpinner1, areaUnitSpinner2;
    String[] areaUnits = new String[]  {"None","Acre","Bigha","Biswa","Guntha","Hectare","Sq-m","Sq-km"};

    String areaUnitSelected1, areaUnitSelected2;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        areaUnitSpinner1 = (Spinner) findViewById(R.id.areaSpinner1);
        areaUnitSpinner2 = (Spinner) findViewById(R.id.areaSpinner2);
        areaUnitSpinner1.setOnItemSelectedListener(this);
        areaUnitSpinner2.setOnItemSelectedListener(this);

        ArrayAdapter aa4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,areaUnits);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaUnitSpinner1.setAdapter(aa4);
        ArrayAdapter aa5 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,areaUnits);
        aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaUnitSpinner2.setAdapter(aa5);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();


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

    public void signup() {
        Log.d(TAG, "Signup");

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
        databaseReference = databaseReference.child("userInfo");
        databaseReference2 = databaseReference2.child("users");

        newUser n = new newUser(name,email,phone,totland,areaUnitSelected1,culland,areaUnitSelected2,newpass);
        databaseReference.child(phone+new Date()).setValue(n);

        newUserForAuth n1 = new newUserForAuth(newpass);
        databaseReference2.child(phone).setValue(n1);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(getApplicationContext(), DashActivity.class));
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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

        if (areaUnitSelected1 == "None" || _totland.getText().toString().isEmpty()) {
            Snackbar.make(_signupButton, "Pls select proper area values!", Snackbar.LENGTH_LONG).show();
            valid = false;
            return  valid;
        }
        if (areaUnitSelected2 == "None" && ! _culland.getText().toString().isEmpty() ) {
            Snackbar.make(_signupButton, "Pls select proper cultivated area unit!", Snackbar.LENGTH_LONG).show();
            valid = false;
            return  valid;
        }
        if ( _culland.getText().toString().isEmpty() ) {
            _culland.setText("0");
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
        } else {
            _phoneText.setError(null);
        }
        if (totland.isEmpty()) {
            _totland.setError("field is empty");
            valid= false;
        }
        else
            _totland.setError(null);
        if (culland.isEmpty() || culland.length()>totland.length()) {
            _culland.setError("field is empty");
            valid= false;
        }
        else
            _culland.setError(null);

        if (newpass.isEmpty() || newpass.length() < 4 || newpass.length() > 10) {
            _newpass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _newpass.setError(null);
        }
        if (conpass.isEmpty() || !conpass.equals(newpass)) {
            _conpass.setError("password did not match!");
            valid = false;
        } else {
            _conpass.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {





    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        if(parent ==areaUnitSpinner1)
            areaUnitSelected1 = areaUnits[position];
        if(parent ==areaUnitSpinner2)
            areaUnitSelected2 = areaUnits[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}