package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewentryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner monthSpinner, monthSpinner1, daySpinner, waterSource;

    String[] water = new String[]  {"other", "Canal", "Well", "Municipal", "Ponds"};
    String[] month = new String[]  {"month","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] day = new String[] {"day","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String monthSelected, waterSelected, monthSelected1, daySelected;

    @BindView(R.id.input_crop) EditText _cropText;
    @BindView(R.id.input_landArea) EditText _landArea;
    @BindView(R.id.input_soilType) EditText _soilType;
    @BindView(R.id.input_start) EditText _startText;
    @BindView(R.id.input_expiry) EditText _expiryText;
    @BindView(R.id.btn_done) Button _doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
        ButterKnife.bind(this);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        monthSpinner.setOnItemSelectedListener(this);
        monthSpinner1 = (Spinner) findViewById(R.id.monthSpinner1);
        monthSpinner1.setOnItemSelectedListener(this);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        daySpinner.setOnItemSelectedListener(this);
        waterSource = (Spinner) findViewById(R.id.waterSpinner);
        waterSource.setOnItemSelectedListener(this);

        ArrayAdapter aa4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,month);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(aa4);
        ArrayAdapter aa5 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,month);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner1.setAdapter(aa5);
        ArrayAdapter aa6 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,day);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(aa6);
        ArrayAdapter aa7 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,water);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSource.setAdapter(aa7);

        _doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    public void done()
    {
        if (!validate()) {
            failed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(NewentryActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding new crop...");
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
        databaseReference = databaseReference.child("activeProjects");
        databaseReference=databaseReference.child("7972247921");
        databaseReference=databaseReference.child(new Date().toString());
        newCropProject newCropProject = new newCropProject(_cropText.getText().toString(),_expiryText.getText().toString(),monthSelected);
        databaseReference.setValue(newCropProject);
    }

    public boolean validate() {
        boolean valid = true;

        String crop = _cropText.getText().toString();
        String land = _landArea.getText().toString();
        String soil = _soilType.getText().toString();
        String start= _startText.getText().toString();
        String expiry = _expiryText.getText().toString();

        if (crop.isEmpty()) {
            _cropText.setError("needs to be filled");
            valid = false;
        } else {
            _cropText.setError(null);
        }
        if (land.isEmpty()) {
            _landArea.setError("needs to be filled");
            valid = false;
        } else {
            _landArea.setError(null);
        }
        if (soil.isEmpty()) {
            _soilType.setError("needs to be filled");
            valid = false;
        } else {
            _soilType.setError(null);
        }

        if (start.isEmpty() || start.length()!=4) {
            _startText.setError("appropriate values needed");
            valid = false;
        } else {
            _startText.setError(null);
        }
        if (expiry.isEmpty() || expiry.length()!=4) {
            _expiryText.setError("appropriate values needed");
            valid = false;
        } else {
            _expiryText.setError(null);
        }
        if(monthSelected.equals("month")) {
            Snackbar.make(_doneButton, "Pls select proper month!", Snackbar.LENGTH_LONG).show();
            valid = false;
        }
        if(daySelected.equals("day")) {
            Snackbar.make(_doneButton, "Pls select proper day!", Snackbar.LENGTH_LONG).show();
            valid = false;
        }
        if(monthSelected1.equals("month")) {
            Snackbar.make(_doneButton, "Pls select proper month!", Snackbar.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }
    public void success() {
        _doneButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(getApplicationContext(), EntryActivity.class));
        overridePendingTransition(R.anim.entry_from_right,R.anim.exit_from_right);
        finish();
    }

    public void failed() {
        Toast.makeText(getBaseContext(), "Entry failed", Toast.LENGTH_LONG).show();

        _doneButton.setEnabled(true);
    }
    @Override
    public void onClick(View view) {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            if (parent==monthSpinner)
                monthSelected = month[position];
            else if(parent==monthSpinner1)
                monthSelected1 = month[position];
            else if(parent==daySpinner)
                daySelected = day[position];
            else if(parent==waterSource)
                waterSelected = water[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
