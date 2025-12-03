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
        Model.getInstance().getView().getmusterisecilenmenu().addListener((observableValue, eskideger, yenideger) ->{
            switch(yenideger){
                case MUS-> Admin_parent.setCenter(Model.getInstance().getView().getParayatirView());
                case "Müşteriler"-> Admin_parent.setCenter(Model.getInstance().getView().getMusterilerView());
                default ->  Admin_parent.setCenter(Model.getInstance().getView().getMusterikayitView());
            }
        } );
    }
}
