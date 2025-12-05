package main;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Veznedar {
    private int tellerId;
    private String ad;
    private String soyad;
    private String vPassword;

    private static List<Musteri> musteriler = new ArrayList<>();

    private static List<Veznedar> veznedarlar = new ArrayList<>();

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

    public static List<Musteri> getMusteriler() {
        return musteriler;
    }

    public static void setMusteriler(List<Musteri> musterilerList) {
        musteriler = musterilerList;
    }

    public static List<Veznedar> getVeznedarlar() {
        return veznedarlar;
    }

    public static void setVeznedarlar(List<Veznedar> veznedarlarList) {
        veznedarlar = veznedarlarList;
    }

    public static void veznedarEkle(int tellerId, String ad, String soyad, String vPassword) {
        Veznedar v = new Veznedar(tellerId, ad, soyad, vPassword);
    }
    public static void veznedarSil(int tellerId) {
        veznedarlar.removeIf(v -> v.getTellerId() == tellerId);
    }

    public static Veznedar veznedarBul(int tellerId) {
        for(Veznedar v : veznedarlar) {
            if(v.getTellerId() == tellerId) {
                return v;
            }
        }
        return null;
    }

    public static boolean veznedarIdKullaniliyor(int tellerId) {
        for(Veznedar v : veznedarlar) {
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
        for(Musteri m : musteriler) {
            if(m.getMusteriId() == musteriId) {
                return true;
            }
        }
        return false;
    }

    public static boolean hesapIdKullaniliyor(int hesapId) {
        for(Musteri m : musteriler) {
            for(Hesap h : m.getHesaplar()) {
                if(h.getHesapId() == hesapId) {
                    return true;
                }
            }
        }
        return false;
    }
    public void musteriSil(int musteriId){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                musteriler.remove(m);
                break;
            }
        }
    }
    public void vHesapAc(int musteriId){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                int rastgeleHesapId = rastgeleHesapIdOlustur();
                m.mHesapAc(new Hesap(musteriId, rastgeleHesapId, new HesapTuru("vadesiz")));
            }
        }
    }

    public void vVadeliHesapAc(int musteriId){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                int rastgeleHesapId = rastgeleHesapIdOlustur();
                m.mHesapAc(new Hesap(musteriId, rastgeleHesapId, new HesapTuru("vadeli")));
            }
        }
    }
    public void vHesapKapat(int musteriId,int hesapId){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                m.mHesapKapat(hesapId);
            }
        }
    }
    public void vParaYatir(int musteriId, int hesapId,int yatırılacak){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                m.paraYatir(hesapId,yatırılacak);
            }
        }
    }
    public void vParaCek(int musteriId, int hesapId,int cekilecek){
        for(Musteri m : musteriler){
            if(m.getMusteriId()==musteriId){
                m.paraCek(hesapId,cekilecek);
            }
        }
    }
}
