package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class KarbantartoHibaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    ArrayList<Eszkoz> eszkozTemp;

    ArrayAdapter<Kategoria> adapterKategoria;
    ArrayAdapter<Eszkoz> adapterEszkoz;
    ArrayAdapter<CharSequence> adapterTipus;

    String selectedKategoria;
    Eszkoz selectedEszkoz;
    String selectedTipus;

    Spinner katSpinner, eszkSpinner, tipusSpinner;
    EditText hibaleiras;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_hiba);
        //Loader.loadKategoriak();
        eszkozTemp = new ArrayList<Eszkoz>();

        hibaleiras = findViewById(R.id.hiba_leiras);

        katSpinner = findViewById(R.id.spinner_kategoria);
        katSpinner.setOnItemSelectedListener(KarbantartoHibaActivity.this);

        eszkSpinner = findViewById(R.id.spinner_eszkoz);
        eszkSpinner.setOnItemSelectedListener(KarbantartoHibaActivity.this);
        tipusSpinner = findViewById(R.id.spinner_tipus);
        tipusSpinner.setOnItemSelectedListener(KarbantartoHibaActivity.this);

        adapterTipus = ArrayAdapter.createFromResource(this, R.array.feladatTipus, android.R.layout.simple_spinner_dropdown_item);
        tipusSpinner.setAdapter(adapterTipus);

        submitButton = findViewById(R.id.submitHiba);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedKategoria == null || selectedEszkoz == null) return; //|| selectedTipus == null) return;
                Log.d("karbantarto", "lefut");
                new KarbantartasiFeladat(selectedEszkoz, "Rendkivuli", hibaleiras.getText().toString(), "Utemezetlen");
                  //       public KarbantartasiFeladat(Eszkoz _eszkoz, String _tipus, String _hiba_leiras)
            }
        });

        adapterKategoria = new ArrayAdapter<Kategoria>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, Loader.kategoriak) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };
        adapterKategoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        katSpinner.setAdapter(adapterKategoria);

        eszkozTemp.add(new Eszkoz("Eszközök...", "", "", "", "", "", "", ""));
        adapterEszkoz = new ArrayAdapter<Eszkoz>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, eszkozTemp) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };
        adapterEszkoz.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        eszkSpinner.setAdapter(adapterEszkoz);



    }


    private void EszkozokFrissites()
    {

        eszkozTemp.clear();
        eszkozTemp.add(new Eszkoz("Eszközök...", "", "", "", "", "", "", ""));
        selectedEszkoz = null;
        ArrayList<Eszkoz> megfeleloEszkoz = findEszkozByKategoria(selectedKategoria);
        for(int i=0;i<megfeleloEszkoz.size(); i++)
        {
            eszkozTemp.add(megfeleloEszkoz.get(i));
        }



    }

    private ArrayList<Eszkoz> findEszkozByKategoria(String katnev)
    {
        ArrayList<Eszkoz> eszkozok = new ArrayList<Eszkoz>();
        //Log.d("karbantarto", Loader.eszkozok.get(0).nev);
       for(int i=0; i<Loader.eszkozok.size(); i++)
        {
            //Log.d("karbantarto", Loader.eszkozok.get(i).nev + " - " + Loader.eszkozok.get(i).kategoria);
            if(katnev.equals(Loader.eszkozok.get(i).kategoria))
            {
                eszkozok.add(Loader.eszkozok.get(i));
            }
        }
        return eszkozok;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("karbantarto", "parent: " + parent.getId());
        switch (parent.getId()) {
            case R.id.spinner_kategoria:
                Log.d("karbantarto", "ayyo");
                    selectedKategoria = parent.getItemAtPosition(pos).toString();
                Log.d("karbantarto", "Selected kat:" + selectedKategoria);// + " " + selectedEszkoz.kategoria);
                    EszkozokFrissites();
                break;
            case R.id.spinner_eszkoz:
                    Log.d("karbantarto", "lefut most épp jo");
                    selectedEszkoz = (Eszkoz)parent.getItemAtPosition(pos);
                    Log.d("karbantarto", "Selected eszk:" + selectedEszkoz.nev + " " + selectedEszkoz.kategoria);
                break;

            case R.id.spinner_tipus:
                    selectedTipus = parent.getItemAtPosition(pos).toString();
                break;
            default:

                break;
        }


        if(selectedKategoria == "Kategória...")
            selectedKategoria = null;

        if(selectedKategoria == "Eszközök...")
            selectedEszkoz = null;

        if(selectedTipus == "Típus...")
            selectedTipus = null;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()) {
            case R.id.spinner_kategoria:
                selectedKategoria = null;
                break;
            case R.id.spinner_eszkoz:
                selectedEszkoz = null;
                break;
            case R.id.spinner_tipus:
                selectedTipus = null;
                break;
            default:
                break;
        }

    }
}