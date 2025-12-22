package main;
import java.io.Serializable;
import java.util.Random;
import main.dataStructures.ArrayList;

public class Veznedar implements Serializable {

    private String userName;
    private String ad;
    private String soyad;
    private String vPassword;
    private String tcNo;
    private String telNo;
    private int yetki;

    // ArrayList (Dinamik Dizi) veri yapısı: Tüm müşterileri dinamik olarak saklamak için kullanılıyor
    private static ArrayList<Musteri> musteriler = new ArrayList<>();

    // ArrayList (Dinamik Dizi) veri yapısı: Tüm veznedarları dinamik olarak saklamak için kullanılıyor
    private static ArrayList<Veznedar> veznedarlar = new ArrayList<>();

    Veznedar(String userName,String ad,String soyad,String vPassword,String tcNo,String telNo){
        this.userName=userName;
        this.ad=ad;
        this.soyad=soyad;
        this.vPassword=vPassword;
        this.tcNo=tcNo;
        this.telNo=telNo;
        this.yetki=2;
    }
    public String getUserName() {
        return userName;
    }
    public String getAd() {
        return ad;
    }
    public String getSoyad() {
        return soyad;
    }
    public String getVPassword() {
        return vPassword;
    }
    public String getTcNo() {
        return tcNo;
    }
    public String getTelNo() {
        return telNo;
    }

    public void setTellerId(String userName) {
        this.userName = userName;
    }
    public void setAd(String ad) {
        this.ad = ad;
    }
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    public void setVPassword(String vPassword) {
        this.vPassword = vPassword;
    }
    public void setTcNo(String tc) {
        this.tcNo = tc;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    
    public int getYetki() {
        return yetki;
    }
    
    public void setYetki(int yetki) {
        this.yetki = yetki;
    }

    private static Random rand = new Random();

    public static ArrayList<Musteri> getMusteriler() {
        return musteriler;
    }

    public static void setMusteriler(ArrayList<Musteri> musterilerList) {
        musteriler = musterilerList;
    }

    public static ArrayList<Veznedar> getVeznedarlar() {
        return veznedarlar;
    }

    public static void setVeznedarlar(ArrayList<Veznedar> veznedarlarList) {
        veznedarlar = veznedarlarList;
    }

    public static void veznedarEkle(String userName, String ad, String soyad, String vPassword,String tcNo,String telNo) {
        Veznedar v = new Veznedar(userName, ad, soyad, vPassword,tcNo,telNo);
        v.setYetki(2);
        veznedarlar.add(v);
    }
    public static void veznedarSil(String userName) {
        veznedarlar.removeIf(v -> v.getUserName() != null && v.getUserName().equals(userName));
    }

    public static Veznedar veznedarBul(String userName) {
        for(int i = 0; i < veznedarlar.size(); i++) {
            Veznedar v = veznedarlar.get(i);
            if(v.getUserName() != null && v.getUserName().equals(userName)) {
                return v;
            }
        }
        return null;
    }

    public static boolean veznedarIdKullaniliyor(String userName) {
        for(int i = 0; i < veznedarlar.size(); i++) {
            Veznedar v = veznedarlar.get(i);
            if(v.getUserName() != null && v.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void musteriEkle(Musteri m){
        int musteriId = m.getMusteriId();
        int vadesizHesapId = rastgeleHesapIdOlustur();
        int vadeliHesapId = rastgeleHesapIdOlustur();
        Hesap vadesizHesap = new Hesap(musteriId, vadesizHesapId, new HesapTuru("vadesiz"));
        m.mHesapAc(vadesizHesap);
        Hesap vadeliHesap = new Hesap(musteriId, vadeliHesapId, new HesapTuru("vadeli"));
        m.mHesapAc(vadeliHesap);
        musteriler.add(m);
    }
    private int rastgeleHesapIdOlustur() {
        int yeniId;
        do {
            yeniId = 100000 + rand.nextInt(990000);
        } while(hesapIdKullaniliyor(yeniId));
        return yeniId;
    }

    public static boolean musteriIdKullaniliyor(int musteriId) {
        for(int i = 0; i < musteriler.size(); i++) {
            Musteri m = musteriler.get(i);
            if(m.getMusteriId() == musteriId) {
                return true;
            }
        }
        return false;
    }

    public static boolean tcKimlikKullaniliyor(String tcKimlik) {
        if (tcKimlik == null || tcKimlik.trim().isEmpty()) {
            return false;
        }
        for(int i = 0; i < musteriler.size(); i++) {
            Musteri m = musteriler.get(i);
            if(m.getTCkimlik() != null && m.getTCkimlik().equals(tcKimlik)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hesapIdKullaniliyor(int hesapId) {
        for(int i = 0; i < musteriler.size(); i++) {
            Musteri m = musteriler.get(i);
            main.dataStructures.LinkedList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++) {
                Hesap h = hesaplar.get(j);
                if(h.getHesapId() == hesapId) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean musteriSil(int musteriId){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                
                for (int j = 0; j < m.getHesaplar().size(); j++) {
                    if (m.getHesaplar().get(j).getBakiyeInt() > 0) {
                        return false;
                    }
                }
                musteriler.removeAt(i);
                return true;
            }
        }
        return false;
    }
    public void vHesapAc(int musteriId){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                int rastgeleHesapId = rastgeleHesapIdOlustur();
                m.mHesapAc(new Hesap(musteriId, rastgeleHesapId, new HesapTuru("vadesiz")));
            }
        }
    }

    public void vVadeliHesapAc(int musteriId){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                int rastgeleHesapId = rastgeleHesapIdOlustur();
                m.mHesapAc(new Hesap(musteriId, rastgeleHesapId, new HesapTuru("vadeli")));
            }
        }
    }
    public void vHesapKapat(int musteriId,int hesapId){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                m.mHesapKapat(hesapId);
            }
        }
    }
    public void vParaYatir(int musteriId, int hesapId,int yatırılacak){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                m.paraYatir(hesapId,yatırılacak);
            }
        }
    }
    public void vParaCek(int musteriId, int hesapId,int cekilecek){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                m.paraCek(hesapId,cekilecek);
            }
        }
    }

}
