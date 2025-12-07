package main;
import java.io.Serializable;
import java.util.Random;
import main.dataStructures.ArrayList;

public class Veznedar implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tellerId;
    private String ad;
    private String soyad;
    private String vPassword;

    private static ArrayList<Musteri> musteriler = new ArrayList<>();

    private static ArrayList<Veznedar> veznedarlar = new ArrayList<>();

    Veznedar(int tellerId,String ad,String soyad,String vPassword){
        this.tellerId=tellerId;
        this.ad=ad;
        this.soyad=soyad;
        this.vPassword=vPassword;
    }
    public int getTellerId() {
        return tellerId;
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

    public void setTellerId(int tellerId) {
        this.tellerId = tellerId;
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

    public static void veznedarEkle(int tellerId, String ad, String soyad, String vPassword) {
        Veznedar v = new Veznedar(tellerId, ad, soyad, vPassword);
        veznedarlar.add(v);
    }
    public static void veznedarSil(int tellerId) {
        veznedarlar.removeIf(v -> v.getTellerId() == tellerId);
    }

    public static Veznedar veznedarBul(int tellerId) {
        for(int i = 0; i < veznedarlar.size(); i++) {
            Veznedar v = veznedarlar.get(i);
            if(v.getTellerId() == tellerId) {
                return v;
            }
        }
        return null;
    }

    public static boolean veznedarIdKullaniliyor(int tellerId) {
        for(int i = 0; i < veznedarlar.size(); i++) {
            Veznedar v = veznedarlar.get(i);
            if(v.getTellerId() == tellerId) {
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
            yeniId = 10000 + rand.nextInt(990000); // 10000 ile 999999 arası
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

    public static boolean hesapIdKullaniliyor(int hesapId) {
        for(int i = 0; i < musteriler.size(); i++) {
            Musteri m = musteriler.get(i);
            ArrayList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++) {
                Hesap h = hesaplar.get(j);
                if(h.getHesapId() == hesapId) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void musteriSil(int musteriId){
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            if(m.getMusteriId()==musteriId){
                musteriler.removeAt(i);
                break;
            }
        }
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
