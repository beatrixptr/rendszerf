package com.example.karbantartasirendszer;

import java.util.ArrayList;

    public class EszkozList {

    public static ArrayList<Eszkoz> eszkozok = new ArrayList<Eszkoz>();
    public static ArrayList<Eszkoz> getEszkozok(){
        return eszkozok;
    }
    public static void setEszkozok(ArrayList<Eszkoz> eszk){

       for(int i = 1; i < eszk.size(); i++){
            eszkozok.add(eszk.get(i));
       }
    }

}
