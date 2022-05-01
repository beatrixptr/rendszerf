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

    public static int asd = 0;
    public static void eszkoz_allapotanak_ellenorzese(){

       for(int i=1; i<Loader.eszkozok.size(); i++)
       {
           Log.d("teszt11", "lefut");
               check(Loader.eszkozok.get(i));


        }

    }

    public static KarbantartasiFeladat getEszkozFeladat(Eszkoz eszk)
    {
        for(int i=1; i<KarbantartasKezelo.feladatok.size(); i++)
        {
            if(eszk.nev.equals(KarbantartasKezelo.feladatok.get(i).eszkoz.nev) && eszk.tipus.equals(KarbantartasKezelo.feladatok.get(i).eszkoz.tipus) && eszk.kategoria.equals( KarbantartasKezelo.feladatok.get(i).eszkoz.kategoria))
                return KarbantartasKezelo.feladatok.get(i);
        }
        return null;
    }

    private static void check(Eszkoz eszk){
        Log.d("teszt11", "lefut");
        KarbantartasiFeladat kar = getEszkozFeladat(eszk);
        if(kar != null)
        {
            Log.d("teszt11", "van ilyen kar");
             if(kar.statusz.equals("Befejezve") && kar.tipus.equals("Idoszakos")){

                String[] reszek = kar.idopont.split("-");
                int nap = Integer.parseInt(reszek[1]);
                int honap = Integer.parseInt(reszek[2]);
                int ev = Integer.parseInt(reszek[3]);

                switch (eszk.periodus) {
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


                KarbantartasiFeladat ujFeladat = new KarbantartasiFeladat(kar.eszkoz, kar.tipus, kar.hiba_leiras, "Utemezetlen", ujido);
                ujFeladat.addKarbantartasiFeladat();

                deleteRegi(kar);

            }
        }

    }

    public static void deleteRegi(KarbantartasiFeladat regi)
    {
        for(int i=1; i<KarbantartasKezelo.feladatok.size(); i++)
        {
            if(KarbantartasKezelo.feladatok.get(i) == regi)
            {
                KarbantartasKezelo.feladatok.remove(i);
            }
        }

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Karbantartas = rootRef.collection("Karbantartasok");
        String feladatNev = regi.eszkoz.nev + "-" + regi.idopont;
        Karbantartas.document(feladatNev).delete();

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


}
