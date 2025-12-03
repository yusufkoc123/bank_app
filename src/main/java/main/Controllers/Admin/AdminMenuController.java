package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Models.Model;
import main.Views.AdminMenuOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button mkayit_btn;
    public Button musteriler_btn;
    public Button parayatir_btn;
    public Button acikis_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addListeners();
    }
    private void addListeners(){
        musteriler_btn.setOnAction(e -> mkayit());
        mkayit_btn.setOnAction(e -> musteriler());
        parayatir_btn.setOnAction(e -> parayatir());
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
}