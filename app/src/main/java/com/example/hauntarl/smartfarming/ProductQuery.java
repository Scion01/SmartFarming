package com.example.hauntarl.smartfarming;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductQuery extends AppCompatActivity implements ChildEventListener {
    private Spinner budgetSpin;
    String[] budget = new String[]  {"in Rs.","<= 2000","<= 4000","<= 6000","<=8000","<=10000","> 10000"};
    String budgetSelected, _descText, rating;
    @BindView(R.id.input_subject) EditText _subText;
    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_quantity) EditText _quanText;
    @BindView(R.id.btn_done) Button _doneButton;

    SharedPreferences sharedPref;
    String phone;
    int check;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_query);
        ButterKnife.bind(this);

        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.urgency);


        budgetSpin = (Spinner) findViewById(R.id.BudgetSpinner);
        ArrayAdapter aa4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,budget);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpin.setAdapter(aa4);
        budgetSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                budgetSpin.setSelection(i);
                budgetSelected=budget[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference =databaseReference.child("users");
        sharedPref= getSharedPreferences("mypref", 0);
        phone=sharedPref.getString("phone","");
        databaseReference = databaseReference.child(phone);
        databaseReference.addChildEventListener(this);
        _subText.setText("Product Enquiry");
        _subText.setEnabled(false);
        _doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection() ==1){
                    float ratingValue = simpleRatingBar.getRating();
                    int ratingNumber= (int)ratingValue;
                    switch(ratingNumber)
                    {
                        case 0 : rating="casual enquiry for ";
                            break;
                        case 1 : rating="not so urgent need for ";
                            break;
                        case 2 : rating="acute need for ";
                            break;
                        case 3 : rating="serious need for ";
                            break;
                        case 4 : rating="crucial need for ";
                            break;
                        case 5 : rating="dire need for ";
                            break;
                    }
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

        _descText="Product based enquiry with " + rating + _nameText.getText().toString() + " in " + _quanText.getText().toString() + " quantity and can spend " + budgetSelected + "rupees";
        final ProgressDialog progressDialog = new ProgressDialog(ProductQuery.this);
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
        AddingNewQuery addingNewQuery = new AddingNewQuery(_descText,_subText.getText().toString(),"noImagesYet",0,"Please wait while we process your request.","None");
        databaseReference.setValue(addingNewQuery);
        Intent resultIntent = new Intent(ProductQuery.this,QueryActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivity(ProductQuery.this,(int) Calendar.getInstance().getTimeInMillis(),resultIntent,0);
        NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("The following query was placed successfully!");
        inboxStyle.addLine("Query Subject: "+_subText.getText().toString());
        inboxStyle.addLine("Query Description: "+_descText.toString());
        inboxStyle.addLine("Please wait while we respond to your query.");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(ProductQuery.this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Query Placed Successfully!")
                .setContentText("The following query was placed successfully!")
                .setStyle(inboxStyle)
                .addAction(R.mipmap.ic_launcher,"View your query!",piResult)
                .setVibrate(new long[] { 1000, 1000 })
                .setSound(alarmSound);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
    }

    public boolean validate() {
        boolean valid = true;

        String sub = _subText.getText().toString();
        String name = _nameText.getText().toString();
        String quantity = _quanText.getText().toString();
        String budget = budgetSelected;

        if (sub.isEmpty()) {
            _subText.setError("cannot be empty!");
            valid = false;
        } else {
            _subText.setError(null);
        }
        if (name.isEmpty()) {
            _nameText.setError("cannot be empty!");
            valid = false;
        } else {
            _nameText.setError(null);
        }        if (quantity.isEmpty()) {
            _quanText.setError("cannot be empty!");
            valid = false;
        } else {
            _quanText.setError(null);
        }
        if(budget.equals("in Rs."))
        {
            Snackbar.make(_doneButton, "Please select proper Budget!", Snackbar.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }
    public void success() {
        _doneButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "You may check the progress of your query in My Queries tab!", Toast.LENGTH_LONG).show();
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