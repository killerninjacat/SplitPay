package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    int id;
    TextView welcome;
    String username;
    Button transactions,splits,logout;
    private void clearLoginState() {
        getSharedPreferences("loginPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        id=getIntent().getIntExtra("userid",0);
        username=getIntent().getStringExtra("uname");
        transactions=(Button) findViewById(R.id.transactions);
        splits=(Button) findViewById(R.id.splits);
        welcome=(TextView) findViewById(R.id.textView);
        logout=(Button) findViewById(R.id.logout);
        welcome.setText(" WELCOME, "+username);
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,TransactionsActivity.class);
                i.putExtra("u_id",id);
                startActivity(i);
            }
        });
        splits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(HomeActivity.this,SplitsActivity.class);
                startActivity(i1);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoginState();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}