package com.example.karbantartasirendszer;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class Kategoria {
    public String nev;


    public Kategoria(String _nev)
    {
        nev = _nev;
    }

    public void AddEszkoz(Eszkoz eszk)
    {
        Log.d("teszt3", "AddEszkoz fut");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");


        Map<String, Object> note = new HashMap<>();
       // note.put("Nev", eszk.nev);
        note.put("Azonosito",eszk.azonosito);
        note.put("Elhelyezkedes",eszk.elhelyezkedes);
        //note.put("Tipus",eszk.tipus);
        note.put("Leiras",eszk.leiras);
        note.put("Karbantartasi ido",eszk.karbantartasiido);
        note.put("Norm ido",eszk.normido);
        note.put("Instrukcio",eszk.instrukcio);


        CollectionReference tipusCollection = rootRef.collection("Kategoriak/"+ this.nev + "/" + eszk.tipus);


        tipusCollection.document(eszk.nev).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("teszt3", "Új eszköz( " + eszk.nev + " ) hozzáadva a " + nev + " kategóriához " + eszk.tipus + " tipussal");


                CollectionReference AlKategoria = rootRef.collection("Alkategoriak");
                Map<String, Object> lol = new HashMap<>();
                AlKategoria.document(eszk.tipus).set(lol);

                Loader.eszkozok.add(eszk);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("teszt3", "Nem sikerült az eszköz kategóriához rendelése!");
            }
        });
    }





     @Override
    public String toString() {
        return nev;
    }

}
