package com.example.karbantartasirendszer;

public class Eszkoz {

    public String nev;
    public String azonosito;
    public String elhelyezkedes;
    public String tipus;
    public String kategoria;
    public String periodus;
    public String normaido;
    public String instrukcio;

    public Eszkoz(String Nev, String Kat, String Tipus, String Azonosito, String Elhelyezkedes, String Periodus,String Normaido,String Instrukcio) {
        this.nev = Nev;
        this.kategoria = Kat;
        this.azonosito = Azonosito;
        this.elhelyezkedes = Elhelyezkedes;
        this.tipus = Tipus;
        this.periodus = Periodus;
        this.normaido = Normaido;
        this.instrukcio= Instrukcio;
    }


    @Override
    public String toString() {
        return nev;
    }


}
