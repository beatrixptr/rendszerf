package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KarbantartoFeladatokActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button megerositBtn;
    TextView adatokTV, cancerTV;
    Spinner sajatFeladatokSpinner, statuszSpinner;
    ArrayAdapter<KarbantartasiFeladat> adapterSajatFeladat;
    ArrayAdapter<CharSequence> adapterStatusz;
    KarbantartasiFeladat karbF;
    int karbFPos;
    String statusz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karbantarto_feladatok);

        String karb = getIntent().getExtras().getString("karb");
        adatokTV = findViewById(R.id.adatokTV);
        cancerTV = findViewById(R.id.cancerTV);

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

                //String cancer = parent.getItemAtPosition(pos).toString();
                //adatokTV.setText(cancer);
                if (statusz.equals("Megkezdve"))
                {
                    String instrukcio = "-->Hiba:\n" + hiba + "\n\n-->Instrukciok:\n" + eszkinstukcio;
                    adatokTV.setText(instrukcio);
                }
                karbFPos = pos;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        statuszSpinner = findViewById(R.id.statuszSpinner);
        adapterStatusz = ArrayAdapter.createFromResource(this,R.array.statusz, android.R.layout.simple_spinner_dropdown_item);
        adapterStatusz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statuszSpinner.setAdapter(adapterStatusz);
        statuszSpinner.setOnItemSelectedListener(this);

        megerositBtn = findViewById(R.id.megerositBtn);
        megerositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statuszMegerosit();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        statusz = parent.getItemAtPosition(pos).toString();
        //Toast.makeText(this, statusz, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void statuszMegerosit(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        karbF.setStatusz(statusz);
        String karbFelNev = karbF.eszkoz.getNev() +"-"+ karbF.idopont;
        if(statusz != null){
            cancerTV.append("\n"+karbF.getStatusz());
            DocumentReference karbFelRef = db.collection("Karbantartasok").document(karbFelNev);
            karbFelRef.update("statusz",statusz);

            if(statusz.equals("Megkezdve"))
            {
                String instrukcio = "-->Hiba:\n" + karbF.getHiba_leiras() + "\n\n-->Instrukciok:\n" + karbF.getEszkoz().instrukcio;
                adatokTV.setText(instrukcio);
            }
            if(statusz.equals("Befejezve") )
            {
                if(karbF.tipus.equals("Rendkivuli"))
                    karbFelRef.delete();

                KarbantartasKezelo.sajatFeladatok.remove(karbF);
                Loader.removeFeladat(Loader.loggedInUser, karbF);
            }

          /*  DocumentReference sajatFelRef = db.collection("Users").document(Loader.loggedInUser);
            sajatFelRef.get().whereArrayContaints("Feladatok", karbF).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                }
            });*/
        }

        /*usersRef.whereArrayContains("friends", john).get().addOnCompleteListener{ johnTask ->
    johnTask.apply {
        if (johnTask.isSuccessful) {
            for (document in result) {
                val docIdRef = usersRef.document(document.id)
                docIdRef.update("friends", FieldValue.arrayRemove(john)).addOnCompleteListener{ removeTask ->
                    if (removeTask.isSuccessful) {
                        docIdRef.update("friends", FieldValue.arrayUnion(Friend("John", 21))).addOnCompleteListener{ additionTask ->*/
    }
}