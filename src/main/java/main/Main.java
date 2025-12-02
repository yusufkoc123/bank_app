package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.View;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class Main extends Application {
    @Override
    public void start(Stage stage)  {
        Bank banka=new Bank(1,"İnüBank","Malatya");
        Veznedar v=new Veznedar(31,"ibo","bakkal","123456");

        // İlk müşteri ve hesabı
        Musteri m1 = new Musteri(1, "Ahmet", "Yılmaz", "10547799978", "İstanbul", 5551234, "12345");
        Hesap a = new Hesap(1, 1001);
        m1.mHesapAc(a); // Hesabı müşteriye ekle
        v.musteriEkle(m1);
        m1.paraYatir(1001,10000000);
        // Yeni müşteri ve hesap ID'si 1002 olan hesap (para gönderme testi için)
        Musteri m2 = new Musteri(2, "Mehmet", "Demir", "12345678901", "Ankara", 5555678, "54321");
        Hesap b = new Hesap(2, 1002);
        m2.mHesapAc(b); // Hesabı müşteriye ekle
        v.musteriEkle(m2);

        Model.getInstance().getView().showLoginWindow();

    }

    static void main(String[] args) {
        Bank banka=new Bank(1,"İnüBank","Malatya");
        Veznedar v=new Veznedar(31,"ibo","bakkal","123456");

        // İlk müşteri ve hesabı
        Musteri m1 = new Musteri(1, "Ahmet", "Yılmaz", "10547799978", "İstanbul", 5551234, "12345");
        Hesap a = new Hesap(1, 1001);
        m1.mHesapAc(a); // Hesabı müşteriye ekle
        v.musteriEkle(m1);
        m1.paraYatir(1001,1000);

        // Yeni müşteri ve hesap ID'si 1002 olan hesap (para gönderme testi için)
        Musteri m2 = new Musteri(2, "Mehmet", "Demir", "12345678901", "Ankara", 5555678, "54321");
        Hesap b = new Hesap(2, 1002);
        m2.mHesapAc(b); // Hesabı müşteriye ekle
        v.musteriEkle(m2);

    }
}