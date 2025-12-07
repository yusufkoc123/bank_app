package main;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import main.dataStructures.ArrayList;

public class dosyaIslemleri {
    private static final String DATA_DIR = "data";
    private static final String MUSTERI_DOSYA = DATA_DIR + File.separator + "musteriler.txt";
    private static final String VEZNEDAR_DOSYA = DATA_DIR + File.separator + "veznedarlar.txt";

    // Klasörü oluştur
    private static void klasorOlustur() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    // Tüm müşterileri kaydet (okunabilir text formatında)
    public static void tumMusterileriKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) {
            return;
        }
        klasorOlustur();
        try (PrintWriter writer = new PrintWriter(new FileWriter(MUSTERI_DOSYA, false), true)) {
            writer.println("# Müşteri Verileri - Okunabilir Format");
            writer.println("# Her müşteri MUSTERI_START ve MUSTERI_END arasında");
            writer.println();
            
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                writer.println("MUSTERI_START");
                writer.println("musteriId=" + m.getMusteriId());
                writer.println("adi=" + escapeString(m.getAdi()));
                writer.println("soyad=" + escapeString(m.getSoyad()));
                writer.println("TCkimlik=" + escapeString(m.getTCkimlik()));
                writer.println("adres=" + escapeString(m.getAdres()));
                writer.println("telNo=" + m.getTelNo());
                writer.println("mPassword=" + escapeString(m.getMPassword()));
                
                // Hesaplar
                ArrayList<Hesap> hesaplar = m.getHesaplar();
                writer.println("hesapSayisi=" + hesaplar.size());
                for (int j = 0; j < hesaplar.size(); j++) {
                    Hesap h = hesaplar.get(j);
                    writer.println("HESAP_START");
                    writer.println("  hesapId=" + h.getHesapId());
                    writer.println("  musteriId=" + h.getMusteriId());
                    writer.println("  bakiye=" + h.getBakiye());
                    writer.println("  hesapTuru=" + escapeString(h.getHesapTuru().getHesapTuru()));
                    writer.println("HESAP_END");
                }
                
                // İşlemler
                ArrayList<Islem> islemler = m.getIslemler();
                writer.println("islemSayisi=" + (islemler != null ? islemler.size() : 0));
                if (islemler != null) {
                    for (int j = 0; j < islemler.size(); j++) {
                        Islem islem = islemler.get(j);
                        writer.println("ISLEM_START");
                        writer.println("  gondericiAdi=" + escapeString(islem.getGondericiAdi()));
                        writer.println("  aliciAdi=" + escapeString(islem.getAliciAdi()));
                        writer.println("  miktar=" + islem.getMiktar());
                        writer.println("  tarih=" + (islem.getTarih() != null ? islem.getTarih().format(DateTimeFormatter.ISO_LOCAL_DATE) : ""));
                        writer.println("  mesaj=" + escapeString(islem.getMesaj()));
                        writer.println("  islemTuru=" + escapeString(islem.getIslemTuru()));
                        writer.println("ISLEM_END");
                    }
                }
                
                writer.println("MUSTERI_END");
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Müşteriler kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // String'deki özel karakterleri escape et
    private static String escapeString(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("=", "\\=")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
    
    // Escape edilmiş string'i geri çevir
    private static String unescapeString(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.replace("\\r", "\r")
                  .replace("\\n", "\n")
                  .replace("\\=", "=")
                  .replace("\\\\", "\\");
    }

    // Tüm müşterileri yükle (okunabilir text formatından)
    public static void tumMusterileriYukle() {
        klasorOlustur();
        File dosya = new File(MUSTERI_DOSYA);
        if (!dosya.exists()) {
            return;
        }
        
        // Önce eski binary formatı kontrol et
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MUSTERI_DOSYA))) {
            Object obj = ois.readObject();
            ArrayList<Musteri> yuklenenMusteriler;
            
            if (obj != null && obj.getClass().getName().equals("java.util.ArrayList")) {
                java.util.ArrayList<Musteri> oldList = (java.util.ArrayList<Musteri>) obj;
                yuklenenMusteriler = new ArrayList<>();
                for (int i = 0; i < oldList.size(); i++) {
                    yuklenenMusteriler.add(oldList.get(i));
                }
            } else {
                yuklenenMusteriler = (ArrayList<Musteri>) obj;
            }
            
            if (yuklenenMusteriler != null) {
                Veznedar.setMusteriler(yuklenenMusteriler);
                // Eski formatı yeni formata çevir
                tumMusterileriKaydet();
                return;
            }
        } catch (Exception e) {
            // Binary format değil, text formatını oku
        }
        
        // Text formatını oku
        try (BufferedReader reader = new BufferedReader(new FileReader(MUSTERI_DOSYA))) {
            ArrayList<Musteri> yuklenenMusteriler = new ArrayList<>();
            String line;
            Musteri currentMusteri = null;
            Hesap currentHesap = null;
            Islem currentIslem = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                if (line.equals("MUSTERI_START")) {
                    currentMusteri = new Musteri("", "", "", "", 0, "");
                } else if (line.equals("MUSTERI_END")) {
                    if (currentMusteri != null) {
                        yuklenenMusteriler.add(currentMusteri);
                        currentMusteri = null;
                    }
                } else if (line.equals("HESAP_START")) {
                    currentHesap = new Hesap(0, 0, new HesapTuru("vadesiz"));
                } else if (line.equals("HESAP_END")) {
                    if (currentHesap != null && currentMusteri != null) {
                        currentMusteri.mHesapAc(currentHesap);
                        currentHesap = null;
                    }
                } else if (line.equals("ISLEM_START")) {
                    currentIslem = new Islem("", "", 0, LocalDate.now(), "", "");
                } else if (line.equals("ISLEM_END")) {
                    if (currentIslem != null && currentMusteri != null) {
                        currentMusteri.islemEkle(currentIslem);
                        currentIslem = null;
                    }
                } else if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = unescapeString(parts[1].trim());
                        
                        if (currentMusteri != null && currentHesap == null && currentIslem == null) {
                            // Müşteri alanları
                            switch (key) {
                                case "musteriId":
                                    currentMusteri.setMusteriId(Integer.parseInt(value));
                                    break;
                                case "adi":
                                    currentMusteri.setAdi(value);
                                    break;
                                case "soyad":
                                    currentMusteri.setSoyad(value);
                                    break;
                                case "TCkimlik":
                                    currentMusteri.setTCkimlik(value);
                                    break;
                                case "adres":
                                    currentMusteri.setAdres(value);
                                    break;
                                case "telNo":
                                    currentMusteri.setTelNo(Integer.parseInt(value));
                                    break;
                                case "mPassword":
                                    currentMusteri.setMPassword(value);
                                    break;
                            }
                        } else if (currentHesap != null) {
                            // Hesap alanları
                            switch (key.trim()) {
                                case "hesapId":
                                    currentHesap.setHesapId(Integer.parseInt(value));
                                    break;
                                case "musteriId":
                                    currentHesap.setMusteriId(Integer.parseInt(value));
                                    break;
                                case "bakiye":
                                    currentHesap.setBakiye(Integer.parseInt(value));
                                    break;
                                case "hesapTuru":
                                    currentHesap.setHesapTuru(new HesapTuru(value));
                                    break;
                            }
                        } else if (currentIslem != null) {
                            // İşlem alanları
                            switch (key.trim()) {
                                case "gondericiAdi":
                                    currentIslem.setGondericiAdi(value);
                                    break;
                                case "aliciAdi":
                                    currentIslem.setAliciAdi(value);
                                    break;
                                case "miktar":
                                    currentIslem.setMiktar(Integer.parseInt(value));
                                    break;
                                case "tarih":
                                    if (!value.isEmpty()) {
                                        currentIslem.setTarih(LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE));
                                    }
                                    break;
                                case "mesaj":
                                    currentIslem.setMesaj(value);
                                    break;
                                case "islemTuru":
                                    currentIslem.setIslemTuru(value);
                                    break;
                            }
                        }
                    }
                }
            }
            
            if (!yuklenenMusteriler.isEmpty()) {
                Veznedar.setMusteriler(yuklenenMusteriler);
            }
        } catch (IOException e) {
            System.err.println("Müşteriler yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            dosyayiYedekle(dosya, MUSTERI_DOSYA);
            Veznedar.setMusteriler(new ArrayList<>());
        }
    }
    
    // Bozuk dosyayı yedekleme yardımcı metodu
    private static void dosyayiYedekle(File dosya, String dosyaYolu) {
        if (dosya.exists()) {
            try {
                // Benzersiz yedek dosya adı oluştur (tarih-saat ile)
                String timestamp = String.valueOf(System.currentTimeMillis());
                File backupFile = new File(dosyaYolu + ".backup_" + timestamp);
                
                // Dosyayı kopyala
                try (FileInputStream fis = new FileInputStream(dosya);
                     FileOutputStream fos = new FileOutputStream(backupFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                
                // Orijinal dosyayı sil
                dosya.delete();
                System.err.println("Bozuk dosya yedeklendi: " + backupFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Dosya yedeklenirken hata oluştu: " + e.getMessage());
                // Yedekleme başarısız olsa bile dosyayı silmeyi dene
                dosya.delete();
            }
        }
    }

    // Tüm veznedarları kaydet (okunabilir text formatında)
    public static void tumVeznedarlariKaydet() {
        ArrayList<Veznedar> veznedarlar = Veznedar.getVeznedarlar();
        if (veznedarlar == null) {
            return;
        }
        klasorOlustur();
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEZNEDAR_DOSYA, false), true)) {
            writer.println("# Veznedar Verileri - Okunabilir Format");
            writer.println("# Her veznedar VEZNEDAR_START ve VEZNEDAR_END arasında");
            writer.println();
            
            for (int i = 0; i < veznedarlar.size(); i++) {
                Veznedar v = veznedarlar.get(i);
                writer.println("VEZNEDAR_START");
                writer.println("tellerId=" + v.getTellerId());
                writer.println("ad=" + escapeString(v.getAd()));
                writer.println("soyad=" + escapeString(v.getSoyad()));
                writer.println("vPassword=" + escapeString(v.getVPassword()));
                writer.println("VEZNEDAR_END");
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Veznedarlar kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Tüm veznedarları yükle (okunabilir text formatından)
    public static void tumVeznedarlariYukle() {
        klasorOlustur();
        File dosya = new File(VEZNEDAR_DOSYA);
        if (!dosya.exists()) {
            return;
        }
        
        // Önce eski binary formatı kontrol et
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(VEZNEDAR_DOSYA))) {
            Object obj = ois.readObject();
            ArrayList<Veznedar> yuklenenVeznedarlar;
            
            if (obj != null && obj.getClass().getName().equals("java.util.ArrayList")) {
                java.util.ArrayList<Veznedar> oldList = (java.util.ArrayList<Veznedar>) obj;
                yuklenenVeznedarlar = new ArrayList<>();
                for (int i = 0; i < oldList.size(); i++) {
                    yuklenenVeznedarlar.add(oldList.get(i));
                }
            } else {
                yuklenenVeznedarlar = (ArrayList<Veznedar>) obj;
            }
            
            if (yuklenenVeznedarlar != null) {
                Veznedar.setVeznedarlar(yuklenenVeznedarlar);
                // Eski formatı yeni formata çevir
                tumVeznedarlariKaydet();
                return;
            }
        } catch (Exception e) {
            // Binary format değil, text formatını oku
        }
        
        // Text formatını oku
        try (BufferedReader reader = new BufferedReader(new FileReader(VEZNEDAR_DOSYA))) {
            ArrayList<Veznedar> yuklenenVeznedarlar = new ArrayList<>();
            String line;
            Veznedar currentVeznedar = null;
            int tellerId = 0;
            String ad = "";
            String soyad = "";
            String vPassword = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                if (line.equals("VEZNEDAR_START")) {
                    tellerId = 0;
                    ad = "";
                    soyad = "";
                    vPassword = "";
                } else if (line.equals("VEZNEDAR_END")) {
                    if (tellerId != 0) {
                        currentVeznedar = new Veznedar(tellerId, ad, soyad, vPassword);
                        yuklenenVeznedarlar.add(currentVeznedar);
                    }
                } else if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = unescapeString(parts[1].trim());
                        
                        switch (key) {
                            case "tellerId":
                                tellerId = Integer.parseInt(value);
                                break;
                            case "ad":
                                ad = value;
                                break;
                            case "soyad":
                                soyad = value;
                                break;
                            case "vPassword":
                                vPassword = value;
                                break;
                        }
                    }
                }
            }
            
            if (!yuklenenVeznedarlar.isEmpty()) {
                Veznedar.setVeznedarlar(yuklenenVeznedarlar);
            }
        } catch (IOException e) {
            System.err.println("Veznedarlar yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            dosyayiYedekle(dosya, VEZNEDAR_DOSYA);
            Veznedar.setVeznedarlar(new ArrayList<>());
        }
    }

    // Tüm verileri kaydet (müşteriler ve veznedarlar)
    public static void tumVerileriKaydet() {
        tumMusterileriKaydet();
        tumVeznedarlariKaydet();
    }

    // Tüm verileri yükle (müşteriler ve veznedarlar)
    public static void tumVerileriYukle() {
        tumMusterileriYukle();
        tumVeznedarlariYukle();
    }
}
