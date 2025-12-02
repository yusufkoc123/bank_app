package main;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private int bankaId;
    private String bankaAdi;
    private String konum;
    static private List<Musteri> musteriler = new ArrayList<>();
    static private List<Veznedar> veznedarlar = new ArrayList<>();
    public Bank(int bankaId, String bankaAdi, String konum){
        this.bankaId=bankaId;
        this.bankaAdi=bankaAdi;
        this.konum=konum;



    }

    // GETTERS
    public int getBankaId() {
        return bankaId;
    }
    public String getBankaAdi() {
        return bankaAdi;
    }
    public String getKonum() {
        return konum;
    }
    public List<Musteri> getMusteriler() {
        return musteriler;
    }
    public static List<Musteri> getMusterilerStatic() {
        return musteriler;
    }
    // SETTERS
    public void setBankaId(int bankaId) {
        this.bankaId = bankaId;
    }
    public void setBankaAdi(String bankaAdi) {
        this.bankaAdi = bankaAdi;
    }
    public void setKonum(String konum) {
        this.konum = konum;
    }
    public void setMusteriler(List<Musteri> musteriler) {
        this.musteriler=musteriler;}

}
