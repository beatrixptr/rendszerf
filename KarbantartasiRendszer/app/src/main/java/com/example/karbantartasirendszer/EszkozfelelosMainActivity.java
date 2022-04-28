package com.example.karbantartasirendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EszkozfelelosMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    private String selectedVegzettseg, selectedKategoria, selectedPeriodusKat, selectedPeriodusEszk;
    private ArrayAdapter<String> adapterVegzettseg;
    private ArrayAdapter<CharSequence> adapterPeriodusKat, adapterPeriodusEszk;
    private ArrayAdapter<Kategoria> adapterKategoria;
    private EditText EditNameKat, EditNameEszkoz, EditKatEszkoz, EditTipusEszkoz, EditAzonEszkoz, EditVegzettsegKat, EditUjVegzettseg, EditNormaKat, EditNormaEszk, EditInstrukcioKat, EditInstrukcioEszk, EditHely;
    private Button kuldes, kuldesEszkoz, AddVegzettseg, teszt;
    private Spinner vegzettsegSpinner, kategoriaSpinner, periodusSpinnerKat, periodusSpinnerEszk;

    FirebaseFirestore rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootRef = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_eszkozfelelos_main);

        kategoriaSpinner = findViewById(R.id.kategoriaSpinner);
        kategoriaSpinner.setOnItemSelectedListener(this);

        periodusSpinnerKat = findViewById(R.id.periodusSpinnerKat);
        periodusSpinnerKat.setOnItemSelectedListener(this);

        periodusSpinnerEszk = findViewById(R.id.periodusSpinnerEszk);
        periodusSpinnerEszk.setOnItemSelectedListener(this);

        vegzettsegSpinner = findViewById(R.id.vegzettsegSpinner);
        vegzettsegSpinner.setOnItemSelectedListener(this);

        Loader.loadKategoriak();
        Loader.loadVegzettsegek();

              //  EditPlace = findViewById(R.id.EditName);
        EditNameKat = findViewById(R.id.editNameKat);
       // EditVegzettsegKat = findViewById(R.id.editVegzettsegKat);

        EditNameEszkoz = findViewById(R.id.editNameEszkoz);
       // EditKatEszkoz = findViewById(R.id.editKategoriaEszkoz);
        EditTipusEszkoz = findViewById(R.id.editTipusEszkoz);
        EditAzonEszkoz = findViewById(R.id.editAzonositoEszkoz);
        EditUjVegzettseg = findViewById(R.id.editUjVegzettseg);

        EditNormaKat = findViewById(R.id.editNormaKat);
        EditInstrukcioKat = findViewById(R.id.editInstrukcioKat);
        EditNormaEszk = findViewById(R.id.editNormaEszk);
        EditInstrukcioEszk = findViewById(R.id.editInstrukcioEszk);
        EditHely = findViewById(R.id.editHely);

        kuldes = findViewById(R.id.submitKat);
        kuldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddKategoria();
            }
        });

        AddVegzettseg = findViewById(R.id.addVegzettseg);
        AddVegzettseg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUjVegzettseg();
            }
        });

        kuldesEszkoz = findViewById(R.id.submitEszkoz);
        kuldesEszkoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEszkozToKategoria();
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
        kategoriaSpinner.setAdapter(adapterKategoria);

        adapterVegzettseg = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, Loader.vegzettsegek) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };;
        adapterVegzettseg.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        vegzettsegSpinner.setAdapter(adapterVegzettseg);


        adapterPeriodusKat = ArrayAdapter.createFromResource(this, R.array.periodusok, android.R.layout.simple_spinner_dropdown_item);
        adapterPeriodusKat.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        periodusSpinnerKat.setAdapter(adapterPeriodusKat);

        adapterPeriodusEszk = ArrayAdapter.createFromResource(this, R.array.periodusok, android.R.layout.simple_spinner_dropdown_item);
        adapterPeriodusEszk.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        periodusSpinnerEszk.setAdapter(adapterPeriodusEszk);

        periodusSpinnerKat.setSelection(0, false);
    }

    public void AddKategoria()
    {

        if(EditNormaKat.getText().toString().isEmpty() || selectedPeriodusKat == null || selectedVegzettseg == null)
        {
            Toast.makeText(getApplicationContext(),"Szükséges adatok nincsenek megadva!", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");
        Kategoria ujKategoria = new Kategoria(EditNameKat.getText().toString(), EditNormaKat.getText().toString(), selectedPeriodusKat, EditInstrukcioKat.getText().toString());

        Log.d("teszt", "hozzaadva" + ujKategoria.nev.toString());
        Map<String, Object> note = new HashMap<>();
        note.put("normaido", EditNormaKat.getText().toString());
        note.put("periodus", selectedPeriodusKat);
        note.put("instrukcio", EditInstrukcioKat.getText().toString());
        Log.d("teszt", EditNameKat.getText().toString());

        Kategoriak.document(ujKategoria.nev).set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Új kategória hozzáadva", Toast.LENGTH_SHORT).show();
                if(!kategoriaLetezik(ujKategoria))
                    Loader.kategoriak.add(ujKategoria);
            //    Kategoriak.document(ujKategoria.nev).update(note);
                Kategoriak.document(ujKategoria.nev).update("Vegzettsegek", FieldValue.arrayUnion(selectedVegzettseg));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getApplicationContext(),"Valami hiba történt",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean kategoriaLetezik(Kategoria uj)
    {
        for(int i=0; i<Loader.kategoriak.size(); i++)
        {
            if(uj.nev.toString().equals(Loader.kategoriak.get(i).nev.toString()))
                return true;
        }
        return false;
    }

    private void AddEszkozToKategoria()
    {
        if(selectedKategoria == null)
        {
            Toast.makeText(getApplicationContext(),"Szükséges kategória nincs kiválasztva!", Toast.LENGTH_SHORT).show();
            return;
        }
        int katId = getKategoriaIDByNev(selectedKategoria);
        if( katId != -1)
        {
            //String Nev, String Kat, String Tipus, String Azonosito, S
            //String Azonosito, String Elhelyezkedes, String Tipus, String Karbantartasido,String Normaido,String Instrukcio
            Eszkoz uj = new Eszkoz(EditNameEszkoz.getText().toString(), selectedKategoria, EditTipusEszkoz.getText().toString(), EditAzonEszkoz.getText().toString(), EditHely.getText().toString(), selectedPeriodusEszk, EditNormaEszk.getText().toString(), EditInstrukcioEszk.getText().toString());//EditNameEszkoz.getText().toString(), EditTipusEszkoz.getText().toString());
           // String res =
          //  Log.d("teszt2", res);
            Log.d("teszt2", "lefut a katt");
            Loader.kategoriak.get(katId).AddEszkoz(uj, selectedPeriodusEszk);
        }
        Log.d("teszt2", "lefut a katt, de kivul");
    }

    private int getKategoriaIDByNev(String kat)
    {
        for(int i=0; i<Loader.kategoriak.size(); i++)
        {
            Log.d("teszt2", "osszehasonlitani:" + kat.toString() + " - " + Loader.kategoriak.get(i).nev.toString());
            if(kat.equals(Loader.kategoriak.get(i).nev.toString()))
            {
                return i;
            }
        }
        return -1;
    }

    private void AddUjVegzettseg()
    {
        CollectionReference VegzettsegCol = rootRef.collection("Vegzettsegek");
        Map<String, Object> note = new HashMap<>();
        VegzettsegCol.document(EditUjVegzettseg.getText().toString()).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(getApplicationContext(),"Új eszköz elmentve", Toast.LENGTH_LONG).show();
                if(!vegzettsegLetezik(EditUjVegzettseg.getText().toString()))
                    Loader.vegzettsegek.add(EditUjVegzettseg.getText().toString());
                Log.d("teszt3", "Új végzettség( " + EditUjVegzettseg.getText().toString() + " ) hozzáadva a végzettségekhez");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Toast.makeText(getApplicationContext(),"Valami hiba tortent",Toast.LENGTH_LONG).show();
                Log.d("teszt3", "Nem sikerült a végzettség hozzáadása");
            }
        });
    }

    private boolean vegzettsegLetezik(String ujnev)
    {
        for(int i=0; i<Loader.vegzettsegek.size(); i++)
        {
            if(ujnev.equals(Loader.vegzettsegek.get(i)))
                return true;
        }
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.vegzettsegSpinner:
                    selectedVegzettseg = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.kategoriaSpinner:
                    selectedKategoria = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.periodusSpinnerKat:
                    if(pos == 0)
                        selectedPeriodusKat = null;
                    else
                        selectedPeriodusKat = parent.getItemAtPosition(pos).toString();

                   // Log.d("teszt6", selectedPeriodusKat);
                break;
            case R.id.periodusSpinnerEszk:
                    if(pos == 0)
                        selectedPeriodusEszk = null;
                    else
                        selectedPeriodusEszk = parent.getItemAtPosition(pos).toString();
                break;
            default:
                break;
        }

        if(selectedVegzettseg == "Végzettség...")
            selectedVegzettseg = null;

        if(selectedKategoria == "Kategória...")
            selectedKategoria = null;

        if(selectedPeriodusKat == "Periódus...")
            selectedPeriodusKat = null;

        if(selectedPeriodusEszk == "Periódus...")
            selectedPeriodusEszk = null;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()) {
            case R.id.vegzettsegSpinner:
                selectedVegzettseg = null;
                break;
            case R.id.kategoriaSpinner:
                selectedKategoria = null;
                break;
            default:
                break;
        }

    }
}