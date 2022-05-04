package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class KarbantartoMainActivity extends AppCompatActivity {



    private Button hibaButton, feladatokButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_main);

        String karb = getIntent().getExtras().getString("karb");
        Loader.loadSajatFeladatok(karb);

        feladatokButton = findViewById(R.id.feladatok);
        feladatokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KarbantartoMainActivity.this, KarbantartoFeladatokActivity.class);
                i.putExtra("karb",karb);
                startActivity(i);
            }
        });

        hibaButton = findViewById(R.id.hibabejelentes);
        hibaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KarbantartoMainActivity.this, KarbantartoHibaActivity.class);
                startActivity(i);
            }
        });

        Toast.makeText(this, karb, Toast.LENGTH_SHORT).show();
    }
}