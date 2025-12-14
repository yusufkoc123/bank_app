package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import main.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane Admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Model.getInstance().getView().getAdminsecilenmenu().addListener((observableValue, eskideger, yenideger) ->{
            switch(yenideger){
                case MUSTERILER-> Admin_parent.setCenter(Model.getInstance().getView().getMusterilerView());
                case PARA_YATIR -> Admin_parent.setCenter(Model.getInstance().getView().getParayatirView());
                case VEZNEDAR_EKLE -> Admin_parent.setCenter(Model.getInstance().getView().getVeznedarekleView());
                default ->  Admin_parent.setCenter(Model.getInstance().getView().getMusterikayitView());
            }
        } );
    }
}
