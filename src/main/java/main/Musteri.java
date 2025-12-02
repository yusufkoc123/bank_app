package main;

import java.util.List;
import java.util.ArrayList;
public class Musteri {
    private int musteriId;
    private String adi;
    private String soyad;
    private  String TCkimlik;
    private String adres;
    private int telNo;
    private String mPassword;
    private List<Hesap> hesaplar;
    Musteri(int musteriId,String adi,String soyad,String TCkimlik ,String adres,int telNo,String mPassword){
        this.musteriId=musteriId;
        this.adi=adi;
        this.soyad=soyad;
        this.TCkimlik=TCkimlik;
        this.adres=adres;
        this.telNo=telNo;
        this.mPassword=mPassword;
        this.hesaplar = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "Musteri {" +
                "musteriId=" + musteriId +
                ", adi='" + adi + '\'' +
                ", soyad='" + soyad + '\'' +
                ", TCkimlik='" + TCkimlik + '\'' +
                ", adres='" + adres + '\'' +
                ", telNo=" + telNo +
                ", mPassword='" + mPassword + '\'' +
                ", hesaplar=" + hesaplar +
                '}';
    }

    //GETTERS
    public String getAdi() {
        return adi;
    }
    public String getSoyad() {
        return soyad;
    }
    public String getTCkimlik() {
        return TCkimlik;
    }
    public String getAdres() {
        return adres;
    }
    public int getTelNo() {
        return telNo;
    }
    public String getMPassword() {
        return mPassword;
    }
    public int getMusteriId(){
        return musteriId;
    }
    public List<Hesap> getHesaplar() {
        return hesaplar;
    }
    //SETTERS
    public void setMusteriId(int musteriId) {
        this.musteriId = musteriId;
    }
    public void setAdi(String adi) {
        this.adi = adi;
    }
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    public void setTCkimlik(String TCkimlik) {
        this.TCkimlik = TCkimlik;
    }
    public void setAdres(String adres) {
        this.adres = adres;
    }
    public void setTelNo(int telNo) {
        this.telNo = telNo;
    }
    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }
    public void setHesaplar(List<Hesap> hesaplar) {
        this.hesaplar = hesaplar;
    }

    public void mHesapAc(Hesap h) {
        hesaplar.add(h);}
    public void mHesapKapat(int hesapId) {
        hesaplar.removeIf(h -> h.getHesapId() == hesapId);}
    public void paraYatir(int hesapId,int yatırılacakPara){
        List<Hesap> hesaplar=this.getHesaplar();
        for(Hesap h:hesaplar){
            if(h.getHesapId()==hesapId){
                h.setBakiye(h.getBakiye()+yatırılacakPara);
            }
        }
    }
    public void paraCek(int hesapId,int cekilecekPara){
        List<Hesap> hesaplar=this.getHesaplar();
        for(Hesap h:hesaplar){
            if(h.getHesapId()==hesapId){
                h.setBakiye(h.getBakiye()-cekilecekPara);
            }
        }
    }
    public double toplamBakiye(){
        List<Hesap> hesaplar=this.getHesaplar();
        double toplamBakiye=0;
        for(Hesap h:hesaplar){
            toplamBakiye+=h.getBakiye();
        }
        return toplamBakiye;
    }
    public boolean paraGonder(int gonderilenHesapId, int gonderilecekHesapId, int miktar){
        // Gönderen hesabı bul ve bakiyeyi kontrol et
        Hesap gonderilenHesap = null;
        for(Hesap h : this.hesaplar){
            if(h.getHesapId() == gonderilenHesapId){
                gonderilenHesap = h;
                break;
            }
        }

        if(gonderilenHesap == null){
            return false; // Gönderen hesap bulunamadı
        }

        if(gonderilenHesap.getBakiye() < miktar){
            return false; // Yetersiz bakiye
        }

        // Alıcı hesabı Bank sınıfındaki müşteriler listesinden bul
        List<Musteri> musteriler = Bank.getMusterilerStatic();
        if(musteriler == null){
            return false; // Müşteriler listesi bulunamadı
        }

        Hesap gonderilecekHesap = null;
        for(Musteri m : musteriler){
            for(Hesap h : m.getHesaplar()){
                if(h.getHesapId() == gonderilecekHesapId){
                    gonderilecekHesap = h;
                    break;
                }
            }
            if(gonderilecekHesap != null){
                break;
            }
        }

        if(gonderilecekHesap == null){
            return false;
        }

        // Para transferi yap
        gonderilenHesap.setBakiye(gonderilenHesap.getBakiye() - miktar);
        gonderilecekHesap.setBakiye(gonderilecekHesap.getBakiye() + miktar);

        return true; // Transfer başarılı
    }



    }
