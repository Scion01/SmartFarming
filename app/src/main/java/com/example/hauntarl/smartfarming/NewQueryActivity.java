package com.example.hauntarl.smartfarming;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewQueryActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.input_subject) EditText _subText;
    @BindView(R.id.input_desc) EditText _descText;
    @BindView(R.id.btn_done) Button _doneButton;
    @BindView(R.id.btn_camera) Button _camera;
    @BindView(R.id.btn_gallery) ImageButton  _gallery;
    @BindView(R.id.imageToBeUploaded)
    ImageView imageView;
    SharedPreferences sharedPref;
    String phone;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    private Uri uri;
    private Integer permissionChecked=0;
    private Bitmap bitmap;
    private String photoPath;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private StorageReference mStorageRef;
    private int checkForImages=0;
    private Button cancelImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query);
        ButterKnife.bind(this);


        cancelImage = findViewById(R.id.btn_cancel_image);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        _camera.setOnClickListener(this);
        _gallery.setOnClickListener(this);
        cancelImage.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        checkAndroidVersion();

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
    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        else
            permissionChecked=1;
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA)
                +ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

               /* Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload photos",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{android.Manifest.permission
                                                    .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }
                            }
                        }).show();*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{android.Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{android.Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        }
        else
            permissionChecked=1;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && writeExternalFile && fineLocation)
                    {
                        Toast.makeText(this,"All the required permissions are granted!!",Toast.LENGTH_SHORT).show();
                        permissionChecked=1;
                    } else {
                        /*Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload photos",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{android.Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA,android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                    PERMISSIONS_MULTIPLE_REQUEST);
                                        }
                                    }
                                }).show();*/

                    }
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                imageView.setVisibility(View.VISIBLE);
                photoPath  = cameraPhoto.getPhotoPath();
                Log.d("photoPath",photoPath);
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    imageView.setImageBitmap(bitmap);
                    checkForImages=1;
                    cancelImage.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }


            }
            else if(requestCode == GALLERY_REQUEST){
                uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                photoPath = galleryPhoto.getPath();
                Log.d("photoPath",photoPath);
                imageView.setVisibility(View.VISIBLE);
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    imageView.setImageBitmap(bitmap);
                    checkForImages=1;
                    cancelImage.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
    public void done()
    {
        if (!validate()) {
            failed();
            return;
        }

        new SweetAlertDialog(NewQueryActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you?")
                .setContentText("Have you checked the contents of the query?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        final SweetAlertDialog pDialog = new SweetAlertDialog(NewQueryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Uploading");
                        pDialog.setCancelable(false);
                        pDialog.show();


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference = databaseReference.child("queries");
                        String uploadedFileName= "noImagesYet";
                        if(checkForImages ==1) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            uploadedFileName = phone + " " + new Date();
                            byte[] dataBAOS = baos.toByteArray();
                            StorageReference imagesRef = mStorageRef.child(uploadedFileName);
                            final UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Uploading Failed!!"+e.toString(), Toast.LENGTH_SHORT).show();
                                    pDialog.cancel();
                                    SweetAlertDialog afterUpload= new SweetAlertDialog(NewQueryActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            afterUpload.setTitleText("Oops...");
                                            afterUpload.setContentText("Something went wrong, Please try again!");
                                            afterUpload.show();

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    pDialog.cancel();
                                    SweetAlertDialog afterUpload = new SweetAlertDialog(NewQueryActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            afterUpload.setTitleText("Good job!");
                                            afterUpload.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.cancel();
                                                    success();
                                                }
                                            });
                                            afterUpload.setContentText("The Query was placed successfully!!");
                                            afterUpload.setCancelable(false);
                                            afterUpload.show();
                                }
                            });
                        }
                        databaseReference=databaseReference.child(phone+" "+new Date());
                        AddingNewQuery addingNewQuery = new AddingNewQuery(_descText.getText().toString(),_subText.getText().toString(),uploadedFileName,0,"Please wait while we process your request.","None");
                        databaseReference.setValue(addingNewQuery, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(NewQueryActivity.this,"Query Added",Toast.LENGTH_SHORT).show();
                                if(checkForImages!=1){
                                    pDialog.cancel();
                                    Intent resultIntent = new Intent(NewQueryActivity.this,QueryActivity.class);
                                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    PendingIntent piResult = PendingIntent.getActivity(NewQueryActivity.this,(int) Calendar.getInstance().getTimeInMillis(),resultIntent,0);
                                    NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
                                    inboxStyle.setBigContentTitle("The following query was placed successfully!");
                                    inboxStyle.addLine("Query Subject: "+_subText.getText().toString());
                                    inboxStyle.addLine("Query Description: "+_descText.getText().toString());
                                    inboxStyle.addLine("Please wait while we respond to your query.");
                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                    NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(NewQueryActivity.this)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Query Placed Successfully!")
                                            .setContentText("The following query was placed successfully!")
                                            .setStyle(inboxStyle)
                                            .addAction(R.mipmap.ic_launcher,"View your query!",piResult)
                                            .setVibrate(new long[] { 1000, 1000 })
                                            .setSound(alarmSound);;

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0,mBuilder.build());

                                    SweetAlertDialog queryUploaded= new SweetAlertDialog(NewQueryActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            queryUploaded.setTitleText("Good job!");
                                            queryUploaded.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.cancel();
                                                    success();
                                                }
                                            });
                                            queryUploaded.setContentText("The Query was placed successfully!!");
                                            queryUploaded.setCancelable(false);
                                            queryUploaded.show();
                                }
                            }
                        });

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        return;
                    }
                })
                .show();


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
        finish();

    }

    public void failed() {
        Toast.makeText(getBaseContext(), "Please check the fields entered!", Toast.LENGTH_LONG).show();

        _doneButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v == _camera){

            try {
                startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                cameraPhoto.addToGallery();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }


        }
        else if(v == _gallery){
            if(permissionChecked==1)
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            else
                checkAndroidVersion();
        }
        else if(v == cancelImage){
            checkForImages=0;
            imageView.setVisibility(View.GONE);
            cancelImage.setVisibility(View.GONE);
        }
    }
}
