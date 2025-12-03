package main.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Bank;
import main.Models.Model;
import main.Musteri;
import main.Views.HesapTuru;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
public class LoginController implements Initializable {
    public ChoiceBox<HesapTuru> accountSellect;
    public TextField password_fld;
    public Button giris_btn;
    public Label error_lbl;
    public Label giris_lbl;
    public TextField giris_fld;
    Bank banka=new Bank(1,"İnüBank","Malatya");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        accountSellect.setItems(FXCollections.observableArrayList(HesapTuru.MUSTERI, HesapTuru.YONETICI));
        accountSellect.setValue(Model.getInstance().getView().getGiristuru());
        accountSellect.valueProperty().addListener(observable -> Model.getInstance().getView().setGiristuru(accountSellect.getValue()));
        giris_btn.setOnAction(e->giris());
    }


   /*  if(isEmpty(giris_fld) && isEmpty(password_fld)){
        error_lbl.setText("Lütfen müşteri id ve şifre alanlarını doldurun!");
    }else if(isEmpty(giris_fld)){
        error_lbl.setText("Müşteri id alanı boş olamaz!");
    }else if(isEmpty(password_fld)){
        error_lbl.setText("Şifre alanı boş olamaz!");
    }else if(mSifreKontrol()) {
        Model.getInstance().getView().showClientWindow();
    }else{
        error_lbl.setText("Müşteri id veya şifre yanlış!");
    }
    */

    public void giris(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        if (Model.getInstance().getView().getGiristuru()==HesapTuru.MUSTERI) {
            //musteri kontrolleri
            Model.getInstance().getView().musteriWindow();
        }
        else if (Model.getInstance().getView().getGiristuru()==HesapTuru.YONETICI){
            //admin kontrolleri
            Model.getInstance().getView().AdminWindow();
        }
        else {
            error_lbl.setText("Hesap Türü seçmelisiniz.");
        }
    }

    public boolean isRegistered(int musteriId){
        List<Musteri> musteriler=banka.getMusteriler();
        boolean isRegistered=false;
        for(Musteri musteri:musteriler){
            if(musteri.getMusteriId()==musteriId){
                isRegistered=true;
            }
        }
        return isRegistered;
    }
    public boolean mSifreKontrol(){
        String smusteriId=giris_fld.getText().trim();
        String sifre=password_fld.getText().trim();
        boolean isPassword=false;

        // Alanlar boş değilse devam et
        if(!isEmpty(giris_fld) && !isEmpty(password_fld)){
            try {
                int musteriId=Integer.parseInt(smusteriId);
                List<Musteri> musteriler=banka.getMusteriler();
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


    public boolean isEmpty(TextField fld){
        if(fld.getText().equals("")){
            return true;
        }
        return false;
    }
}
