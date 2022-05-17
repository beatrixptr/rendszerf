package com.example.karbantartasirendszer;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KarbantartoUser {
    public String nev;
    public ArrayList<String> vegzettsegek;
    public ArrayList<KarbantartasiFeladat> sajatFeladatok = new ArrayList<KarbantartasiFeladat>();

    public KarbantartoUser(String _nev, ArrayList<String> _vegzettsegek)
    {
        nev = _nev;
        vegzettsegek = _vegzettsegek;
    }

    public void addFeladat(KarbantartasiFeladat feladat)
    {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference UserKarb = rootRef.collection("Users");

        Map<String, Object> note = new HashMap<>();

        feladat.UpdateStatusz("Kiosztva - " + nev);
        UserKarb.document(nev).set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UserKarb.document(nev).update("Feladatok", FieldValue.arrayUnion(feladat)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       // feladat.UpdateStatusz("Kiosztva - " + nev);
                    }
                });

            }

        });



    }



    @Override
    public String toString() {
        return nev;
    }
}

