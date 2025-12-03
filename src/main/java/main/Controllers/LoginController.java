package main.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Models.Model;
import main.Musteri;
import main.Veznedar;
import main.Views.HesapTuru;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<HesapTuru> accountSellect;
    public TextField password_fld;
    public Button giris_btn;
    public Label error_lbl;
    public Label giris_lbl;
    public TextField giris_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        accountSellect.setItems(FXCollections.observableArrayList(HesapTuru.MUSTERI, HesapTuru.YONETICI));
        accountSellect.setValue(Model.getInstance().getView().getGiristuru());
        accountSellect.valueProperty().addListener(observable -> {
            Model.getInstance().getView().setGiristuru(accountSellect.getValue());
            updateLabelText();
        });
        updateLabelText(); // İlk yüklemede label'ı güncelle
        giris_btn.setOnAction(e->giris());
        error_lbl.setText("");
    }

    private void updateLabelText() {
        if (accountSellect.getValue() == HesapTuru.MUSTERI) {
            giris_lbl.setText("Müşteri ID:");
        } else if (accountSellect.getValue() == HesapTuru.YONETICI) {
            giris_lbl.setText("Kullanıcı Adı:");
        }
    }


    public void giris(){
        error_lbl.setText("");
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        if (Model.getInstance().getView().getGiristuru() == null) {
            error_lbl.setText("Hesap Türü seçmelisiniz.");
            return;
        }
/*
        if(isEmpty(giris_fld) && isEmpty(password_fld)){
            error_lbl.setText("Lütfen ID ve şifre alanlarını doldurun!");
            return;
        } else if(isEmpty(giris_fld)){
            error_lbl.setText("ID alanı boş olamaz!");
            return;
        } else if(isEmpty(password_fld)){
            error_lbl.setText("Şifre alanı boş olamaz!");
            return;
        }*/

        if (Model.getInstance().getView().getGiristuru() == HesapTuru.MUSTERI) {

            Model.getInstance().getView().musteriWindow();
           /* if(mSifreKontrol()) {
                Model.getInstance().getView().musteriWindow();
            } else {
                error_lbl.setText("Müşteri ID veya şifre yanlış!");
            }*/
        } else if (Model.getInstance().getView().getGiristuru() == HesapTuru.YONETICI){

            Model.getInstance().getView().AdminWindow();

            /* if(vSifreKontrol()) {
                Model.getInstance().getView().AdminWindow();
            } else {
                error_lbl.setText("Veznedar ID veya şifre yanlış!");
            }*/
        }
    }

    public boolean isRegistered(int musteriId){
        List<Musteri> musteriler = Veznedar.getMusteriler();
        for(Musteri musteri : musteriler){
            if(musteri.getMusteriId() == musteriId){
                return true;
            }
        }
        return false;
    }
    public boolean mSifreKontrol(){
        String smusteriId = giris_fld.getText().trim();
        String sifre = password_fld.getText().trim();
        boolean isPassword = false;

        // Alanlar boş değilse devam et
        if(!isEmpty(giris_fld) && !isEmpty(password_fld)){
            try {
                int musteriId = Integer.parseInt(smusteriId);
                List<Musteri> musteriler = Veznedar.getMusteriler();
                if(isRegistered(musteriId)) {
                    for (Musteri musteri : musteriler) {
                        if (musteri.getMusteriId() == musteriId && musteri.getMPassword().equals(sifre)) {
                            isPassword = true;
                            // Başarılı giriş yapan müşteriyi modele kaydet
                            Model.getInstance().setCurrentMusteri(musteri);
                            break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // Müşteri ID sayısal değilse false döner
                return false;
            }
        }
        return isPassword;
    }

    public boolean vSifreKontrol(){
        String sveznedarId = giris_fld.getText().trim();
        String sifre = password_fld.getText().trim();
        boolean isPassword = false;

        // Alanlar boş değilse devam et
        if(!isEmpty(giris_fld) && !isEmpty(password_fld)){
            try {
                int veznedarId = Integer.parseInt(sveznedarId);
                List<Veznedar> veznedarlar = Veznedar.getVeznedarlar();
                for (Veznedar veznedar : veznedarlar) {
                    if (veznedar.getTellerId() == veznedarId && veznedar.getVPassword().equals(sifre)) {
                        isPassword = true;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                // Veznedar ID sayısal değilse false döner
                return false;
            }
        }
        return isPassword;
    }


    public boolean isEmpty(TextField fld){
        if(fld.getText().trim().equals("")){
            return true;
        }
        return false;
    }
}
