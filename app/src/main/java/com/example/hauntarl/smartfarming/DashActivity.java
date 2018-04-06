package com.example.hauntarl.smartfarming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class DashActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public GridView gridView;
    int images[] =
            {
                    R.drawable.crop,
                    R.drawable.active,
                    R.drawable.plan,
                    R.drawable.redeem,
                    R.drawable.chat,
                    R.drawable.call,
                    R.drawable.sharefarm,
                    R.drawable.about_us
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        gridView = findViewById(R.id.gridView1);

        GridActivity gridAdapter = new GridActivity(getApplicationContext(),images);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }



    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //position would tell u everything
        switch(position){
            case 0:
                Toast.makeText(getApplicationContext(),"entry selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),EntryActivity.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
            case 4:
                Toast.makeText(getApplicationContext(),"query selected!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),QueryActivity.class));
                overridePendingTransition(R.anim.entry_from_left,R.anim.exit_from_left);
                return ;
        }
    }
}
