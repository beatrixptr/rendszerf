package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends AppCompatActivity {

    private Button addUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        addUser = findViewById(R.id.addUser);

    }

    public void addUser(View v){
        Intent i = new Intent(AdminMainActivity.this, addUser.class);
        startActivity(i);
    }





}