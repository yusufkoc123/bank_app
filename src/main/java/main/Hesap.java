package main;

import java.util.Random;

public class Hesap {
    private int musteriId;
    private int hesapId;
    private int bakiye;
    Random rand = new Random();
    Hesap(int musteriId,int hesapId ) {
        this.hesapId = hesapId;
        this.musteriId = musteriId;
        this.bakiye = bakiye=0;

    }
    @Override
    public String toString() {
        return "Hesap[ID=" + hesapId + ", No=" + hesapId + ", Bakiye=" + bakiye + "]";
    }


    public int getBakiye() {
        return bakiye;
    }
    public int getHesapId() {
        return hesapId;
    }
    public int getMusteriId() {
        return musteriId;
    }


    public void setBakiye(int bakiye) {
        this.bakiye = bakiye;
    }
    public void setHesapId(int hesapId) {
        this.hesapId = hesapId;
    }
    public void setMusteriId(int musteriId) {
        this.musteriId = musteriId;
    }
}
