package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KarbantartoFeladatokActivity extends AppCompatActivity {

    TextView adatokTV, cancerTV;
    Spinner sajatFeladatokSpinner;
    ArrayAdapter<KarbantartasiFeladat> adapterSajatFeladat;
    KarbantartasiFeladat karbF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_feladatok);

        String karb = getIntent().getExtras().getString("karb");
        adatokTV = findViewById(R.id.adatokTV);
        cancerTV = findViewById(R.id.cancerTV);
        //convertToKarbantartasiFeladat();
        sajatFeladatokSpinner = findViewById(R.id.sajatFeladatokSpinner);

        adapterSajatFeladat = new ArrayAdapter<KarbantartasiFeladat>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                KarbantartasKezelo.sajatFeladatok) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return true;
                return true;
            }
        };
        adapterSajatFeladat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sajatFeladatokSpinner.setAdapter(adapterSajatFeladat);
        sajatFeladatokSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                Map map = (HashMap) parent.getItemAtPosition(pos);
                Map eszkMap = (HashMap) map.get("eszkoz");
                String eszknev = String.valueOf(eszkMap.get("nev"));
                String eszkazon = String.valueOf(eszkMap.get("azonosito"));
                String eszkper = String.valueOf(eszkMap.get("periodus"));
                String eszkkat = String.valueOf(eszkMap.get("kategoria"));
                String eszknormaido = String.valueOf(eszkMap.get("normaido"));
                String eszkelhelyezkedes = String.valueOf(eszkMap.get("elhelyezkedes"));
                String eszkinstukcio = String.valueOf(eszkMap.get("instrukcio"));
                String eszktipus = String.valueOf(eszkMap.get("valami"));
                Eszkoz tempEszk = new Eszkoz(eszknev,eszkkat,eszktipus,
                        eszkazon,eszkelhelyezkedes,eszkper,
                        eszknormaido,eszkinstukcio);
                String statusz = String.valueOf(map.get("statusz"));
                String idopont = String.valueOf(map.get("idopont"));
                String hiba = String.valueOf(map.get("hiba_leiras"));
                String tipus = String.valueOf(map.get("tipus"));
                karbF = new KarbantartasiFeladat(tempEszk,tipus,hiba,
                        statusz,idopont);
                cancerTV.setText(karbF.toString());

                String cancer = parent.getItemAtPosition(pos).toString();
                //adatokTV.setText(parent.getItemAtPosition(pos).getClass().toString());
                adatokTV.setText(cancer);
                //adatokTV.append("\nteszt: " + );

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void convertToKarbantartasiFeladat() {

    }

}