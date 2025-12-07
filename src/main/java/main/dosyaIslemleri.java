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
        return s == null ? "" : s.replace("|", "\\|").replace("\n", "\\n").replace(";", "\\;");
    }

    private static String unesc(String s) {
        return s == null ? "" : s.replace("\\;", ";").replace("\\n", "\n").replace("\\|", "|");
    }

    public static void tumMusterileriKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) return;
        klasorOlustur();
        try (PrintWriter w = new PrintWriter(new FileWriter(MUSTERI_DOSYA))) {
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append(m.getMusteriId()).append("|");
                sb.append(esc(m.getAdi())).append("|");
                sb.append(esc(m.getSoyad())).append("|");
                sb.append(esc(m.getTCkimlik())).append("|");
                sb.append(esc(m.getAdres())).append("|");
                sb.append(m.getTelNo()).append("|");
                sb.append(esc(m.getMPassword())).append("|");
                
                // Hesaplar
                ArrayList<Hesap> hesaplar = m.getHesaplar();
                for (int j = 0; j < hesaplar.size(); j++) {
                    if (j > 0) sb.append(";");
                    Hesap h = hesaplar.get(j);
                    sb.append(h.getHesapId()).append(",");
                    sb.append(h.getMusteriId()).append(",");
                    sb.append(h.getBakiye()).append(",");
                    sb.append(esc(h.getHesapTuru().getHesapTuru()));
                }
                sb.append("|");
                
                // İşlemler
                ArrayList<Islem> islemler = m.getIslemler();
                if (islemler != null) {
                    for (int j = 0; j < islemler.size(); j++) {
                        if (j > 0) sb.append(";");
                        Islem islem = islemler.get(j);
                        sb.append(esc(islem.getGondericiAdi())).append(",");
                        sb.append(esc(islem.getAliciAdi())).append(",");
                        sb.append(islem.getMiktar()).append(",");
                        sb.append(islem.getTarih() != null ? islem.getTarih().format(DateTimeFormatter.ISO_LOCAL_DATE) : "").append(",");
                        sb.append(esc(islem.getMesaj())).append(",");
                        sb.append(esc(islem.getIslemTuru()));
                    }
                }
                w.println(sb.toString());
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
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 8) continue;
                
                Musteri m = new Musteri(unesc(parts[1]), unesc(parts[2]), unesc(parts[3]), 
                    unesc(parts[4]), Integer.parseInt(parts[5]), unesc(parts[6]));
                m.setMusteriId(Integer.parseInt(parts[0]));
                
                // Hesaplar
                if (!parts[7].isEmpty()) {
                    String[] hesaplar = parts[7].split(";");
                    for (String hesapStr : hesaplar) {
                        if (hesapStr.isEmpty()) continue;
                        String[] h = hesapStr.split(",");
                        if (h.length == 4) {
                            Hesap hesap = new Hesap(Integer.parseInt(h[1]), Integer.parseInt(h[0]), 
                                new HesapTuru(unesc(h[3])));
                            hesap.setBakiye(Integer.parseInt(h[2]));
                            m.mHesapAc(hesap);
                        }
                    }
                }
                
                // İşlemler
                if (parts.length > 8 && !parts[8].isEmpty()) {
                    String[] islemler = parts[8].split(";");
                    for (String islemStr : islemler) {
                        if (islemStr.isEmpty()) continue;
                        String[] i = islemStr.split(",");
                        if (i.length == 6) {
                            Islem islem = new Islem(unesc(i[0]), unesc(i[1]), Integer.parseInt(i[2]), 
                                i[3].isEmpty() ? LocalDate.now() : LocalDate.parse(i[3], DateTimeFormatter.ISO_LOCAL_DATE),
                                unesc(i[4]), unesc(i[5]));
                            m.islemEkle(islem);
                        }
                    }
                }
                list.add(m);
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
                w.println(veznedar.getTellerId() + "|" + esc(veznedar.getAd()) + "|" + 
                    esc(veznedar.getSoyad()) + "|" + esc(veznedar.getVPassword()));
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
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("\\|", -1);
                if (p.length == 4) {
                    list.add(new Veznedar(Integer.parseInt(p[0]), unesc(p[1]), unesc(p[2]), unesc(p[3])));
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
