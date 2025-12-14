package main;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.time.LocalDate;
import main.dataStructures.Queue;
import main.dataStructures.ArrayList;
public class Musteri implements Serializable {
    private static final long serialVersionUID = 1L;
    private int musteriId;
    private String adi;
    private String soyad;
    private  String TCkimlik;
    private String adres;
    private String telNo;
    private String mPassword;
    private LocalDate kayitTarihi;
    private ArrayList<Hesap> hesaplar;
    private Queue<Islem> islemler;
    private static final int MAX_ISLEM_SAYISI = 20;
    private static final int GUNLUK_ISLEM_LIMITI = 100000;
    private static Random rand = new Random();

    public Musteri(String adi, String soyad, String TCkimlik, String adres, String telNo, String mPassword) {
        this.musteriId = rastgeleMusteriIdOlustur();
        this.adi = adi;
        this.soyad = soyad;
        this.TCkimlik = TCkimlik;
        this.adres = adres;
        this.telNo = telNo;
        this.mPassword = mPassword;
        this.kayitTarihi = LocalDate.now();
        this.hesaplar = new ArrayList<>();
        this.islemler = new Queue<>();
    }




    private int rastgeleMusteriIdOlustur() {
        int yeniId;
        do {
            yeniId = 1000 + rand.nextInt(999000);
        } while(Veznedar.musteriIdKullaniliyor(yeniId));
        return yeniId;
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
    public String getTelNo() {
        return telNo;
    }
    public String getMPassword() {
        return mPassword;
    }
    public int getMusteriId(){
        return musteriId;
    }
    public ArrayList<Hesap> getHesaplar() {
        return hesaplar;
    }
    public ArrayList<Islem> getIslemler() {
        return islemler.toArrayList();
    }
    
    public void islemEkle(Islem islem) {
        if (islem == null) {
            return;
        }
        islemler.offer(islem);
        while (islemler.size() > MAX_ISLEM_SAYISI) {
            islemler.poll();
        }
    }
    
    public void islemleriTemizle() {
        islemler = new Queue<>();
    }
    
    public int getGunlukIslemToplami() {
        int toplam = 0;
        LocalDate bugun = LocalDate.now();
        ArrayList<Islem> islemlerList = getIslemler();
        
        for (int i = 0; i < islemlerList.size(); i++) {
            Islem islem = islemlerList.get(i);
            if (islem != null && islem.getTarih() != null && islem.getTarih().equals(bugun)) {
                String islemTuru = islem.getIslemTuru();
                // Para Çekme ve Para Transferi (gönderen olarak) işlemlerini say
                if ("Para Çekme".equals(islemTuru)) {
                    toplam += islem.getMiktar();
                } else if ("Para Transferi".equals(islemTuru)) {
                    // Sadece gönderen olarak yapılan transferleri say
                    String musteriAdi = this.adi + " " + this.soyad;
                    if (musteriAdi.equals(islem.getGondericiAdi())) {
                        toplam += islem.getMiktar();
                    }
                }
            }
        }
        return toplam;
    }
    
    public static int getGunlukIslemLimiti() {
        return GUNLUK_ISLEM_LIMITI;
    }
    
    public int getKalanGunlukIslemLimiti() {
        return GUNLUK_ISLEM_LIMITI - getGunlukIslemToplami();
    }
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
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }
    public void setHesaplar(ArrayList<Hesap> hesaplar) {
        this.hesaplar = hesaplar;
    }
    
    public LocalDate getKayitTarihi() {
        return kayitTarihi;
    }
    
    public void setKayitTarihi(LocalDate kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }



    public void mHesapAc(Hesap h) {
        if (h != null &&
                h.getHesapTuru() != null &&
                "vadeli".equals(h.getHesapTuru().getHesapTuru())) {
            for (int i = 0; i < hesaplar.size(); i++) {
                Hesap mevcutHesap = hesaplar.get(i);
                if (mevcutHesap.getHesapTuru() != null &&
                        "vadeli".equals(mevcutHesap.getHesapTuru().getHesapTuru())) {
                    return;
                }
            }
        }
        hesaplar.add(h);
    }

    public void mHesapAcVadesiz() {
        int rastgeleHesapId = rastgeleHesapIdOlustur();
        Hesap yeniHesap = new Hesap(this.musteriId, rastgeleHesapId, new HesapTuru("vadesiz"));
        hesaplar.add(yeniHesap);
    }

    public void mHesapAcVadeli() {
        int rastgeleHesapId = rastgeleHesapIdOlustur();
        Hesap yeniHesap = new Hesap(this.musteriId, rastgeleHesapId, new HesapTuru("vadeli"));
        hesaplar.add(yeniHesap);
    }

    private int rastgeleHesapIdOlustur() {
        int yeniId;
        do {
            yeniId = 10000 + rand.nextInt(990000);
        } while(Veznedar.hesapIdKullaniliyor(yeniId));
        return yeniId;
    }

    public void mHesapKapat(int hesapId) {
        hesaplar.removeIf(h -> h.getHesapId() == hesapId);
    }
    public void paraYatir(int hesapId,int yatırılacakPara){
        ArrayList<Hesap> hesaplar=this.getHesaplar();
        for(int i = 0; i < hesaplar.size(); i++){
            Hesap h = hesaplar.get(i);
            if(h.getHesapId()==hesapId){
                int yeniBakiye = h.getBakiyeInt() + yatırılacakPara;
                h.setBakiye(yeniBakiye);
                String musteriAdi = this.adi + " " + this.soyad;
                Islem islem = new Islem("Banka", musteriAdi, yatırılacakPara, LocalDate.now(), 
                    "Para Yatırma - Hesap ID: " + hesapId, "Para Yatırma");
                this.islemEkle(islem);
            }
        }
    }
    public void paraCek(int hesapId,int cekilecekPara){
        ArrayList<Hesap> hesaplar=this.getHesaplar();
        for(int i = 0; i < hesaplar.size(); i++){
            Hesap h = hesaplar.get(i);
            if(h.getHesapId()==hesapId){
                int yeniBakiye = h.getBakiyeInt() - cekilecekPara;
                h.setBakiye(yeniBakiye);
                String musteriAdi = this.adi + " " + this.soyad;
                Islem islem = new Islem(musteriAdi, "Banka", cekilecekPara, LocalDate.now(), 
                    "Para Çekme - Hesap ID: " + hesapId, "Para Çekme");
                this.islemEkle(islem);
            }
        }
    }
    public double toplamBakiye(){
        ArrayList<Hesap> hesaplar=this.getHesaplar();
        double toplamBakiye=0;
        for(int i = 0; i < hesaplar.size(); i++){
            Hesap h = hesaplar.get(i);
            toplamBakiye+=h.getBakiyeInt();
        }
        return toplamBakiye;
    }
    public boolean paraGonder(int gonderilenHesapId, int gonderilecekHesapId, int miktar){
        return paraGonder(gonderilenHesapId, gonderilecekHesapId, miktar, null);
    }
    
    public boolean paraGonder(int gonderilenHesapId, int gonderilecekHesapId, int miktar, String mesaj){
        Hesap gonderilenHesap = null;
        for(int i = 0; i < this.hesaplar.size(); i++){
            Hesap h = this.hesaplar.get(i);
            if(h.getHesapId() == gonderilenHesapId){
                gonderilenHesap = h;
                break;
            }
        }

        if(gonderilenHesap == null){
            return false;
        }

        if(gonderilenHesap.getBakiyeInt() < miktar){
            return false;
        }

        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if(musteriler == null){
            return false;
        }

        Hesap gonderilecekHesap = null;
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            ArrayList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++){
                Hesap h = hesaplar.get(j);
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

        int gonderilenYeniBakiye = gonderilenHesap.getBakiyeInt() - miktar;
        int gonderilecekYeniBakiye = gonderilecekHesap.getBakiyeInt() + miktar;
        gonderilenHesap.setBakiye(gonderilenYeniBakiye);
        gonderilecekHesap.setBakiye(gonderilecekYeniBakiye);

        String gondericiAdi = this.adi + " " + this.soyad;
        String aliciAdi = "";
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            ArrayList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++){
                Hesap h = hesaplar.get(j);
                if(h.getHesapId() == gonderilecekHesapId){
                    aliciAdi = m.getAdi() + " " + m.getSoyad();
                    break;
                }
            }
            if(!aliciAdi.isEmpty()){
                break;
            }
        }
        String islemMesaji = (mesaj != null && !mesaj.isEmpty()) ? mesaj : 
            "Para Transferi - Hesap ID: " + gonderilenHesapId + " -> " + gonderilecekHesapId;
        Islem islem = new Islem(gondericiAdi, aliciAdi, miktar, LocalDate.now(), 
            islemMesaji, "Para Transferi");
        this.islemEkle(islem);

        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            ArrayList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++){
                Hesap h = hesaplar.get(j);
                if(h.getHesapId() == gonderilecekHesapId){
                    if(m.getMusteriId() != this.musteriId){
                        Islem aliciIslem = new Islem(gondericiAdi, m.getAdi() + " " + m.getSoyad(), miktar, LocalDate.now(), 
                            islemMesaji, "Para Transferi");
                        m.islemEkle(aliciIslem);
                    }
                    break;
                }
            }
        }

        return true;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (islemler != null) {
            ArrayList<Islem> islemlerList = islemler.toArrayList();
            out.writeObject(islemlerList);
        } else {
            out.writeObject(new ArrayList<Islem>());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = in.readFields();
        
        musteriId = fields.get("musteriId", 0);
        adi = (String) fields.get("adi", null);
        soyad = (String) fields.get("soyad", null);
        TCkimlik = (String) fields.get("TCkimlik", null);
        adres = (String) fields.get("adres", null);
        Object telNoObj = fields.get("telNo", null);
        if (telNoObj instanceof String) {
            telNo = (String) telNoObj;
        } else if (telNoObj instanceof Integer) {
            telNo = String.valueOf(telNoObj);
        } else {
            telNo = "";
        }
        mPassword = (String) fields.get("mPassword", null);
        
        Object kayitTarihiObj = fields.get("kayitTarihi", null);
        if (kayitTarihiObj instanceof LocalDate) {
            kayitTarihi = (LocalDate) kayitTarihiObj;
        } else {
            kayitTarihi = LocalDate.now();
        }
        
        Object hesaplarObj = fields.get("hesaplar", null);
        if (hesaplarObj != null) {
            String className = hesaplarObj.getClass().getName();
            if (className.equals("java.util.ArrayList")) {
                @SuppressWarnings("unchecked")
                java.util.ArrayList<Hesap> oldHesaplar = (java.util.ArrayList<Hesap>) hesaplarObj;
                hesaplar = new ArrayList<>();
                for (int i = 0; i < oldHesaplar.size(); i++) {
                    hesaplar.add(oldHesaplar.get(i));
                }
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<Hesap> hesaplarList = (ArrayList<Hesap>) hesaplarObj;
                hesaplar = hesaplarList;
            }
        } else {
            hesaplar = new ArrayList<>();
        }
        
        Object islemlerObj = fields.get("islemler", null);
        islemler = new Queue<>();
        if (islemlerObj != null) {
            if (islemlerObj instanceof Queue) {
                @SuppressWarnings("unchecked")
                Queue<Islem> islemlerQueue = (Queue<Islem>) islemlerObj;
                islemler = islemlerQueue;
            } else if (islemlerObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<Islem> oldIslemler = (java.util.List<Islem>) islemlerObj;
                for (int i = 0; i < oldIslemler.size(); i++) {
                    islemler.offer(oldIslemler.get(i));
                }
            } else if (islemlerObj instanceof ArrayList) {
                @SuppressWarnings("unchecked")
                ArrayList<Islem> oldIslemler = (ArrayList<Islem>) islemlerObj;
                for (int i = 0; i < oldIslemler.size(); i++) {
                    islemler.offer(oldIslemler.get(i));
                }
            }
        }
        
        try {
            @SuppressWarnings("unchecked")
            ArrayList<Islem> islemlerList = (ArrayList<Islem>) in.readObject();
            if (islemlerList != null && islemlerList.size() > 0) {
                islemler = new Queue<>();
                for(int i = 0; i < islemlerList.size(); i++){
                    islemler.offer(islemlerList.get(i));
                }
            }
        } catch (Exception e) {
        }
    }

}
