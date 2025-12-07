package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.View;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import main.dataStructures.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage)  {
        dosyaIslemleri.tumVerileriYukle();
        
        // Eğer veri yoksa demo veriler oluştur
        demoVerilerOlustur();
        
        Model.getInstance().getView().showLoginWindow();

        musterileriYazdir();
    }
    
    private static void demoVerilerOlustur() {
        // Veznedarlar listesi boşsa demo veznedarlar ekle
        if (Veznedar.getVeznedarlar().isEmpty()) {
            Veznedar.veznedarEkle(1001, "Ahmet", "Yılmaz", "veznedar1");
            Veznedar.veznedarEkle(1002, "Ayşe", "Demir", "veznedar2");
            Veznedar.veznedarEkle(1003, "Mehmet", "Kaya", "admin123");
            dosyaIslemleri.tumVeznedarlariKaydet();
            System.out.println("Demo veznedarlar oluşturuldu.");
        }
        
        // Müşteriler listesi boşsa demo müşteriler ekle
        if (Veznedar.getMusteriler().isEmpty()) {
            Veznedar veznedar = new Veznedar(1001, "Ahmet", "Yılmaz", "veznedar1");
            
            // Demo Müşteri 1
            Musteri musteri1 = new Musteri("Yusuf", "Koç", "12345678901", "İstanbul, Kadıköy", 555123456, "musteri1");
            veznedar.musteriEkle(musteri1);
            // Vadesiz hesaba bakiye ekle
            ArrayList<Hesap> hesaplar1 = musteri1.getHesaplar();
            for (int i = 0; i < hesaplar1.size(); i++) {
                Hesap h = hesaplar1.get(i);
                if (h.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    h.setBakiye(50000);
                } else if (h.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    h.setBakiye(100000);
                }
            }
            
            // Demo Müşteri 2
            Musteri musteri2 = new Musteri("Ayşe", "Yıldız", "98765432109", "Ankara, Çankaya", 555987654, "musteri2");
            veznedar.musteriEkle(musteri2);
            ArrayList<Hesap> hesaplar2 = musteri2.getHesaplar();
            for (int i = 0; i < hesaplar2.size(); i++) {
                Hesap h = hesaplar2.get(i);
                if (h.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    h.setBakiye(75000);
                } else if (h.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    h.setBakiye(150000);
                }
            }
            
            // Demo Müşteri 3
            Musteri musteri3 = new Musteri("Mehmet", "Şahin", "45678912345", "İzmir, Konak", 555456789, "musteri3");
            veznedar.musteriEkle(musteri3);
            ArrayList<Hesap> hesaplar3 = musteri3.getHesaplar();
            for (int i = 0; i < hesaplar3.size(); i++) {
                Hesap h = hesaplar3.get(i);
                if (h.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    h.setBakiye(30000);
                } else if (h.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    h.setBakiye(80000);
                }
            }
            
            // Demo Müşteri 4
            Musteri musteri4 = new Musteri("Fatma", "Özkan", "78912345678", "Bursa, Nilüfer", 555789123, "musteri4");
            veznedar.musteriEkle(musteri4);
            ArrayList<Hesap> hesaplar4 = musteri4.getHesaplar();
            for (int i = 0; i < hesaplar4.size(); i++) {
                Hesap h = hesaplar4.get(i);
                if (h.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    h.setBakiye(25000);
                } else if (h.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    h.setBakiye(60000);
                }
            }
            
            // Demo Müşteri 5
            Musteri musteri5 = new Musteri("Ali", "Çelik", "32165498701", "Antalya, Muratpaşa", 555321654, "musteri5");
            veznedar.musteriEkle(musteri5);
            ArrayList<Hesap> hesaplar5 = musteri5.getHesaplar();
            for (int i = 0; i < hesaplar5.size(); i++) {
                Hesap h = hesaplar5.get(i);
                if (h.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    h.setBakiye(40000);
                } else if (h.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    h.setBakiye(90000);
                }
            }
            
            // Demo işlemler ekle (müşteri1 için)
            musteri1.paraYatir(hesaplar1.get(0).getHesapId(), 10000);
            musteri1.paraCek(hesaplar1.get(0).getHesapId(), 5000);
            
            // Müşterileri kaydet
            dosyaIslemleri.tumMusterileriKaydet();
            System.out.println("Demo müşteriler oluşturuldu.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void musterileriYazdir() {
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        System.out.println("\n========== MÜŞTERİ LİSTESİ ==========");
        System.out.println("Toplam Müşteri Sayısı: " + musteriler.size());
        System.out.println("----------------------------------------");

        if(musteriler.isEmpty()) {
            System.out.println("Henüz müşteri bulunmamaktadır.");
        } else {
            for(int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                System.out.println(m.getTCkimlik());
                System.out.println("\nMüşteri ID: " + m.getMusteriId());
                if(!m.getHesaplar().isEmpty()) {
                    System.out.println((m.getHesaplar().get(0)));
                }

                if(!m.getHesaplar().isEmpty()) {
                    System.out.println("Hesaplar:");
                    ArrayList<Hesap> hesaplar = m.getHesaplar();
                    for(int j = 0; j < hesaplar.size(); j++) {
                        Hesap h = hesaplar.get(j);
                        String hesapTuru = h.getHesapTuru().getHesapTuru();
                        System.out.println("  - Hesap ID: " + h.getHesapId() +
                                " | Tür: " + hesapTuru +
                                " | Bakiye: " + h.getBakiye() + " TL "+
                                "Şifre: "+m.getMPassword() );
                    }
                }
                System.out.println("Toplam Bakiye: " + m.toplamBakiye() + " TL");
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================\n");
    }
}