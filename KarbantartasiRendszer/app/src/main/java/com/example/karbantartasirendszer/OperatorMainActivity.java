package com.example.karbantartasirendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class OperatorMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private TextView cTV, hibaTV,allapotTV,tipusTV,idopontTV;
    private Button listBtn, hozzarendelBtn;
    private Spinner feladatSpinner, karbSpinner;
    private ArrayList<KarbantartoUser> karbTemp = new ArrayList<KarbantartoUser>();
    private KarbantartasiFeladat selectedFeladat;
    private KarbantartoUser selectedUser;
    int selectedPos;
    ArrayAdapter<KarbantartasiFeladat> adapterFeladat;
    ArrayAdapter<KarbantartoUser> adapterKarb;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_main);

        cTV = findViewById(R.id.cTV);
        cTV.setText(":)");
        hibaTV = findViewById(R.id.hibaTV);
        allapotTV = findViewById(R.id.allapotTV);
        tipusTV = findViewById(R.id.tipusTV);
        idopontTV = findViewById(R.id.idopontTV);

        karbTemp = new ArrayList<KarbantartoUser>();
        karbTemp.add(new KarbantartoUser("Karbantartó..", null));
        karbSpinner =findViewById(R.id.karbantartoSpinner);
        adapterKarb = new ArrayAdapter<KarbantartoUser>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                karbTemp) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };
        adapterKarb.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        karbSpinner.setAdapter(adapterKarb);
        karbSpinner.setOnItemSelectedListener(this);

        feladatSpinner = findViewById(R.id.feladatSpinner);
        //eszkTemp = new Eszkoz("Válasszon...", "", "", "", "", "", "", "");
        //KarbantartasKezelo.feladatok.add(new KarbantartasiFeladat(eszkTemp,"","","",""));
        adapterFeladat = new ArrayAdapter<KarbantartasiFeladat>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                KarbantartasKezelo.feladatok) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };
        adapterFeladat.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        feladatSpinner.setAdapter(adapterFeladat);
        feladatSpinner.setOnItemSelectedListener(this);


        hozzarendelBtn = findViewById(R.id.hozzarendelBtn);
        hozzarendelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedUser != null && selectedFeladat != null)
                    selectedUser.addFeladat(selectedFeladat);

            }
        });
    }

    public void UpdateElerhetoKarbantarto()
    {
        if(selectedFeladat == null || selectedFeladat.toString().equals("Válasszon...-"))
            return;

        karbTemp.clear();
        karbTemp.add(new KarbantartoUser("Karbantartó..", null));
        karbSpinner.setAdapter(adapterKarb);

        Kategoria vizsgaltKategoria = getKategoriaByNev(selectedFeladat.eszkoz.kategoria);
        Log.d("match", vizsgaltKategoria.toString());
        ArrayList<String> szuksegesVegzettsegek = getSzuksegesVegzettsegekByKategoria(vizsgaltKategoria);

        for(int i=1; i<Loader.karbantartok.size(); i++)
        {
            //Log.d("match2", "belepett karbantartok");
            for(int j=0; j<Loader.karbantartok.get(i).vegzettsegek.size(); j++)
            {
                //Log.d("match2", "belepett elsoelso");
                for(int k=0; k<szuksegesVegzettsegek.size(); k++)
                {
                    //Log.d("match2", "belepett masodik");
                    if(szuksegesVegzettsegek.get(k).equals(Loader.karbantartok.get(i).vegzettsegek.get(j)))
                    {
                        Log.d("match2", "vegzettseg matchel karbantarto: " + Loader.karbantartok.get(i).nev);
                        Log.d("match2", szuksegesVegzettsegek.get(k) + " = " +(Loader.karbantartok.get(i).vegzettsegek.get(j)));
                        if(!karbTemp.contains(Loader.karbantartok.get(i)))
                            karbTemp.add(Loader.karbantartok.get(i));
                    }
                }
            }
        }
    }

    private ArrayList<String> getKarbantartoVegzettsegek(KarbantartoUser karbantarto)
    {
        ArrayList<String> karbVegzettsegek = new ArrayList<String>();
        for(int i=0; i< karbantarto.vegzettsegek.size(); i++)
        {
            karbVegzettsegek.add(karbantarto.vegzettsegek.get(i));
        }
        return karbVegzettsegek;
    }

    private ArrayList<String> getSzuksegesVegzettsegekByKategoria(Kategoria kat)
    {
        ArrayList<String> katVegzettsegek = new ArrayList<String>();
        for(int i=0; i<kat.szuksegesVegzettsegek.size(); i++)
        {
            katVegzettsegek.add(kat.szuksegesVegzettsegek.get(i));
        }
        return katVegzettsegek;
    }

    private Kategoria getKategoriaByNev(String nev)
    {
        for(int i=1; i<Loader.kategoriak.size(); i++)
        {
            if(nev.equals(Loader.kategoriak.get(i).nev))
            {
                return Loader.kategoriak.get(i);
            }
        }
        return null;
    }

  //  private ArrayList<String> getSzuksegesVegzettsegek(Kategoria )

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch (parent.getId()) {
            case R.id.feladatSpinner:
                selectedFeladat = (KarbantartasiFeladat) parent.getItemAtPosition(pos);
                selectedPos = pos;

                hibaTV.setText(selectedFeladat.getHiba_leiras());
                allapotTV.setText(selectedFeladat.getStatusz());
                tipusTV.setText(selectedFeladat.getTipus());
                idopontTV.setText(selectedFeladat.getIdopont());
                UpdateElerhetoKarbantarto();
                break;

            case R.id.karbantartoSpinner:

                    selectedUser = (KarbantartoUser) parent.getItemAtPosition(pos);

                break;
        }

        if(selectedUser != null)
        {
            if(selectedUser.nev.equals("Karbantartó.."))
                selectedUser = null;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}