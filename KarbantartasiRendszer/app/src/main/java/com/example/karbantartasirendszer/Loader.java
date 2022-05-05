package com.example.karbantartasirendszer;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class Loader {

    public static ArrayList<Kategoria> kategoriak = new ArrayList<Kategoria>();
    public static ArrayList<String> tipusok = new ArrayList<String>();
    public static ArrayList<String> vegzettsegek = new ArrayList<String>();
    public static ArrayList<Eszkoz> eszkozok = new ArrayList<Eszkoz>();
    public static ArrayList<String> felhasznalok = new ArrayList<String>();
    public static ArrayList<KarbantartoUser> karbantartok = new ArrayList<KarbantartoUser>();

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
                        ArrayList<String> szuksegesVegzettsegek = (ArrayList<String>) document.get("Vegzettsegek");
                        kategoriak.add(new Kategoria(document.getId().toString(), document.getString("normaido"), document.getString("periodus"), document.getString("instrukcio"), szuksegesVegzettsegek));
                    }

                    loadTipusok();
                    showKategVegz();
                }
            }
        });
    }

    public static void loadKarbantartok()
    {
        karbantartok.clear();
        karbantartok.add(new KarbantartoUser("Karbantartó...", null));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = rootRef.collection("Users");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String szerep = document.getString("role");
                        if(szerep.equals("karbantarto"))
                        {

                            ArrayList<String> karbVegzettsegek = (ArrayList<String>) document.get("Vegzettsegek");
                            karbantartok.add(new KarbantartoUser(document.getId().toString(), karbVegzettsegek));
                        }
                    }
                  //  showKarbantartok();

                }

            }
        });
    }

    public static void showKarbantartok()
    {

        for(int i=1;i<karbantartok.size(); i++)
        {
            Log.d("idkwhat", "nev: " + karbantartok.get(i).nev.toString() + "\n");
            for(int j=0; j<karbantartok.get(i).vegzettsegek.size(); j++)
            {
                Log.d("idkwhat", karbantartok.get(i).vegzettsegek.get(j).toString());
            }
        }
    }

    public static void showKategVegz()
    {
        Log.d("idkwhat", "lefut pedfig");
        for(int i=1;i<kategoriak.size(); i++)
        {
            Log.d("idkwhat", "nev: " + kategoriak.get(i).nev.toString() + "\n");
            for(int j=0; j<kategoriak.get(i).szuksegesVegzettsegek.size(); j++)
            {
                Log.d("idkwhat", kategoriak.get(i).szuksegesVegzettsegek.get(j).toString());
            }
        }
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

    public static void loadFelhasznalok()
    {
        felhasznalok.clear();
        felhasznalok.add("Név...");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Users = rootRef.collection("Users");
        Users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        felhasznalok.add(document.getId().toString());
                    }

                }
            }
        });
    }

    public static void loadKarbantartasiFeladatok() {
        Eszkoz eszkTemp = new Eszkoz("Válasszon...", "", "", "", "", "", "", "");
        KarbantartasKezelo.feladatok.clear();
        KarbantartasKezelo.feladatok.add(new KarbantartasiFeladat(eszkTemp, "", "", "", ""));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference karbantartasokReference = rootRef.collection("Karbantartasok");
        karbantartasokReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

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
                        Eszkoz eszkoz = new Eszkoz(eszkNev, kategoria, tipus, azonosito, elhelyezkedes, periodus, normaido, instrukcio);
                        String hiba = document.getString("hiba_leiras");
                        String idopont = document.getString("idopont");
                        String statusz = document.getString("statusz");
                        String karbTipus = document.getString("tipus");
                        KarbantartasiFeladat feladat = new KarbantartasiFeladat(eszkoz, karbTipus, hiba, statusz, idopont);

                        KarbantartasKezelo.feladatok.add(feladat);
                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public static void loadSajatFeladatok(String _karb){
        //KarbantartasKezelo.sajatFeladatok.clear();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference karbRef = rootRef.collection("Users").document(_karb);
        Eszkoz eszkTemp = new Eszkoz("Válasszon...", "", "", "", "", "", "", "");
        karbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                KarbantartasKezelo.sajatFeladatok = (ArrayList<KarbantartasiFeladat>) document.get("Feladatok");
                //KarbantartasKezelo.sajatFeladatok.add(0, new KarbantartasiFeladat(eszkTemp, "", "", "", ""));
                Log.d("sajatFelSize",String.valueOf(KarbantartasKezelo.sajatFeladatok.size()));
                //List<KarbantartasiFeladat> fel = (List<KarbantartasiFeladat>) document.get("Feladatok");

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
