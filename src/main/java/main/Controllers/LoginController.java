package main.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public PasswordField password_fld;
    public Button giris_btn;
    public Button sifremi_unuttum_btn;
    public Label error_lbl;
    public Label giris_lbl;
    public TextField giris_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        accountSellect.setItems(FXCollections.observableArrayList(HesapTuru.Musteri, HesapTuru.Veznedar));
        accountSellect.setValue(Model.getInstance().getView().getGiristuru());
        accountSellect.valueProperty().addListener(observable -> {
            Model.getInstance().getView().setGiristuru(accountSellect.getValue());
            updateLabelText();
        });
        updateLabelText();
        giris_btn.setOnAction(e->giris());
        sifremi_unuttum_btn.setOnAction(e -> sifremiUnuttum());
        error_lbl.setText("");
    }

    private void updateLabelText() {
        if (accountSellect.getValue() == HesapTuru.Musteri) {
            giris_lbl.setText("Müşteri ID:");
        } else if (accountSellect.getValue() == HesapTuru.Veznedar) {
            giris_lbl.setText("Kullanıcı Adı:");
        }
    }


    public void giris(){
        error_lbl.setText("");
        Stage stage = (Stage) error_lbl.getScene().getWindow();

        if (Model.getInstance().getView().getGiristuru() == null) {
            error_lbl.setText("Hesap Türü seçmelisiniz.");
            return;
        }



        if (Model.getInstance().getView().getGiristuru() == HesapTuru.Musteri) {
            if(mSifreKontrol()) {
                Model.getInstance().getView().closeStage(stage);
                Model.getInstance().getView().musteriWindow();
            } else {
                error_lbl.setText("Müşteri ID veya şifre yanlış!");
            }
        } else if (Model.getInstance().getView().getGiristuru() == HesapTuru.Veznedar){
//            if(vSifreKontrol()) {
                    Model.getInstance().getView().closeStage(stage);
                    Model.getInstance().getView().AdminWindow();
//            } else {
//                error_lbl.setText("Yönetici ID veya şifre yanlış!");
//            }
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

        if(!isEmpty(giris_fld) && !isEmpty(password_fld)){
            try {
                int musteriId = Integer.parseInt(smusteriId);
                List<Musteri> musteriler = Veznedar.getMusteriler();
                if(isRegistered(musteriId)) {
                    for (Musteri musteri : musteriler) {
                        if (musteri.getMusteriId() == musteriId && musteri.getMPassword().equals(sifre)) {
                            isPassword = true;
                            Model.getInstance().setCurrentMusteri(musteri);
                            break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return isPassword;
    }

    public boolean vSifreKontrol(){
        String sveznedarId = giris_fld.getText().trim();
        String sifre = password_fld.getText().trim();
        boolean isPassword = false;

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

    private void sifremiUnuttum() {
        try {
            Stage stage = (Stage) sifremi_unuttum_btn.getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Fxml/SifremiUnuttum.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
