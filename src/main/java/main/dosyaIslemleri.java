package main;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import main.dataStructures.ArrayList;

public class dosyaIslemleri {
    private static String getDataDir() {
        File idePath = new File("src/main/resources/data");
        if (idePath.exists()) {
            return idePath.getAbsolutePath();
        }
        File runtimePath = new File("data");
        if (runtimePath.exists() || runtimePath.mkdirs()) {
            return runtimePath.getAbsolutePath();
        }
        return new File(System.getProperty("user.dir"), "data").getAbsolutePath();
    }
    
    private static String getMusteriDosya() {
        return getDataDir() + File.separator + "musteriler.yib";
    }
    
    private static String getVeznedarDosya() {
        return getDataDir() + File.separator + "veznedarlar.yib";
    }
    
    private static String getHesapDosya() {
        return getDataDir() + File.separator + "hesaplar.yib";
    }
    
    private static String getIslemDosya() {
        return getDataDir() + File.separator + "islemler.yib";
    }

    private static void klasorOlustur() {
        new File(getDataDir()).mkdirs();
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
        
        try (PrintWriter w = new PrintWriter(new FileWriter(getMusteriDosya()))) {
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                if (m != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(esc(String.valueOf(m.getMusteriId()))).append("|");
                    sb.append(esc(m.getAdi() != null ? m.getAdi() : "")).append("|");
                    sb.append(esc(m.getSoyad() != null ? m.getSoyad() : "")).append("|");
                    sb.append(esc(m.getTCkimlik() != null ? m.getTCkimlik() : "")).append("|");
                    sb.append(esc(m.getAdres() != null ? m.getAdres() : "")).append("|");
                    sb.append(esc(m.getTelNo() != null ? m.getTelNo() : "")).append("|");
                    sb.append(esc(m.getMPassword() != null ? m.getMPassword() : "")).append("|");
                    sb.append(m.getKayitTarihi() != null ? m.getKayitTarihi().format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
                    w.println(sb.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void tumHesaplariKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) return;
        klasorOlustur();
        
        try (PrintWriter w = new PrintWriter(new FileWriter(getHesapDosya()))) {
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                ArrayList<Hesap> hesaplar = m.getHesaplar();
                if (hesaplar != null) {
                    for (int j = 0; j < hesaplar.size(); j++) {
                        Hesap h = hesaplar.get(j);
                        if (h != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(h.getHesapId()).append("|");
                            sb.append(h.getMusteriId()).append("|");
                            sb.append(h.getBakiye()).append("|");
                            sb.append(esc(h.getHesapTuru() != null && h.getHesapTuru().getHesapTuru() != null ? h.getHesapTuru().getHesapTuru() : ""));
                            w.println(sb.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void tumIslemleriKaydet() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        if (musteriler == null) return;
        klasorOlustur();
        
        try (PrintWriter w = new PrintWriter(new FileWriter(getIslemDosya()))) {
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                ArrayList<Islem> islemler = m.getIslemler();
                if (islemler != null) {
                    for (int j = 0; j < islemler.size(); j++) {
                        Islem islem = islemler.get(j);
                        if (islem != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(m.getMusteriId()).append("|");
                            sb.append(esc(islem.getGondericiAdi() != null ? islem.getGondericiAdi() : "")).append("|");
                            sb.append(esc(islem.getAliciAdi() != null ? islem.getAliciAdi() : "")).append("|");
                            sb.append(islem.getMiktar()).append("|");
                            sb.append(islem.getTarih() != null ? islem.getTarih().format(DateTimeFormatter.ISO_LOCAL_DATE) : "").append("|");
                            sb.append(esc(islem.getMesaj() != null ? islem.getMesaj() : "")).append("|");
                            sb.append(esc(islem.getIslemTuru() != null ? islem.getIslemTuru() : ""));
                            w.println(sb.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tumMusterileriYukle() {
        klasorOlustur();
        File f = new File(getMusteriDosya());
        if (!f.exists()) {
            Veznedar.setMusteriler(new ArrayList<>());
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] h = new byte[2];
            if (fis.read(h) == 2 && h[0] == (byte)0xAC && h[1] == (byte)0xED) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                    Object obj = ois.readObject();
                    ArrayList<Musteri> list = obj instanceof java.util.ArrayList ? 
                        convertList((java.util.ArrayList<Musteri>) obj) : (ArrayList<Musteri>) obj;
                    if (list != null) {
                        Veznedar.setMusteriler(list);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Musteri> list = new ArrayList<>();
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;
                
                String telNo = parts[5];
                try {
                    Integer.parseInt(telNo);
                    telNo = unesc(telNo);
                } catch (NumberFormatException e) {
                    telNo = unesc(telNo);
                }
                
                Musteri m = new Musteri(unesc(parts[1]), unesc(parts[2]), unesc(parts[3]), 
                    unesc(parts[4]), telNo, unesc(parts[6]));
                m.setMusteriId(Integer.parseInt(unesc(parts[0])));
                
                if (parts.length > 7 && !parts[7].isEmpty() && !parts[7].contains(",") && !parts[7].contains(";")) {
                    try {
                        m.setKayitTarihi(LocalDate.parse(parts[7], DateTimeFormatter.ISO_LOCAL_DATE));
                    } catch (Exception e) {
                        m.setKayitTarihi(LocalDate.now());
                    }
                } else {
                    m.setKayitTarihi(LocalDate.now());
                }
                
                int hesapIndex = parts.length > 7 && !parts[7].isEmpty() && !parts[7].contains(",") && !parts[7].contains(";") ? 8 : 7;
                if (parts.length > hesapIndex && !parts[hesapIndex].isEmpty() && parts[hesapIndex].contains(",")) {
                    String[] hesaplar = parts[hesapIndex].split(";");
                    for (String hesapStr : hesaplar) {
                        if (hesapStr.isEmpty()) continue;
                        String[] h = hesapStr.split(",");
                        if (h.length == 4) {
                            Hesap hesap = new Hesap(Integer.parseInt(h[1]), Integer.parseInt(h[0]), 
                                new HesapTuru(unesc(h[3])));
                            hesap.setBakiye(h[2]); // Bakiye artık String olarak tutuluyor
                            m.mHesapAc(hesap);
                        }
                    }
                }
                
                int islemIndex = hesapIndex + 1;
                if (parts.length > islemIndex && !parts[islemIndex].isEmpty() && parts[islemIndex].contains(",")) {
                    String[] islemler = parts[islemIndex].split(";");
                    for (String islemStr : islemler) {
                        if (islemStr.isEmpty()) continue;
                        String[] i = islemStr.split(",");
                        if (i.length >= 5) {
                            String gondericiAdi = unesc(i[0]);
                            String aliciAdi = unesc(i[1]);
                            int miktar = Integer.parseInt(i[2]);
                            LocalDate tarih = i[3].isEmpty() ? LocalDate.now() : LocalDate.parse(i[3], DateTimeFormatter.ISO_LOCAL_DATE);
                            String mesaj = unesc(i[4]);
                            String islemTuru = i.length >= 6 ? unesc(i[5]) : "Para Transferi";
                            Islem islem = new Islem(gondericiAdi, aliciAdi, miktar, tarih, mesaj, islemTuru);
                            m.islemEkle(islem);
                        }
                    }
                }
                list.add(m);
            }
            if (!list.isEmpty()) {
                Veznedar.setMusteriler(list);
            } else {
                Veznedar.setMusteriler(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Veznedar.setMusteriler(new ArrayList<>());
        }
    }
    
    public static void tumHesaplariYukle() {
        klasorOlustur();
        File f = new File(getHesapDosya());
        if (!f.exists()) {
            return;
        }
        
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            if (musteriler == null) return;
            
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 4) continue;
                
                int hesapId = Integer.parseInt(parts[0]);
                int musteriId = Integer.parseInt(parts[1]);
                String bakiye = parts[2]; // Bakiye artık String olarak tutuluyor
                String hesapTuru = unesc(parts[3]);
                
                for (int i = 0; i < musteriler.size(); i++) {
                    Musteri m = musteriler.get(i);
                    if (m.getMusteriId() == musteriId) {
                        boolean hesapVar = false;
                        ArrayList<Hesap> hesaplar = m.getHesaplar();
                        for (int j = 0; j < hesaplar.size(); j++) {
                            if (hesaplar.get(j).getHesapId() == hesapId) {
                                hesapVar = true;
                                hesaplar.get(j).setBakiye(bakiye);
                                break;
                            }
                        }
                        if (!hesapVar) {
                            Hesap hesap = new Hesap(musteriId, hesapId, new HesapTuru(hesapTuru));
                            hesap.setBakiye(bakiye);
                            m.mHesapAc(hesap);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void tumIslemleriYukle() {
        klasorOlustur();
        File f = new File(getIslemDosya());
        if (!f.exists()) {
            return;
        }
        
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            if (musteriler == null) return;
            
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;
                
                int musteriId = Integer.parseInt(parts[0]);
                String gondericiAdi = unesc(parts[1]);
                String aliciAdi = unesc(parts[2]);
                int miktar = Integer.parseInt(parts[3]);
                LocalDate tarih = parts[4].isEmpty() ? LocalDate.now() : LocalDate.parse(parts[4], DateTimeFormatter.ISO_LOCAL_DATE);
                String mesaj = unesc(parts[5]);
                String islemTuru = parts.length > 6 && !parts[6].isEmpty() ? unesc(parts[6]) : "Para Transferi";
                
                for (int i = 0; i < musteriler.size(); i++) {
                    Musteri m = musteriler.get(i);
                    if (m.getMusteriId() == musteriId) {
                        Islem islem = new Islem(gondericiAdi, aliciAdi, miktar, tarih, mesaj, islemTuru);
                        m.islemEkle(islem);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try (PrintWriter w = new PrintWriter(new FileWriter(getVeznedarDosya()))) {
            for (int i = 0; i < v.size(); i++) {
                Veznedar veznedar = v.get(i);
                w.println(esc(String.valueOf(veznedar.getUserName())) + "|" + esc(veznedar.getAd()) + "|" +
                    esc(veznedar.getSoyad()) + "|" + esc(veznedar.getVPassword()) + "|" +
                    esc(veznedar.getTcNo() != null ? veznedar.getTcNo() : "") + "|" +
                    esc(veznedar.getTelNo() != null ? veznedar.getTelNo() : "") + "|" +
                    veznedar.getYetki());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tumVeznedarlariYukle() {
        klasorOlustur();
        File f = new File(getVeznedarDosya());
        if (!f.exists()) {
            Veznedar.setVeznedarlar(new ArrayList<>());
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] h = new byte[2];
            if (fis.read(h) == 2 && h[0] == (byte)0xAC && h[1] == (byte)0xED) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                    Object obj = ois.readObject();
                    ArrayList<Veznedar> list = obj instanceof java.util.ArrayList ? 
                        convertVeznedarList((java.util.ArrayList<Veznedar>) obj) : (ArrayList<Veznedar>) obj;
                    if (list != null) {
                        Veznedar.setVeznedarlar(list);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            ArrayList<Veznedar> list = new ArrayList<>();
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("\\|", -1);
                if (p.length >= 4) {
                    String tcNo = p.length > 4 && !p[4].isEmpty() ? unesc(p[4]) : "";
                    String telNo = p.length > 5 && !p[5].isEmpty() ? unesc(p[5]) : "";
                    Veznedar v = new Veznedar(unesc(p[0]), unesc(p[1]), unesc(p[2]), unesc(p[3]), tcNo, telNo);
                    if (p.length > 6 && !p[6].isEmpty()) {
                        try {
                            v.setYetki(Integer.parseInt(p[6]));
                        } catch (NumberFormatException e) {
                            v.setYetki(0);
                        }
                    } else {
                        v.setYetki(0);
                    }
                    list.add(v);
                }
            }
            if (!list.isEmpty()) {
                Veznedar.setVeznedarlar(list);
            } else {
                Veznedar.setVeznedarlar(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        tumHesaplariKaydet();
        tumIslemleriKaydet();
        tumVeznedarlariKaydet();
    }

    public static void tumVerileriYukle() {
        tumMusterileriYukle();
        tumHesaplariYukle();
        tumIslemleriYukle();
        tumVeznedarlariYukle();
    }
}
