package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class KarbantartoFeladatokActivity extends AppCompatActivity {

    TextView adatokTV;
    Spinner sajatFeladatokSpinner;
    ArrayAdapter<KarbantartasiFeladat> adapterSajatFeladat;
    KarbantartasiFeladat selectedFeladat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_feladatok);

        String karb = getIntent().getExtras().getString("karb");
        adatokTV = findViewById(R.id.adatokTV);
        sajatFeladatokSpinner = findViewById(R.id.sajatFeladatokSpinner);

        adapterSajatFeladat = new ArrayAdapter<KarbantartasiFeladat>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                KarbantartasKezelo.sajatFeladatok) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };
        adapterSajatFeladat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sajatFeladatokSpinner.setAdapter(adapterSajatFeladat);
        sajatFeladatokSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                selectedFeladat = (KarbantartasiFeladat) parent.getSelectedItem();
                adatokTV.setText(selectedFeladat.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}