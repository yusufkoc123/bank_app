package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AdminMenuOptions;
import main.dosyaIslemleri;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button mkayit_btn;
    public Button musteriler_btn;
    public Button parayatir_btn;
    public Button acikis_btn;
    public Button veznedarekle_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addListeners();
        yetkiKontrolu();
    }
    
    private void yetkiKontrolu() {
        main.Veznedar currentVeznedar = Model.getInstance().getCurrentVeznedar();
        if (currentVeznedar != null) {
            if (currentVeznedar.getYetki() != 1) {
                veznedarekle_btn.setVisible(false);
                veznedarekle_btn.setManaged(false);
            }
        }
    }
    private void addListeners(){
        musteriler_btn.setOnAction(e -> musteriler());
        mkayit_btn.setOnAction(e -> mkayit());
        parayatir_btn.setOnAction(e -> parayatir());
        acikis_btn.setOnAction(e -> cikis());
        veznedarekle_btn.setOnAction(e -> veznedarekle());
    }

    private void mkayit() {
        Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.MUSTERI_KAYIT);
    }
    private void musteriler() {
        Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.MUSTERILER);
    }
    private void parayatir() {
        Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.PARA_YATIR);
    }
    private void veznedarekle() {Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.VEZNEDAR_EKLE);
    }
    private void cikis() {
        dosyaIslemleri.tumVerileriKaydet();
        Stage stage = (Stage) acikis_btn.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        Model.getInstance().getView().showLoginWindow();
    }
}