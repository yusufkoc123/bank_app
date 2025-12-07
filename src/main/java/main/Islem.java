package main;

import java.io.Serializable;
import java.time.LocalDate;

public class Islem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String gondericiAdi;
    private String aliciAdi;
    private int miktar;
    private LocalDate tarih;
    private String mesaj;
    private String islemTuru; // "Para Yatırma", "Para Çekme", "Para Transferi"

    public Islem(String gondericiAdi, String aliciAdi, int miktar, LocalDate tarih, String mesaj, String islemTuru) {
        this.gondericiAdi = gondericiAdi;
        this.aliciAdi = aliciAdi;
        this.miktar = miktar;
        this.tarih = tarih;
        this.mesaj = mesaj;
        this.islemTuru = islemTuru;
    }

    public String getGondericiAdi() {
        return gondericiAdi;
    }

    public String getAliciAdi() {
        return aliciAdi;
    }

    public int getMiktar() {
        return miktar;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public String getMesaj() {
        return mesaj;
    }

    public String getIslemTuru() {
        return islemTuru;
    }

    public void setGondericiAdi(String gondericiAdi) {
        this.gondericiAdi = gondericiAdi;
    }

    public void setAliciAdi(String aliciAdi) {
        this.aliciAdi = aliciAdi;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }
}

