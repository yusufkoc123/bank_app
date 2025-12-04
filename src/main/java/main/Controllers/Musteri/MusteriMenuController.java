package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.MusteriMenuOptions;

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
        cikis_btn.setOnAction(e -> m_cikis());
    }

    private void anasayfa() {
        Model.getInstance().getView().getmusterisecilenmenu().set(MusteriMenuOptions.ANASAYFA);
    }
    private void islemler() {

        Model.getInstance().getView().getmusterisecilenmenu().set(MusteriMenuOptions.ISLEMLER);
    }
    private void hesaplarim() {
        Model.getInstance().getView().getmusterisecilenmenu().set(MusteriMenuOptions.HESAPLARIM);
    }
    private void m_cikis() {
        Stage stage = (Stage) cikis_btn.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        Model.getInstance().getView().showLoginWindow();
    }
}
