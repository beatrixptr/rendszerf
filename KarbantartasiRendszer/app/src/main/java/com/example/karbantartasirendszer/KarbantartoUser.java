package com.example.karbantartasirendszer;

import java.util.ArrayList;

public class KarbantartoUser {
    public String nev;
    public ArrayList<String> vegzettsegek;

    public KarbantartoUser(String _nev, ArrayList<String> _vegzettsegek)
    {
        nev = _nev;
        vegzettsegek = _vegzettsegek;
    }
}
