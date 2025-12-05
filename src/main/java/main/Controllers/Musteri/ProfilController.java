package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import main.Models.Model;

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
    }
}
