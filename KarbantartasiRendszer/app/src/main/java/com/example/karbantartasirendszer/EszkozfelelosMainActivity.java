package com.example.karbantartasirendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EszkozfelelosMainActivity extends AppCompatActivity {

    private ArrayList<Kategoria> kategoriak;
    private EditText EditNameKat, EditNameEszkoz, EditKatEszkoz, EditTipusEszkoz, EditAzonEszkoz, EditVegzettsegKat;
    private Button kuldes, kuldesEszkoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eszkozfelelos_main);
        kategoriak = new ArrayList<Kategoria>();
        loadKategoriak();
      //  EditPlace = findViewById(R.id.EditName);
        EditNameKat = findViewById(R.id.editNameKat);
        EditVegzettsegKat = findViewById(R.id.editVegzettsegKat);

        EditNameEszkoz = findViewById(R.id.editNameEszkoz);
        EditKatEszkoz = findViewById(R.id.editKategoriaEszkoz);
        EditTipusEszkoz = findViewById(R.id.editTipusEszkoz);
        EditAzonEszkoz = findViewById(R.id.editAzonositoEszkoz);

        kuldes = findViewById(R.id.submitKat);
        kuldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddKategoria();
            }
        });

        kuldesEszkoz = findViewById(R.id.submitEszkoz);
        kuldesEszkoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEszkozToKategoria();
            }
        });
    }

    public void AddKategoria()
    {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");
        Kategoria ujKategoria = new Kategoria(EditNameKat.getText().toString());
        kategoriak.add(ujKategoria);
        Log.d("teszt", "hozzaadva" + ujKategoria.nev.toString());
        Map<String, Object> note = new HashMap<>();
       // note.put("Nev", "cs");
        Log.d("teszt", EditNameKat.getText().toString());
        Kategoriak.document(ujKategoria.nev).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Új kategória hozzáadva", Toast.LENGTH_LONG).show();
                Map<String, Object> note = new HashMap<>();
                note.put("Vegzettseg", EditVegzettsegKat.getText().toString());
                Kategoriak.document(ujKategoria.nev).update(note);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getApplicationContext(),"Valami hiba történt",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void AddEszkozToKategoria()
    {
        int katId = getKategoriaIDByNev(EditKatEszkoz.getText().toString());
        if( katId != -1)
        {
            Eszkoz uj = new Eszkoz(EditNameEszkoz.getText().toString(), EditTipusEszkoz.getText().toString());
           // String res =
          //  Log.d("teszt2", res);
            Log.d("teszt2", "lefut a katt");
            kategoriak.get(katId).AddEszkoz(uj);
        }
        Log.d("teszt2", "lefut a katt, de kivul");
    }

    private int getKategoriaIDByNev(String kat)
    {
        for(int i=0; i<kategoriak.size(); i++)
        {
            Log.d("teszt2", "osszehasonlitani:" + kat.toString() + " - " + kategoriak.get(i).nev.toString());
            if(kat.equals(kategoriak.get(i).nev.toString()))
            {
                return i;
            }
        }
        return -1;
    }

    private void loadKategoriak()
    {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");
        Kategoriak.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        kategoriak.add(new Kategoria(document.getId().toString()));
                    }

                    for(int i=0; i<kategoriak.size(); i++)
                    {
                        Log.d("teszt", kategoriak.get(i).nev.toString());
                    }
                }
            }
        });
    }

  /*  private Button vissza;
    private static List<String> list;
    private static List<String> names;

    private static ArrayList<Type> mArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eszkozfelelos_main);

        EditPlace = findViewById(R.id.EditPlace);
        EditName = findViewById(R.id.EditName);
        EditAzon = findViewById(R.id.EditAzon);
        EditKateg = findViewById(R.id.EditKateg);
        EditCom = findViewById(R.id.EditCom);
        EditKarIdo = findViewById(R.id.EditKarIdo);
        EditNormIdo = findViewById(R.id.EditNormIdo);
        EditInstr = findViewById(R.id.EditInstr);
        vissza = findViewById(R.id.back);
        vissza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EszkozfelelosMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        kuldes = findViewById(R.id.submit);
        kuldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }
    private void test() {

                Map<String, Object> note = new HashMap<>();
                note.put("Azonosito",EditAzon.getText().toString());
                note.put("Elhelyezkedes",EditPlace.getText().toString());
                note.put("Kategoria",EditKateg.getText().toString());
                note.put("Leiras",EditCom.getText().toString());
                note.put("Karbantartasi ido",EditKarIdo.getText().toString());
                note.put("Norm ido",EditNormIdo.getText().toString());
                note.put("Instrukcio",EditInstr.getText().toString());


                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference Eszkozok = rootRef.collection("Eszkozok");

                Eszkozok.document(EditName.getText().toString()).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EszkozfelelosMainActivity.this,"Új eszköz elmentve", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EszkozfelelosMainActivity.this,"Valami hiba tortent",Toast.LENGTH_LONG).show();

                    }
                });;

    }
   */
}