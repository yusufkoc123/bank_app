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
        if (!yeniSifre.equals(yeniSifreTekrar)) {
            hata_lbl.setText("Yeni şifreler eşleşmiyor!");
            return;
        }
        if (yeniSifre.length() < 4) {
            hata_lbl.setText("Şifre en az 4 karakter olmalıdır!");
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
}

