package com.example.hauntarl.smartfarming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewQueryActivity extends AppCompatActivity {

    @BindView(R.id.input_subject) EditText _subText;
    @BindView(R.id.input_desc) EditText _descText;
    @BindView(R.id.btn_done) Button _doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query);
        ButterKnife.bind(this);

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

        final ProgressDialog progressDialog = new ProgressDialog(NewQueryActivity.this);
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
        databaseReference=databaseReference.child("7972247921"+new Date());
        AddingNewQuery addingNewQuery = new AddingNewQuery(_descText.getText().toString(),_subText.getText().toString(),"noImagesYet",0,"Please wait while we process your request.");
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
}
