package main.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Musteri;
import main.Veznedar;

import java.net.URL;
import java.util.ResourceBundle;
import main.dataStructures.ArrayList;

public class SifremiUnuttumController implements Initializable {
    public TextField musteri_id_fld;
    public TextField tc_kimlik_fld;
    public PasswordField yeni_sifre_fld;
    public PasswordField yeni_sifre_tekrar_fld;
    public Button sifre_sifirla_btn;
    public Button geri_btn;
    public Label hata_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sifre_sifirla_btn.setOnAction(e -> sifreSifirla());
        geri_btn.setOnAction(e -> geriDon());
        hata_lbl.setText("");
    }

    private void sifreSifirla() {
        hata_lbl.setText("");
        
        String musteriIdText = musteri_id_fld.getText().trim();
        String tcText = tc_kimlik_fld.getText().trim();
        String yeniSifre = yeni_sifre_fld.getText().trim();
        String yeniSifreTekrar = yeni_sifre_tekrar_fld.getText().trim();

        if (musteriIdText.isEmpty() || tcText.isEmpty() || yeniSifre.isEmpty() || yeniSifreTekrar.isEmpty()) {
            hata_lbl.setText("Tüm alanları doldurun!");
            return;
        }
        
        if (tcText.length() != 11) {
            hata_lbl.setStyle("-fx-text-fill: red;");
            hata_lbl.setText("TC Kimlik No 11 haneli olmalıdır!");
            return;
        }
        if (!isSadeceRakam(tcText)) {
            hata_lbl.setStyle("-fx-text-fill: red;");
            hata_lbl.setText("TC Kimlik No sadece rakam içermelidir!");
            return;
        }
        if (!tcKimlikDogrula(tcText)) {
            hata_lbl.setStyle("-fx-text-fill: red;");
            hata_lbl.setText("Geçersiz TC Kimlik No! ");
            return;
        }
        if (!yeniSifre.equals(yeniSifreTekrar)) {
            hata_lbl.setText("Yeni şifreler eşleşmiyor!");
            return;
        }
        if (yeniSifre.length() < 8) {
            hata_lbl.setText("Şifre en az 8 karakter olmalıdır!");
            return;
        }
        
        String sifreHataMesaji = sifreGuvenlikKontrolu(yeniSifre);
        if (sifreHataMesaji != null) {
            hata_lbl.setStyle("-fx-text-fill: red;");
            hata_lbl.setText(sifreHataMesaji);
            return;
        }

        try {
            int musteriId = Integer.parseInt(musteriIdText);
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            
            boolean bulundu = false;
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri musteri = musteriler.get(i);
                if (musteri.getMusteriId() == musteriId && musteri.getTCkimlik().equals(tcText)) {
                    musteri.setMPassword(yeniSifre);
                    hata_lbl.setStyle("-fx-text-fill: green;");
                    hata_lbl.setText("Şifreniz başarıyla güncellendi!");
                    bulundu = true;
                    break;
                }
            }
            
            if (!bulundu) {
                hata_lbl.setStyle("-fx-text-fill: red;");
                hata_lbl.setText("Müşteri ID veya TC Kimlik No hatalı!");
            }
            
        } catch (NumberFormatException e) {
            hata_lbl.setStyle("-fx-text-fill: red;");
            hata_lbl.setText("Müşteri ID sayısal olmalıdır!");
        }
    }

    private void geriDon() {
        try {
            Stage stage = (Stage) geri_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

