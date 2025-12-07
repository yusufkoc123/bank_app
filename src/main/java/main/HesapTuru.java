package main;

import java.io.Serializable;

public class HesapTuru implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean tur;
    private double vadeMiktari;
    private String hesapTuru;

    public HesapTuru(String hesapTuru) {
        this.hesapTuru = hesapTuru;
        this.tur = hesapTuru.equals("vadeli") ? true : false;
        this.vadeMiktari = hesapTuru.equals("vadeli") ? 2.5 : 0;
    }

    public boolean getTur() {
        return tur;
    }

    public double getVadeMiktari() {
        return vadeMiktari;
    }

    public String getHesapTuru() {
        return hesapTuru;
    }

}
