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



    private String selectedVegzettseg;
    public  String selectedKategoria;
    private ArrayAdapter<String> adapterVegzettseg;
    private ArrayAdapter<Kategoria> adapterKategoria;
    private EditText EditNameKat, EditNameEszkoz, EditKatEszkoz, EditTipusEszkoz, EditAzonEszkoz, EditVegzettsegKat, EditUjVegzettseg;
    private Button kuldes, kuldesEszkoz, AddVegzettseg, teszt;
    private Spinner vegzettsegSpinner, kategoriaSpinner;
    FirebaseFirestore rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootRef = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_eszkozfelelos_main);

        kategoriaSpinner = findViewById(R.id.kategoriaSpinner);
        kategoriaSpinner.setOnItemSelectedListener(this);

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

       // Log.d("teszt4", Loader.kategoriak.get(1).nev);

    }

    public void AddKategoria()
    {

        CollectionReference Kategoriak = rootRef.collection("Kategoriak");
        Kategoria ujKategoria = new Kategoria(EditNameKat.getText().toString());

        Log.d("teszt", "hozzaadva" + ujKategoria.nev.toString());
        Map<String, Object> note = new HashMap<>();
        Log.d("teszt", EditNameKat.getText().toString());
        if(selectedVegzettseg == null)
        {
            Toast.makeText(getApplicationContext(),"Szükséges végzettség nincs kiválasztva!", Toast.LENGTH_SHORT).show();
            return;
        }

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
            Eszkoz uj = new Eszkoz(EditNameEszkoz.getText().toString(), EditTipusEszkoz.getText().toString());
           // String res =
          //  Log.d("teszt2", res);
            Log.d("teszt2", "lefut a katt");
            Loader.kategoriak.get(katId).AddEszkoz(uj);
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
            default:
                break;
        }

        if(selectedVegzettseg == "Végzettség...")
            selectedVegzettseg = null;

        if(selectedKategoria == "Kategória...")
            selectedKategoria = null;
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