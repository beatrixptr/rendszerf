package com.example.karbantartasirendszer;

public class Eszkoz {

    public String nev;
    public String azonosito;
    public String elhelyezkedes;
    public String tipus;
    public String leiras;
    public String karbantartasiido;
    public String normido;
    public String instrukcio;

    public Eszkoz(String Azonosito, String Elhelyezkedes, String Tipus ,String Leiras, String Karbantartasiido,String Normido,String Instrukcio) {

        this.azonosito = Azonosito;
        this.elhelyezkedes = Elhelyezkedes;
        this.tipus = Tipus;
        this.leiras = Leiras;
        this.karbantartasiido = Karbantartasiido;
        this.normido = Normido;
        this.instrukcio= Instrukcio;
    }

    public Eszkoz(String Nev, String Tipus ) {
        this.nev = Nev;
        this.azonosito = "";
        this.elhelyezkedes = "";
        this.tipus = Tipus;
        this.leiras = "";
        this.karbantartasiido = "";
        this.normido = "";
        this.instrukcio= "";
    }


}
