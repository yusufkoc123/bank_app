package main.Controllers.Musteri;

import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Models.Model;
import main.Musteri;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {
    public Label profil_adi_lbl;
    public Label profil_soyadi_lbl;
    public Label profil_musteriid_lbl;
    public Label profil_tc_lbl;
    public Label profil_tel_lbl;
    public Label profil_adres_lbl;
    public PasswordField mevcut_sifre_fld;
    public PasswordField yeni_sifre_fld;
    public PasswordField yeni_sifre_tekrar_fld;
    public Button sifre_degistir_btn;
    public Label sifre_error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yukleProfilBilgileri();
        sifre_degistir_btn.setOnAction(e -> sifreDegistir());
    }

    private void yukleProfilBilgileri() {
        main.Musteri musteri = Model.getInstance().getCurrentMusteri();
        if (musteri != null) {
            profil_adi_lbl.setText(musteri.getAdi());
            profil_soyadi_lbl.setText(musteri.getSoyad());
            profil_musteriid_lbl.setText(String.valueOf(musteri.getMusteriId()));
            profil_tc_lbl.setText(musteri.getTCkimlik());
            profil_tel_lbl.setText(musteri.getTelNo() != null ? musteri.getTelNo() : "");
            profil_adres_lbl.setText(musteri.getAdres());
        }
    }

    private void sifreDegistir() {
        sifre_error_lbl.setText("");
        sifre_error_lbl.setTextFill(Color.RED);

        Musteri musteri = Model.getInstance().getCurrentMusteri();
        if (musteri == null) {
            sifre_error_lbl.setText("Müşteri bilgisi bulunamadı!");
            return;
        }

        String mevcutSifre = mevcut_sifre_fld.getText();
        String yeniSifre = yeni_sifre_fld.getText();
        String yeniSifreTekrar = yeni_sifre_tekrar_fld.getText();

        if (mevcutSifre.isEmpty() || yeniSifre.isEmpty() || yeniSifreTekrar.isEmpty()) {
            sifre_error_lbl.setText("Lütfen tüm alanları doldurun!");
            return;
        }

        if (!mevcutSifre.equals(musteri.getMPassword())) {
            sifre_error_lbl.setText("Mevcut şifre yanlış!");
            return;
        }

        if (yeniSifre.length() < 8) {
            sifre_error_lbl.setText("Yeni şifre en az 8 karakter olmalıdır!");
            return;
        }
        
        String sifreHataMesaji = sifreGuvenlikKontrolu(yeniSifre);
        if (sifreHataMesaji != null) {
            sifre_error_lbl.setText(sifreHataMesaji);
            return;
        }

        if (!yeniSifre.equals(yeniSifreTekrar)) {
            sifre_error_lbl.setText("Yeni şifreler eşleşmiyor!");
            return;
        }

        if (yeniSifre.equals(mevcutSifre)) {
            sifre_error_lbl.setText("Yeni şifre mevcut şifre ile aynı olamaz!");
            return;
        }

        musteri.setMPassword(yeniSifre);

        sifre_error_lbl.setTextFill(Color.GREEN);
        sifre_error_lbl.setText("Şifre başarıyla değiştirildi!");

        mevcut_sifre_fld.clear();
        yeni_sifre_fld.clear();
        yeni_sifre_tekrar_fld.clear();

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> sifre_error_lbl.setText(""));
        pause.play();
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
