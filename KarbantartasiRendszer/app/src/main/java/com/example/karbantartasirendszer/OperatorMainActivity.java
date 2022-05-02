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

import java.util.Map;

public class OperatorMainActivity extends AppCompatActivity {

    Eszkoz eszkTemp;


    private TextView cTV, hibaTV,allapotTV,tipusTV,idopontTV;
    private Button listBtn;
    private Spinner feladatSpinner;

    private KarbantartasiFeladat selectedFeladat;
    int selectedPos;
    ArrayAdapter<KarbantartasiFeladat> adapterFeladat;

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

        listBtn = findViewById(R.id.listBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadKarbantartasiFeladatok();
            }
        });

        feladatSpinner = findViewById(R.id.feladatSpinner);
        eszkTemp = new Eszkoz("Válasszon...", "", "", "", "", "", "", "");
        KarbantartasKezelo.feladatok.add(new KarbantartasiFeladat(eszkTemp,"","","",""));
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


        feladatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedFeladat = (KarbantartasiFeladat) parent.getItemAtPosition(pos);
                selectedPos = pos;

                hibaTV.setText(selectedFeladat.getHiba_leiras());
                allapotTV.setText(selectedFeladat.getStatusz());
                tipusTV.setText(selectedFeladat.getTipus());
                idopontTV.setText(selectedFeladat.getIdopont());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cTV.setText("nothing");
            }
        });
    }

    private void loadKarbantartasiFeladatok(){
        KarbantartasKezelo.feladatok.clear();
        KarbantartasKezelo.feladatok.add(new KarbantartasiFeladat(eszkTemp,"","","",""));
        CollectionReference karbantartasokReference = db.collection("Karbantartasok");
        karbantartasokReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){

                        Map data = document.getData();
                        Map eszk = (Map) data.get("Eszkoz");
                        String eszkNev = (String) eszk.get("nev");
                        String azonosito = (String) eszk.get("azonosito");
                        String elhelyezkedes = (String) eszk.get("elhelyezkedes");
                        String tipus = (String) eszk.get("tipus");
                        String kategoria = (String) eszk.get("kategoria");
                        String periodus = (String) eszk.get("periodus");
                        String normaido = (String) eszk.get("normaido");
                        String instrukcio = (String) eszk.get("instrukcio");
                        Eszkoz eszkoz = new Eszkoz(eszkNev,kategoria,tipus,azonosito,elhelyezkedes,periodus,normaido,instrukcio);
                        String hiba = document.getString("hiba_leiras");
                        String idopont = document.getString("idopont");
                        String statusz = document.getString("statusz");
                        String karbTipus = document.getString("tipus");
                        KarbantartasiFeladat feladat = new KarbantartasiFeladat(eszkoz,tipus,hiba,statusz,idopont);

                        KarbantartasKezelo.feladatok.add(feladat);
                    }

                }
                else
                {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}