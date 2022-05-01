package com.example.karbantartasirendszer;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KarbantartasiFeladat {
    public String feladatnev;
    public Eszkoz eszkoz;
    public String tipus;
    public String hiba_leiras;
    public String idopont;
    public String statusz;


    public KarbantartasiFeladat(Eszkoz _eszkoz, String _tipus, String _hiba_leiras, String _statusz, String _idopont)
    {

        eszkoz = _eszkoz;
        tipus = _tipus;
        hiba_leiras = _hiba_leiras;
        idopont = _idopont; //String.valueOf(new Date().getTime());
        statusz = _statusz;

        //addKarbantartasiFeladat();
    }
    public KarbantartasiFeladat(Eszkoz _eszkoz, String _tipus, String _hiba_leiras, String _statusz)
    {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        eszkoz = _eszkoz;
        tipus = _tipus;
        hiba_leiras = _hiba_leiras;
        idopont = currentTime + "-" + currentDate; //String.valueOf(new Date().getTime());
        statusz = _statusz;

        addKarbantartasiFeladat();
    }

    public void addKarbantartasiFeladat()
    {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Karbantartas = rootRef.collection("Karbantartasok");

       String feladatNev = eszkoz.nev + "-" + idopont;

        Map<String, Object> note = new HashMap<>();
        note.put("Eszkoz", eszkoz);
        note.put("tipus", tipus);
        note.put("hiba_leiras", hiba_leiras);
        note.put("idopont", idopont);
        note.put("statusz", statusz);

        Karbantartas.document(feladatNev).set(note);
    }


    @Override
    public String toString(){
        return eszkoz.toString();
    }
}
