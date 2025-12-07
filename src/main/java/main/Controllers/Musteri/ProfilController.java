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
            profil_tel_lbl.setText(String.valueOf(musteri.getTelNo()));
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

        // Boş alan kontrolü
        if (mevcutSifre.isEmpty() || yeniSifre.isEmpty() || yeniSifreTekrar.isEmpty()) {
            sifre_error_lbl.setText("Lütfen tüm alanları doldurun!");
            return;
        }

        // Mevcut şifre kontrolü
        if (!mevcutSifre.equals(musteri.getMPassword())) {
            sifre_error_lbl.setText("Mevcut şifre yanlış!");
            return;
        }

        // Yeni şifre uzunluk kontrolü
        if (yeniSifre.length() < 4) {
            sifre_error_lbl.setText("Yeni şifre en az 4 karakter olmalıdır!");
            return;
        }

        // Şifre eşleşme kontrolü
        if (!yeniSifre.equals(yeniSifreTekrar)) {
            sifre_error_lbl.setText("Yeni şifreler eşleşmiyor!");
            return;
        }

        // Yeni şifre mevcut şifre ile aynı olamaz
        if (yeniSifre.equals(mevcutSifre)) {
            sifre_error_lbl.setText("Yeni şifre mevcut şifre ile aynı olamaz!");
            return;
        }

        // Şifreyi değiştir
        musteri.setMPassword(yeniSifre);

        // Başarı mesajı
        sifre_error_lbl.setTextFill(Color.GREEN);
        sifre_error_lbl.setText("Şifre başarıyla değiştirildi!");

        // Alanları temizle
        mevcut_sifre_fld.clear();
        yeni_sifre_fld.clear();
        yeni_sifre_tekrar_fld.clear();

        // 5 saniye sonra mesajı temizle
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> sifre_error_lbl.setText(""));
        pause.play();
    }
}
