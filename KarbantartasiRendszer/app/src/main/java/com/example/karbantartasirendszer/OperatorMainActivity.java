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

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                Loader.loadKarbantartasiFeladatok();
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

                hibaTV.setText(selectedFeladat.hiba_leiras);
                allapotTV.setText(selectedFeladat.statusz);
                tipusTV.setText(selectedFeladat.tipus);
                idopontTV.setText(selectedFeladat.idopont);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cTV.setText("nothing");
            }
        });
    }


}