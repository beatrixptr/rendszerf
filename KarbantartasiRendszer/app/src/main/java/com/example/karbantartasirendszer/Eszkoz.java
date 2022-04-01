package com.example.karbantartasirendszer;

public class Eszkoz {

    private String ID;
    private String kategoria;
    private String kepesites;
    private String karbantartasiIdointervallum;
    private String normaIdo;
    private String instrukciok;

    public Eszkoz(String ID, String kategoria, String kepesites, String karbantartasiIdointervallum, String normaIdo, String instrukciok) {
        this.ID = ID;
        this.kategoria = kategoria;
        this.kepesites = kepesites;
        this.karbantartasiIdointervallum = karbantartasiIdointervallum;
        this.normaIdo = normaIdo;
        this.instrukciok = instrukciok;
    }


}
