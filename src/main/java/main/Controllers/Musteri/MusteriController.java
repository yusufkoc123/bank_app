package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import main.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

    public class MusteriController implements Initializable {
        public BorderPane musteri_parent;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle){
            Model.getInstance().getView().getmusterisecilenmenu().addListener((observableValue, eskideger, yenideger) ->{
                switch(yenideger){
                    case ISLEMLER-> musteri_parent.setCenter(Model.getInstance().getView().getIslemlerView());
                    case HESAPLARIM-> musteri_parent.setCenter(Model.getInstance().getView().getHesaplarimView());
                    default ->  musteri_parent.setCenter(Model.getInstance().getView().getAnasayfaView());
                }
            } );
        }
    }

