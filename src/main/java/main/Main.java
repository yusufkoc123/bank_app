package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.View;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage)  {
        Model.getInstance().getView().showLoginWindow();








        Veznedar veznedar = new Veznedar(1, "Test", "Veznedar", "test123");
        Musteri musteri1 = new Musteri("Ahmet", "Yılmaz", "12345678901", "İstanbul, Kadıköy", 555123456, "musteri1");
        veznedar.musteriEkle(musteri1);

        // Müşteri 1 için 4 adet ek vadesiz hesap aç
        for (int i = 0; i < 4; i++) {
            veznedar.vHesapAc(musteri1.getMusteriId());
        }

        // İkinci müşteri
        Musteri musteri2 = new Musteri("Ayşe", "Demir", "98765432109", "Ankara", 555987654, "musteri2");
        veznedar.musteriEkle(musteri2);
        musterileriYazdir();;



    }






    static void main(String[] args) {

    }


    // Müşteri listesini terminale yazdırma metodu
    private static void musterileriYazdir() {
        List<Musteri> musteriler = Veznedar.getMusteriler();
        System.out.println("\n========== MÜŞTERİ LİSTESİ ==========");
        System.out.println("Toplam Müşteri Sayısı: " + musteriler.size());
        System.out.println("----------------------------------------");

        if(musteriler.isEmpty()) {
            System.out.println("Henüz müşteri bulunmamaktadır.");
        } else {
            for(Musteri m : musteriler) {
                System.out.println("\nMüşteri ID: " + m.getMusteriId());
                System.out.println((m.getHesaplar().get(0)));

                if(!m.getHesaplar().isEmpty()) {
                    System.out.println("Hesaplar:");
                    for(Hesap h : m.getHesaplar()) {
                        m.paraYatir(h.getHesapId(),1000000);
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