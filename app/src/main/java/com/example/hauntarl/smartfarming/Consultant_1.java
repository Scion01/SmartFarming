package com.example.hauntarl.smartfarming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Consultant_1 extends AppCompatActivity {

    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_1);
        name=findViewById(R.id.name);
       // name.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
    }
}
