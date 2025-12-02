package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class MusteriMenuController implements Initializable {
    public Button anasayfa_btn;
    public Button islemler_btn;
    public Button hesaplarim_btn;
    public Button profil_btn;
    public Button cikis_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addListeners();
    }
    private void addListeners(){
        anasayfa_btn.setOnAction(e -> anasayfa());
        islemler_btn.setOnAction(e -> islemler());
        hesaplarim_btn.setOnAction(e -> hesaplarim());
    }

    private void anasayfa() {
        Model.getInstance().getView().getmusterisecilenmenu().set("Anasayfa");
    }
    private void islemler() {
        Model.getInstance().getView().getmusterisecilenmenu().set("İşlemler");
    }
    private void hesaplarim() {
        Model.getInstance().getView().getmusterisecilenmenu().set("Hesaplarım");
    }
}
