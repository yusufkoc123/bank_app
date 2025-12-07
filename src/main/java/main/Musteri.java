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
    private int telNo;
    private String mPassword;
    private LocalDate kayitTarihi;
    private ArrayList<Hesap> hesaplar;
    private Queue<Islem> islemler;
    private static final int MAX_ISLEM_SAYISI = 20;
    private static Random rand = new Random();

    public Musteri(String adi, String soyad, String TCkimlik, String adres, int telNo, String mPassword) {
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
            yeniId = 1000 + rand.nextInt(999000); // 1000 ile 999999 arası
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
    public int getTelNo() {
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
        // Queue'yu ArrayList'e çevir (son 20 işlem)
        return islemler.toArrayList();
    }
    
    // İşlem ekleme metodu - Queue mantığıyla çalışır, 20'den fazla olursa en eski olanı çıkarır
    public void islemEkle(Islem islem) {
        if (islem == null) {
            return;
        }
        islemler.offer(islem); // Queue'ya ekle
        // 20'den fazla işlem varsa en eski olanı çıkar (FIFO)
        while (islemler.size() > MAX_ISLEM_SAYISI) {
            islemler.poll(); // En eski işlemi çıkar
        }
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
        // Her müşteri için en fazla bir adet vadeli hesap olmasını sağla
        if (h != null &&
                h.getHesapTuru() != null &&
                "vadeli".equals(h.getHesapTuru().getHesapTuru())) {
            for (int i = 0; i < hesaplar.size(); i++) {
                Hesap mevcutHesap = hesaplar.get(i);
                if (mevcutHesap.getHesapTuru() != null &&
                        "vadeli".equals(mevcutHesap.getHesapTuru().getHesapTuru())) {
                    // Zaten bir vadeli hesap var; yeni vadeli hesabı ekleme
                    return;
                }
            }
        }
        hesaplar.add(h);
    }

    // Rastgele ID ile vadesiz hesap açma
    public void mHesapAcVadesiz() {
        int rastgeleHesapId = rastgeleHesapIdOlustur();
        Hesap yeniHesap = new Hesap(this.musteriId, rastgeleHesapId, new HesapTuru("vadesiz"));
        hesaplar.add(yeniHesap);
    }

    // Rastgele ID ile vadeli hesap açma
    public void mHesapAcVadeli() {
        int rastgeleHesapId = rastgeleHesapIdOlustur();
        Hesap yeniHesap = new Hesap(this.musteriId, rastgeleHesapId, new HesapTuru("vadeli"));
        hesaplar.add(yeniHesap);
    }

    // Rastgele hesap ID oluştur (10000-999999 arası)
    private int rastgeleHesapIdOlustur() {
        int yeniId;
        do {
            yeniId = 10000 + rand.nextInt(990000); // 10000 ile 999999 arası
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
                h.setBakiye(h.getBakiye()+yatırılacakPara);
                // İşlem kaydı ekle
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
                h.setBakiye(h.getBakiye()-cekilecekPara);
                // İşlem kaydı ekle
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
            toplamBakiye+=h.getBakiye();
        }
        return toplamBakiye;
    }
    public boolean paraGonder(int gonderilenHesapId, int gonderilecekHesapId, int miktar){
        return paraGonder(gonderilenHesapId, gonderilecekHesapId, miktar, null);
    }
    
    public boolean paraGonder(int gonderilenHesapId, int gonderilecekHesapId, int miktar, String mesaj){
        // Gönderen hesabı bul ve bakiyeyi kontrol et
        Hesap gonderilenHesap = null;
        for(int i = 0; i < this.hesaplar.size(); i++){
            Hesap h = this.hesaplar.get(i);
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

        // Alıcı hesabı Veznedar sınıfındaki müşteriler listesinden bul
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if(musteriler == null){
            return false; // Müşteriler listesi bulunamadı
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

        // Para transferi yap
        gonderilenHesap.setBakiye(gonderilenHesap.getBakiye() - miktar);
        gonderilecekHesap.setBakiye(gonderilecekHesap.getBakiye() + miktar);

        // İşlem kaydı ekle - gönderen müşteri için
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
        // Mesaj varsa kullan, yoksa varsayılan mesaj
        String islemMesaji = (mesaj != null && !mesaj.isEmpty()) ? mesaj : 
            "Para Transferi - Hesap ID: " + gonderilenHesapId + " -> " + gonderilecekHesapId;
        Islem islem = new Islem(gondericiAdi, aliciAdi, miktar, LocalDate.now(), 
            islemMesaji, "Para Transferi");
        this.islemEkle(islem);

        // Alıcı müşteri için de işlem kaydı ekle
        for(int i = 0; i < musteriler.size(); i++){
            Musteri m = musteriler.get(i);
            ArrayList<Hesap> hesaplar = m.getHesaplar();
            for(int j = 0; j < hesaplar.size(); j++){
                Hesap h = hesaplar.get(j);
                if(h.getHesapId() == gonderilecekHesapId){
                    Islem aliciIslem = new Islem(gondericiAdi, m.getAdi() + " " + m.getSoyad(), miktar, LocalDate.now(), 
                        islemMesaji, "Para Transferi");
                    m.islemEkle(aliciIslem);
                    break;
                }
            }
        }

        return true; // Transfer başarılı
    }

    // Queue'yu serialize etmek için özel metodlar
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Queue'yu ArrayList'e çevirip kaydet
        if (islemler != null) {
            ArrayList<Islem> islemlerList = islemler.toArrayList();
            out.writeObject(islemlerList);
        } else {
            out.writeObject(new ArrayList<Islem>());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // readFields() kullanarak alanları Object olarak oku, sonra tip dönüşümü yap
        ObjectInputStream.GetField fields = in.readFields();
        
        musteriId = fields.get("musteriId", 0);
        adi = (String) fields.get("adi", null);
        soyad = (String) fields.get("soyad", null);
        TCkimlik = (String) fields.get("TCkimlik", null);
        adres = (String) fields.get("adres", null);
        telNo = fields.get("telNo", 0);
        mPassword = (String) fields.get("mPassword", null);
        
        // kayitTarihi alanını oku (eski kayıtlarda null olabilir)
        Object kayitTarihiObj = fields.get("kayitTarihi", null);
        if (kayitTarihiObj instanceof LocalDate) {
            kayitTarihi = (LocalDate) kayitTarihiObj;
        } else {
            kayitTarihi = LocalDate.now(); // Eski kayıtlar için bugünün tarihi
        }
        
        // hesaplar alanını Object olarak oku ve tip dönüşümü yap
        Object hesaplarObj = fields.get("hesaplar", null);
        if (hesaplarObj != null) {
            String className = hesaplarObj.getClass().getName();
            if (className.equals("java.util.ArrayList")) {
                // Eski java.util.ArrayList ise, yeni tip'e çevir
                @SuppressWarnings("unchecked")
                java.util.ArrayList<Hesap> oldHesaplar = (java.util.ArrayList<Hesap>) hesaplarObj;
                hesaplar = new ArrayList<>();
                for (int i = 0; i < oldHesaplar.size(); i++) {
                    hesaplar.add(oldHesaplar.get(i));
                }
            } else {
                // Zaten doğru tip ise direkt atama yap
                @SuppressWarnings("unchecked")
                ArrayList<Hesap> hesaplarList = (ArrayList<Hesap>) hesaplarObj;
                hesaplar = hesaplarList;
            }
        } else {
            hesaplar = new ArrayList<>();
        }
        
        // islemler alanını Object olarak oku
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
        
        // Queue için özel işlem - writeObject'te ekstra yazılan ArrayList'i oku
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
            // Eğer ekstra veri yoksa, mevcut islemler'i kullan
        }
    }

}
