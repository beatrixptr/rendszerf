package com.example.karbantartasirendszer;

public class Felhasznalo {
    private String nev;
    private String jelszo;
    private String szerep;


    public Felhasznalo(String nev, String jelszo, String szerep) {
        this.nev = nev;
        this.jelszo = jelszo;
        this.szerep = szerep;

    }

    public String getNev() {
        return nev;
    }

    public String getJelszo() {
        return jelszo;
    }

    public String getSzerep() {
        return szerep;
    }


}
