package main;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import main.dataStructures.ArrayList;

public class dosyaIslemleri {
    private static final String DATA_DIR = "data";
    private static final String MUSTERI_DOSYA = DATA_DIR + File.separator + "musteriler.txt";
    private static final String VEZNEDAR_DOSYA = DATA_DIR + File.separator + "veznedarlar.txt";

    private static void klasorOlustur() {
        new File(DATA_DIR).mkdirs();
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("=", "\\=").replace("\n", "\\n");
    }

    private static String unesc(String s) {
        return s == null ? "" : s.replace("\\n", "\n").replace("\\=", "=");
    }

    public static void tumMusterileriKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) return;
        klasorOlustur();
        try (PrintWriter w = new PrintWriter(new FileWriter(MUSTERI_DOSYA))) {
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                w.println("MUSTERI_START");
                w.println("musteriId=" + m.getMusteriId());
                w.println("adi=" + esc(m.getAdi()));
                w.println("soyad=" + esc(m.getSoyad()));
                w.println("TCkimlik=" + esc(m.getTCkimlik()));
                w.println("adres=" + esc(m.getAdres()));
                w.println("telNo=" + m.getTelNo());
                w.println("mPassword=" + esc(m.getMPassword()));
                ArrayList<Hesap> hesaplar = m.getHesaplar();
                for (int j = 0; j < hesaplar.size(); j++) {
                    Hesap h = hesaplar.get(j);
                    w.println("HESAP_START");
                    w.println("  hesapId=" + h.getHesapId());
                    w.println("  musteriId=" + h.getMusteriId());
                    w.println("  bakiye=" + h.getBakiye());
                    w.println("  hesapTuru=" + esc(h.getHesapTuru().getHesapTuru()));
                    w.println("HESAP_END");
                }
                ArrayList<Islem> islemler = m.getIslemler();
                if (islemler != null) {
                    for (int j = 0; j < islemler.size(); j++) {
                        Islem islem = islemler.get(j);
                        w.println("ISLEM_START");
                        w.println("  gondericiAdi=" + esc(islem.getGondericiAdi()));
                        w.println("  aliciAdi=" + esc(islem.getAliciAdi()));
                        w.println("  miktar=" + islem.getMiktar());
                        w.println("  tarih=" + (islem.getTarih() != null ? islem.getTarih().format(DateTimeFormatter.ISO_LOCAL_DATE) : ""));
                        w.println("  mesaj=" + esc(islem.getMesaj()));
                        w.println("  islemTuru=" + esc(islem.getIslemTuru()));
                        w.println("ISLEM_END");
                    }
                }
                w.println("MUSTERI_END");
            }
        } catch (IOException e) {
            System.err.println("Müşteriler kaydedilemedi: " + e.getMessage());
        }
    }

    public static void tumMusterileriYukle() {
        klasorOlustur();
        File f = new File(MUSTERI_DOSYA);
        if (!f.exists()) return;
        
        // Binary format kontrolü
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] h = new byte[2];
            if (fis.read(h) == 2 && h[0] == (byte)0xAC && h[1] == (byte)0xED) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                    Object obj = ois.readObject();
                    ArrayList<Musteri> list = obj instanceof java.util.ArrayList ? 
                        convertList((java.util.ArrayList<Musteri>) obj) : (ArrayList<Musteri>) obj;
                    if (list != null) {
                        Veznedar.setMusteriler(list);
                        tumMusterileriKaydet();
                        return;
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        
        // Text formatını oku
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Musteri> list = new ArrayList<>();
            Musteri m = null;
            Hesap h = null;
            Islem i = null;
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.equals("MUSTERI_START")) m = new Musteri("", "", "", "", 0, "");
                else if (line.equals("MUSTERI_END")) { list.add(m); m = null; }
                else if (line.equals("HESAP_START")) h = new Hesap(0, 0, new HesapTuru("vadesiz"));
                else if (line.equals("HESAP_END")) { if (h != null && m != null) m.mHesapAc(h); h = null; }
                else if (line.equals("ISLEM_START")) i = new Islem("", "", 0, LocalDate.now(), "", "");
                else if (line.equals("ISLEM_END")) { if (i != null && m != null) m.islemEkle(i); i = null; }
                else if (line.contains("=")) {
                    String[] p = line.split("=", 2);
                    if (p.length != 2) continue;
                    String k = p[0].trim(), v = unesc(p[1].trim());
                    if (m != null && h == null && i == null) {
                        if (k.equals("musteriId")) m.setMusteriId(Integer.parseInt(v));
                        else if (k.equals("adi")) m.setAdi(v);
                        else if (k.equals("soyad")) m.setSoyad(v);
                        else if (k.equals("TCkimlik")) m.setTCkimlik(v);
                        else if (k.equals("adres")) m.setAdres(v);
                        else if (k.equals("telNo")) m.setTelNo(Integer.parseInt(v));
                        else if (k.equals("mPassword")) m.setMPassword(v);
                    } else if (h != null) {
                        if (k.equals("hesapId")) h.setHesapId(Integer.parseInt(v));
                        else if (k.equals("musteriId")) h.setMusteriId(Integer.parseInt(v));
                        else if (k.equals("bakiye")) h.setBakiye(Integer.parseInt(v));
                        else if (k.equals("hesapTuru")) h.setHesapTuru(new HesapTuru(v));
                    } else if (i != null) {
                        if (k.equals("gondericiAdi")) i.setGondericiAdi(v);
                        else if (k.equals("aliciAdi")) i.setAliciAdi(v);
                        else if (k.equals("miktar")) i.setMiktar(Integer.parseInt(v));
                        else if (k.equals("tarih") && !v.isEmpty()) i.setTarih(LocalDate.parse(v, DateTimeFormatter.ISO_LOCAL_DATE));
                        else if (k.equals("mesaj")) i.setMesaj(v);
                        else if (k.equals("islemTuru")) i.setIslemTuru(v);
                    }
                }
            }
            if (!list.isEmpty()) Veznedar.setMusteriler(list);
        } catch (Exception e) {
            System.err.println("Müşteriler yüklenemedi: " + e.getMessage());
            Veznedar.setMusteriler(new ArrayList<>());
        }
    }

    private static ArrayList<Musteri> convertList(java.util.ArrayList<Musteri> old) {
        ArrayList<Musteri> n = new ArrayList<>();
        for (int i = 0; i < old.size(); i++) n.add(old.get(i));
        return n;
    }

    public static void tumVeznedarlariKaydet() {
        ArrayList<Veznedar> v = Veznedar.getVeznedarlar();
        if (v == null) return;
        klasorOlustur();
        try (PrintWriter w = new PrintWriter(new FileWriter(VEZNEDAR_DOSYA))) {
            for (int i = 0; i < v.size(); i++) {
                Veznedar veznedar = v.get(i);
                w.println("VEZNEDAR_START");
                w.println("tellerId=" + veznedar.getTellerId());
                w.println("ad=" + esc(veznedar.getAd()));
                w.println("soyad=" + esc(veznedar.getSoyad()));
                w.println("vPassword=" + esc(veznedar.getVPassword()));
                w.println("VEZNEDAR_END");
            }
        } catch (IOException e) {
            System.err.println("Veznedarlar kaydedilemedi: " + e.getMessage());
        }
    }

    public static void tumVeznedarlariYukle() {
        klasorOlustur();
        File f = new File(VEZNEDAR_DOSYA);
        if (!f.exists()) return;
        
        // Binary format kontrolü
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] h = new byte[2];
            if (fis.read(h) == 2 && h[0] == (byte)0xAC && h[1] == (byte)0xED) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                    Object obj = ois.readObject();
                    ArrayList<Veznedar> list = obj instanceof java.util.ArrayList ? 
                        convertVeznedarList((java.util.ArrayList<Veznedar>) obj) : (ArrayList<Veznedar>) obj;
                    if (list != null) {
                        Veznedar.setVeznedarlar(list);
                        tumVeznedarlariKaydet();
                        return;
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        
        // Text formatını oku
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Veznedar> list = new ArrayList<>();
            int id = 0;
            String ad = "", soyad = "", pass = "";
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.equals("VEZNEDAR_START")) { id = 0; ad = ""; soyad = ""; pass = ""; }
                else if (line.equals("VEZNEDAR_END")) { if (id != 0) list.add(new Veznedar(id, ad, soyad, pass)); }
                else if (line.contains("=")) {
                    String[] p = line.split("=", 2);
                    if (p.length == 2) {
                        String k = p[0].trim(), v = unesc(p[1].trim());
                        if (k.equals("tellerId")) id = Integer.parseInt(v);
                        else if (k.equals("ad")) ad = v;
                        else if (k.equals("soyad")) soyad = v;
                        else if (k.equals("vPassword")) pass = v;
                    }
                }
            }
            if (!list.isEmpty()) Veznedar.setVeznedarlar(list);
        } catch (Exception e) {
            System.err.println("Veznedarlar yüklenemedi: " + e.getMessage());
            Veznedar.setVeznedarlar(new ArrayList<>());
        }
    }

    private static ArrayList<Veznedar> convertVeznedarList(java.util.ArrayList<Veznedar> old) {
        ArrayList<Veznedar> n = new ArrayList<>();
        for (int i = 0; i < old.size(); i++) n.add(old.get(i));
        return n;
    }

    public static void tumVerileriKaydet() {
        tumMusterileriKaydet();
        tumVeznedarlariKaydet();
    }

    public static void tumVerileriYukle() {
        tumMusterileriYukle();
        tumVeznedarlariYukle();
    }
}
