package main;

import java.io.*;
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

    // Tüm müşterileri kaydet (tek dosyada liste halinde)
    public static void tumMusterileriKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) {
            return;
        }
        klasorOlustur();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MUSTERI_DOSYA))) {
            oos.writeObject(musteriler);
        } catch (IOException e) {
            System.err.println("Müşteriler kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Tüm müşterileri yükle (tek dosyadan liste halinde)
    @SuppressWarnings("unchecked")
    public static void tumMusterileriYukle() {
        klasorOlustur();
        File dosya = new File(MUSTERI_DOSYA);
        if (!dosya.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MUSTERI_DOSYA))) {
            Object obj = ois.readObject();
            ArrayList<Musteri> yuklenenMusteriler;
            
            // Eğer java.util.ArrayList ise, main.dataStructures.ArrayList'e çevir
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
            }
        } catch (java.io.StreamCorruptedException e) {
            // Dosya bozuk - yedekle ve yeni format ile başlat
            System.err.println("Müşteriler dosyası bozuk tespit edildi. Dosya yedekleniyor ve yeni format ile başlatılıyor...");
            dosyayiYedekle(dosya, MUSTERI_DOSYA);
            Veznedar.setMusteriler(new ArrayList<>());
        } catch (ClassCastException e) {
            // Eski format hatası - dosyayı yedekle ve yeni format ile başlat
            System.err.println("Eski veri formatı tespit edildi. Dosya yedekleniyor ve yeni format ile başlatılıyor...");
            dosyayiYedekle(dosya, MUSTERI_DOSYA);
            Veznedar.setMusteriler(new ArrayList<>());
        } catch (IOException | ClassNotFoundException e) {
            // Diğer IO hataları - dosyayı yedekle ve yeni format ile başlat
            System.err.println("Müşteriler yüklenirken hata oluştu: " + e.getMessage());
            System.err.println("Dosya yedekleniyor ve yeni format ile başlatılıyor...");
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

    // Tüm veznedarları kaydet (tek dosyada liste halinde)
    public static void tumVeznedarlariKaydet() {
        ArrayList<Veznedar> veznedarlar = Veznedar.getVeznedarlar();
        if (veznedarlar == null) {
            return;
        }
        klasorOlustur();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VEZNEDAR_DOSYA))) {
            oos.writeObject(veznedarlar);
        } catch (IOException e) {
            System.err.println("Veznedarlar kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Tüm veznedarları yükle (tek dosyadan liste halinde)
    @SuppressWarnings("unchecked")
    public static void tumVeznedarlariYukle() {
        klasorOlustur();
        File dosya = new File(VEZNEDAR_DOSYA);
        if (!dosya.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(VEZNEDAR_DOSYA))) {
            Object obj = ois.readObject();
            ArrayList<Veznedar> yuklenenVeznedarlar;
            
            // Eğer java.util.ArrayList ise, main.dataStructures.ArrayList'e çevir
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
            }
        } catch (java.io.StreamCorruptedException e) {
            // Dosya bozuk - yedekle ve yeni format ile başlat
            System.err.println("Veznedarlar dosyası bozuk tespit edildi. Dosya yedekleniyor ve yeni format ile başlatılıyor...");
            dosyayiYedekle(dosya, VEZNEDAR_DOSYA);
            Veznedar.setVeznedarlar(new ArrayList<>());
        } catch (ClassCastException e) {
            // Eski format hatası - dosyayı yedekle ve yeni format ile başlat
            System.err.println("Eski veri formatı tespit edildi. Dosya yedekleniyor ve yeni format ile başlatılıyor...");
            dosyayiYedekle(dosya, VEZNEDAR_DOSYA);
            Veznedar.setVeznedarlar(new ArrayList<>());
        } catch (IOException | ClassNotFoundException e) {
            // Diğer IO hataları - dosyayı yedekle ve yeni format ile başlat
            System.err.println("Veznedarlar yüklenirken hata oluştu: " + e.getMessage());
            System.err.println("Dosya yedekleniyor ve yeni format ile başlatılıyor...");
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
