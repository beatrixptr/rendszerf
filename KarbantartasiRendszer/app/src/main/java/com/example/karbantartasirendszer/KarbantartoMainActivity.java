package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KarbantartoMainActivity extends AppCompatActivity {

    private Button hibaButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_main);

        hibaButton = findViewById(R.id.hibabejelentes);
        hibaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KarbantartoMainActivity.this, KarbantartoHibaActivity.class);
                startActivity(i);
            }
        });
    }
}