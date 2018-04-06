package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewentryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner monthSpinner;

    String[] month = new String[]  {"<month>","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String monthSelected;

    @BindView(R.id.input_crop) EditText _cropText;
    @BindView(R.id.input_expiry) EditText _expiryText;
    @BindView(R.id.btn_done) Button _doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
        ButterKnife.bind(this);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        monthSpinner.setOnItemSelectedListener(this);

        ArrayAdapter aa4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,month);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(aa4);

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
        newCropProject newCropProject = new newCropProject(_cropText.getText().toString(),_expiryText.getText().toString(),monthSelected);
        databaseReference.setValue(newCropProject);
    }

    public boolean validate() {
        boolean valid = true;

        String crop = _cropText.getText().toString();
        String expiry = _expiryText.getText().toString();

        if (crop.isEmpty()) {
            _cropText.setError("need to be filled");
            valid = false;
        } else {
            _cropText.setError(null);
        }

        if (expiry.isEmpty() || expiry.length()!=4) {
            _expiryText.setError("appropriate values needed");
            valid = false;
        } else {
            _expiryText.setError(null);
        }
        if(monthSelected == "<month") {
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
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _doneButton.setEnabled(true);
    }
    @Override
    public void onClick(View view) {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            monthSelected = month[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
