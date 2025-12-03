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
                System.out.println("Ad Soyad: " + m.getAdi() + " " + m.getSoyad());
                System.out.println("TC Kimlik: " + m.getTCkimlik());
                System.out.println("Adres: " + m.getAdres());
                System.out.println("Telefon: " + m.getTelNo());
                System.out.println("Hesap Sayısı: " + m.getHesaplar().size());

                if(!m.getHesaplar().isEmpty()) {
                    System.out.println("Hesaplar:");
                    for(Hesap h : m.getHesaplar()) {
                        String hesapTuru = h.getHesapTuru().getHesapTuru();
                        System.out.println("  - Hesap ID: " + h.getHesapId() +
                                " | Tür: " + hesapTuru +
                                " | Bakiye: " + h.getBakiye() + " TL");
                    }
                }
                System.out.println("Toplam Bakiye: " + m.toplamBakiye() + " TL");
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================\n");
    }
}