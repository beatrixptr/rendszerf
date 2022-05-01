package com.example.karbantartasirendszer;


import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class statuscheck {
    public static ArrayList<KarbantartasiFeladat> feladatok = null;
    public static ArrayList<Eszkoz> eszkozok = null;
    public static int asd = 0;
    public static void eszkoz_allapotanak_ellenorzese(){



            laodeszk();

            feladatok = KarbantartasKezelo.getFeladatok();
            eszkozok = EszkozList.eszkozok;

            Log.d("eszkSTATUS",Integer.toString(eszkozok.size()));
            Log.d("KARB",Integer.toString(feladatok.size()));


                    for (Eszkoz value: eszkozok) {
                        Log.d("ASd","EszkozForeach");
                        //try{
                            String a =value.getPeriodus();
                            String b =value.getKategoria();
                            String c =value.getNev_eszkoz();
                            ArrayList<KarbantartasiFeladat> g = feladatok;
                            Eszkoz d = value;
                            check(d,g);
                        /*}catch (NullPointerException i){
                            Log.d("23123", "valamihiba ");
                        }*/

                    }





    }



    private static void check(Eszkoz eszk,ArrayList<KarbantartasiFeladat> feladatok){

        Log.d("ASd","Check");
        int a = 0;
        Log.d("ASd","kar-nev-ifEA");
        Log.d("asd23", feladatok.get(1).getStatusz());
        for (KarbantartasiFeladat kar : feladatok) {
            Eszkoz dk = kar.getEszkoz();
            Log.d("ASd","LEFUTTATJA");
            String nev = kar.eszkoz.nev; // dk.getNev_eszkoz() -> ez sem jo
            Log.d("ASd","LEFUTTATJA");
            String kat = dk.kategoria; // dk.getKategoria()-> ez sem jo
            Log.d("ASd","LEFUTTATJA");

            Log.d("ASd","kar-nev-ifE");
            if( nev.equals(eszk.getNev_eszkoz()) && kat.equals(eszk.getKategoria())){
                a = 1;
                Log.d("ASd","kar-nev-if");
                if(kar.getStatusz().equals("befejezve")){

                    String[] reszek = kar.getIdopont().split("-");
                    int nap = Integer.parseInt(reszek[1]);
                    int honap = Integer.parseInt(reszek[2]);
                    int ev = Integer.parseInt(reszek[3]);

                    switch (eszk.getPeriodus()) {
                        case "Hetente":
                            nap += 7;
                            if (nap >= 29 && honap == 2) {
                                if(ev % 4 == 0 || ( ev % 100 == 0 && ev % 400 == 0 ) ) {
                                    if(nap > 29) { nap = nap - 29; honap++; }
                                }else{
                                    nap = nap - 28;
                                    honap++;
                                }

                            } else if (nap >= 31 && (honap == 4 || honap == 6 || honap == 9 || honap == 11)) {
                                nap = nap - 30;
                                honap++;
                            } else if (nap >= 32 && (honap == 1 || honap == 3 || honap == 5 || honap == 7 || honap == 8 || honap == 10)){
                                nap = nap - 31;
                                honap++;
                            } else if(nap >= 32 && honap==12){
                                nap = nap - 31;
                                honap=1;
                                ev++;
                            }

                            break;
                        case "Havonta":
                            honap += 1;
                            if(honap==13){
                                honap=1;
                            }
                            break;
                        case "Negyedevente":
                            honap += 3;
                            if(honap>=13){
                                honap=honap-12;
                            }
                            break;
                        case "Evente":
                            ev++;
                            break;
                        default:
                            break;
                    }
                    Log.d("ASd","sw-utan");

                    String ujido = reszek[0] + "-" + nap + "-" + honap + "-" + ev;

                    addKarbantartasiFeladat(kar.getEszkoz(), kar.getTipus(),kar.getHiba_leiras(),ujido);
                }
            }
        }

        /*if(a==0){
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String idos = currentTime + "-" + currentDate;
            addKarbantartasiFeladat(eszk, eszk.getTipus(),"Automatikusan generalt->Altalanos karbantartas szukseges",idos);
        }*/

    }

    private static void addKarbantartasiFeladat(Eszkoz eszkoz, String tipus, String hiba_leiras, String idopont)
    {
        Log.d("ASd","addkarbantarto");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Karbantartas = rootRef.collection("Karbantartasok");

        String feladatNev = eszkoz.nev + "-" + idopont;

        Map<String, Object> note = new HashMap<>();
        note.put("Eszkoz", eszkoz);
        note.put("tipus", tipus);
        note.put("hiba_leiras", hiba_leiras);
        note.put("idopont", idopont);
        note.put("statusz", "Utemezetlen");

        Karbantartas.document(feladatNev).set(note);
    }

    private static void laodeszk()
    {
        ArrayList<Eszkoz> eszkozok = new ArrayList<Eszkoz>();


        for(int i=0; i<Loader.eszkozok.size(); i++)
        {
            EszkozList.eszkozok.add(Loader.eszkozok.get(i));

        }

    }
}
