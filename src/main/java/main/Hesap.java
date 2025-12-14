package main;

import java.io.Serializable;
import java.util.Random;

public class Hesap implements Serializable {
    private static final long serialVersionUID = 1L;
    private int musteriId;
    private int hesapId;
    private String bakiye;
    private HesapTuru hesapTuru;
    Random rand = new Random();

    public Hesap(int musteriId, int hesapId, HesapTuru hesapTuru) {
        this.hesapId = hesapId;
        this.musteriId = musteriId;
        this.bakiye = "0";
        this.hesapTuru = hesapTuru;
    }

    public Hesap(int musteriId, int hesapId) {
        this(musteriId, hesapId, new HesapTuru("vadesiz"));
    }
    @Override
    public String toString() {
        return "Hesap[ID=" + hesapId + ", No=" + hesapId + ", Bakiye=" + bakiye + "]";
    }


    public String getBakiye() {
        return bakiye;
    }
    
    public int getBakiyeInt() {
        try {
            return Integer.parseInt(bakiye);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public int getHesapId() {
        return hesapId;
    }
    public int getMusteriId() {
        return musteriId;
    }


    public void setBakiye(String bakiye) {
        this.bakiye = bakiye;
    }
    
    public void setBakiye(int bakiye) {
        this.bakiye = String.valueOf(bakiye);
    }
    public void setHesapId(int hesapId) {
        this.hesapId = hesapId;
    }
    public void setMusteriId(int musteriId) {
        this.musteriId = musteriId;
    }

    public HesapTuru getHesapTuru() {
        return hesapTuru;
    }

    public void setHesapTuru(HesapTuru hesapTuru) {
        this.hesapTuru = hesapTuru;
    }
}
