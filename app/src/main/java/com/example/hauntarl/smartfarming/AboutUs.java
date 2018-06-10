package com.example.hauntarl.smartfarming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutUs extends AppCompatActivity {

    private Button share;
    private Button consul_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        share= findViewById(R.id.share);
        consul_1=findViewById(R.id.consultant_1);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out this app at: http://smartfarming.payasmedia.com/genX.apk \n Copy and paste this link in the browser and download!");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        consul_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Consultant_1.class));
            }
        });
    }
}
