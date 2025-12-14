package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Veznedar;
import main.dosyaIslemleri;

import java.net.URL;
import java.util.ResourceBundle;

public class veznedarekleController implements Initializable {
    public TextField username_fld;
    public TextField vznad_fld;
    public TextField vznsoyad_fld;
    public TextField vzntc_fld;
    public TextField vzntel_fld;
    public PasswordField vznsifre_fld;
    public Button kaydet_btn;
    public Label hata_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kaydet_btn.setOnAction(e -> veznedarKaydet());
        hata_lbl.setText("");
    }

    private void veznedarKaydet() {
        hata_lbl.setText("");
        
        String userName = username_fld.getText().trim();
        String ad = vznad_fld.getText().trim();
        String soyad = vznsoyad_fld.getText().trim();
        String sifre = vznsifre_fld.getText().trim();
        String tcNo = vzntc_fld.getText().trim();
        String telNo = vzntel_fld.getText().trim();
        
        if (userName.isEmpty()) {
            hataGoster("Kullanıcı adı boş olamaz!");
            return;
        }
        
        if (Veznedar.veznedarIdKullaniliyor(userName)) {
            hataGoster("Bu kullanıcı adı zaten kullanılıyor!");
            return;
        }
        
        if (ad.isEmpty()) {
            hataGoster("Ad boş olamaz!");
            return;
        }
        
        if (soyad.isEmpty()) {
            hataGoster("Soyad boş olamaz!");
            return;
        }
        
        if (sifre.isEmpty()) {
            hataGoster("Şifre boş olamaz!");
            return;
        }
        
        if (sifre.length() < 8) {
            hataGoster("Şifre en az 8 karakter olmalıdır!");
            return;
        }
        
        String sifreHataMesaji = sifreGuvenlikKontrolu(sifre);
        if (sifreHataMesaji != null) {
            hataGoster(sifreHataMesaji);
            return;
        }
        
        if (!tcNo.isEmpty()) {
            if (tcNo.length() != 11) {
                hataGoster("TC Kimlik No 11 haneli olmalıdır!");
                return;
            }
            if (!isSadeceRakam(tcNo)) {
                hataGoster("TC Kimlik No sadece rakam içermelidir!");
                return;
            }
            if (!tcKimlikDogrula(tcNo)) {
                hataGoster("Geçersiz TC Kimlik No!");
                return;
            }
        }
        
        if (!telNo.isEmpty()) {
            if (!isSadeceRakam(telNo)) {
                hataGoster("Telefon numarası sadece rakam içermelidir!");
                return;
            }
            if (telNo.length() != 10) {
                hataGoster("Telefon numarası 10 haneli olmalıdır!");
                return;
            }
        }
        
        Veznedar.veznedarEkle(userName, ad, soyad, sifre, tcNo, telNo);
        
        dosyaIslemleri.tumVeznedarlariKaydet();
        
        basariliMesajGoster("Veznedar başarıyla eklendi! Yetki seviyesi: 2");
        
        temizle();
    }
    
    private void hataGoster(String mesaj) {
        hata_lbl.setText(mesaj);
        hata_lbl.setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");
    }
    
    private void basariliMesajGoster(String mesaj) {
        hata_lbl.setText(mesaj);
        hata_lbl.setStyle("-fx-text-fill: #00aa00; -fx-font-weight: bold;");
    }
    
    private void temizle() {
        username_fld.clear();
        vznad_fld.clear();
        vznsoyad_fld.clear();
        vzntc_fld.clear();
        vzntel_fld.clear();
        vznsifre_fld.clear();
    }
    
    private boolean isSadeceRakam(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
    private boolean tcKimlikDogrula(String tcKimlik) {
        if (tcKimlik == null || tcKimlik.length() != 11) {
            return false;
        }
        
        int toplam = 0;
        for (int i = 0; i < 10; i++) {
            toplam += Character.getNumericValue(tcKimlik.charAt(i));
        }
        
        int kalan = toplam % 10;
        
        int sonBasamak = Character.getNumericValue(tcKimlik.charAt(10));
        
        return kalan == sonBasamak;
    }
    
    private String sifreGuvenlikKontrolu(String sifre) {
        if (sifre.length() < 8) {
            return "Şifre en az 8 karakter olmalıdır!";
        }
        
        boolean hasKucukHarf = false;
        boolean hasBuyukHarf = false;
        boolean hasRakam = false;
        boolean hasNoktalama = false;
        
        String noktalamaIsaretleri = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        for (int i = 0; i < sifre.length(); i++) {
            char c = sifre.charAt(i);
            if (c >= 'a' && c <= 'z') {
                hasKucukHarf = true;
            } else if (c >= 'A' && c <= 'Z') {
                hasBuyukHarf = true;
            } else if (c >= '0' && c <= '9') {
                hasRakam = true;
            } else if (noktalamaIsaretleri.indexOf(c) >= 0) {
                hasNoktalama = true;
            }
        }
        
        if (!hasKucukHarf) {
            return "Şifre en az 1 küçük harf içermelidir!";
        }
        
        if (!hasBuyukHarf) {
            return "Şifre en az 1 büyük harf içermelidir!";
        }
        
        if (!hasRakam) {
            return "Şifre en az 1 rakam içermelidir!";
        }
        
        if (!hasNoktalama) {
            return "Şifre en az 1 noktalama işareti içermelidir!";
        }
        
        return null;
    }
}
