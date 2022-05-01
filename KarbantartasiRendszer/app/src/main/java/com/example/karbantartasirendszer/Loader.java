package com.example.karbantartasirendszer;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;

public class Loader {

    public static ArrayList<Kategoria> kategoriak = new ArrayList<Kategoria>();
    public static ArrayList<String> tipusok = new ArrayList<String>();
    public static ArrayList<String> vegzettsegek = new ArrayList<String>();
    public static ArrayList<Eszkoz> eszkozok = new ArrayList<Eszkoz>();

    public static void loadKategoriak()
    {
        kategoriak.clear();
        kategoriak.add(new Kategoria("Kategória...", "", "", ""));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Kategoriak = rootRef.collection("Kategoriak");
        Kategoriak.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        kategoriak.add(new Kategoria(document.getId().toString(), document.getString("normaido"), document.getString("periodus"), document.getString("instrukcio")));
                    }

                    loadTipusok();
                }
            }
        });
    }

    private static void loadTipusok()
    {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Tipusok = rootRef.collection("Alkategoriak");
        Tipusok.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        tipusok.add(document.getId());
                    }
                }
                loadOsszesEszkozok();
            }
        });
    }


    public static void loadVegzettsegek()
    {
        vegzettsegek.clear();
        vegzettsegek.add("Végzettség...");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Vegzettsegek = rootRef.collection("Vegzettsegek");
        Vegzettsegek.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        vegzettsegek.add(document.getId().toString());
                    }

                }
            }
        });
    }

    public static void loadOsszesEszkozok()
    {
        eszkozok.clear();
        eszkozok.add(new Eszkoz("Eszközök...", "", "", "", "", "", "", ""));
        for(int i=1; i<kategoriak.size(); i++)
        {
            for(int j=0; j<tipusok.size(); j++)
            {
                loadEszkozByKategoria(kategoriak.get(i), tipusok.get(j));
            }

        }

    }

    private static void loadEszkozByKategoria(Kategoria kat, String tipus)
    {

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference EszkozokByKategoria = rootRef.collection("Kategoriak/" + kat.nev + "/" + tipus);
        EszkozokByKategoria.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    //String Nev, String Kat, String Tipus, String Azonosito, String Elhelyezkedes, String Periodus,String Normaido,String Instru
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        eszkozok.add(new Eszkoz(document.getId(),kat.nev, document.getString("tipus"), document.getString("azonosito"), document.getString("elhelyezkedes"), document.getString("periodus"), document.getString("normaido"), document.getString("instrukcio")));
                    }
                }
                //  showEszkozok();
            }
        });

    }

    public static void showEszkozok()
    {
        Log.d("teszt4", "eszközök size:" + eszkozok.size());
        for(int i=1; i<eszkozok.size(); i++)
        {
            Log.d("teszt4", "Név: " + eszkozok.get(i).nev + " Kategoria: " + eszkozok.get(i).kategoria + " Tipus: " + eszkozok.get(i).tipus + " Norma: " + eszkozok.get(i).normaido);
        }
    }


}
