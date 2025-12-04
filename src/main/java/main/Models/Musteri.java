package main.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Musteri {
    private final StringProperty isim;
    private final StringProperty soyisim;
    private final StringProperty telno;
    private final StringProperty musterID;
    private final StringProperty tcno;
    private final StringProperty kayittarih;

    // Constructor
    public Musteri() {
        this.isim = new SimpleStringProperty("");
        this.soyisim = new SimpleStringProperty("");
        this.telno = new SimpleStringProperty("");
        this.musterID = new SimpleStringProperty("");
        this.tcno = new SimpleStringProperty("");
        this.kayittarih = new SimpleStringProperty("");
    }

    public Musteri(String isim, String soyisim, String telno, String musterID, String tcno, String kayittarih) {
        this.isim = new SimpleStringProperty(isim);
        this.soyisim = new SimpleStringProperty(soyisim);
        this.telno = new SimpleStringProperty(telno);
        this.musterID = new SimpleStringProperty(musterID);
        this.tcno = new SimpleStringProperty(tcno);
        this.kayittarih = new SimpleStringProperty(kayittarih);
    }

    // Property getters (for JavaFX binding)
    public StringProperty isimProperty() {
        return isim;
    }

    public StringProperty soyisimProperty() {
        return soyisim;
    }

    public StringProperty telnoProperty() {
        return telno;
    }

    public StringProperty musterIDProperty() {
        return musterID;
    }

    public StringProperty tcnoProperty() {
        return tcno;
    }

    public StringProperty kayittarihProperty() {
        return kayittarih;
    }

    // Getters
    public String getIsim() {
        return isim.get();
    }

    public String getSoyisim() {
        return soyisim.get();
    }

    public String getTelno() {
        return telno.get();
    }

    public String getMusterID() {
        return musterID.get();
    }

    public String getTcno() {
        return tcno.get();
    }

    public String getKayittarih() {
        return kayittarih.get();
    }

    // Setters
    public void setIsim(String isim) {
        this.isim.set(isim);
    }

    public void setSoyisim(String soyisim) {
        this.soyisim.set(soyisim);
    }

    public void setTelno(String telno) {
        this.telno.set(telno);
    }

    public void setMusterID(String musterID) {
        this.musterID.set(musterID);
    }

    public void setTcno(String tcno) {
        this.tcno.set(tcno);
    }

    public void setKayittarih(String kayittarih) {
        this.kayittarih.set(kayittarih);
    }

    // main.Musteri'den Models.Musteri'ye dönüştürme metodu
    public static Musteri fromDomainModel(main.Musteri domainMusteri) {
        if (domainMusteri == null) {
            return null;
        }
        return new Musteri(
            domainMusteri.getAdi(),
            domainMusteri.getSoyad(),
            String.valueOf(domainMusteri.getTelNo()),
            String.valueOf(domainMusteri.getMusteriId()),
            domainMusteri.getTCkimlik(),
            "" // Kayıt tarihi şimdilik boş, gerekirse eklenebilir
        );
    }
}
