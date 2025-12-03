package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import main.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Model.getInstance().getView().getadminsecilenmenu().addListener((observableValue, eskideger, yenideger) ->{
            switch(yenideger){
                case "Para Yatır"-> admin_parent.setCenter(Model.getInstance().getView().getParayatirView());
                case "Müşteriler"-> admin_parent.setCenter(Model.getInstance().getView().getMusterilerView());
                default ->  admin_parent.setCenter(Model.getInstance().getView().getMusterikayitView());
            }
        } );
    }
}
