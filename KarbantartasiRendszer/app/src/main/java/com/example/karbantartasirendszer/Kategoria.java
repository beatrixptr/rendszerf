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
    public String normaido;
    public String periodus;
    public String instrukcio;
    public ArrayList<String> szuksegesVegzettsegek = new ArrayList<String>();

    public Kategoria(String _nev, String _normaido, String _periodus, String _instrukcio)
    {
        nev = _nev;
        normaido = _normaido;
        periodus = _periodus;
        instrukcio = _instrukcio;
    }

    public Kategoria(String _nev, String _normaido, String _periodus, String _instrukcio, ArrayList<String> _vegzettsegek)
    {
        nev = _nev;
        normaido = _normaido;
        periodus = _periodus;
        instrukcio = _instrukcio;
        szuksegesVegzettsegek = _vegzettsegek;
    }

    public void addNewVegzettseg(String uj)
    {
        szuksegesVegzettsegek.add(uj);
    }

    public void AddEszkoz(Eszkoz eszk, String selectedPeriodus)
    {
        if(eszk.tipus == null || eszk.tipus.isEmpty()) return;
        Log.d("teszt3", "AddEszkoz fut");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");

        String actualPeriod = selectedPeriodus == null ?this.periodus:eszk.periodus;
        String actualNorma = eszk.normaido.isEmpty()?this.normaido:eszk.normaido;
        String actualInstrukcio = eszk.instrukcio.isEmpty()?this.instrukcio:eszk.instrukcio;

        Map<String, Object> note = new HashMap<>();
       // note.put("Nev", eszk.nev);
        note.put("kategoria", eszk.kategoria);
        note.put("tipus", eszk.tipus);
        note.put("azonosito",eszk.azonosito);
        note.put("elhelyezkedes",eszk.elhelyezkedes);
        note.put("periodus", actualPeriod);
        note.put("normaido",actualNorma);
        note.put("instrukcio",actualInstrukcio);


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
