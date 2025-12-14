package main.Controllers.Musteri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.Models.Islemler;
import main.Models.Model;
import main.Views.islemCellview;
import main.Musteri;
import main.Islem;
import main.dataStructures.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

public class islemlerController implements Initializable {
    public ListView<Islemler> islemler_lstview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        islemler_lstview.setCellFactory(listView -> new islemCellview());
        yukleIslemler();
    }

    private void yukleIslemler() {
        ObservableList<Islemler> islemler = FXCollections.observableArrayList();

        Musteri currentMusteri = Model.getInstance().getCurrentMusteri();
        
        if (currentMusteri != null && currentMusteri.getIslemler() != null) {
            ArrayList<Islem> domainIslemler = currentMusteri.getIslemler();
            
            // Tüm işlemleri göster (limit yok)
            for (int i = domainIslemler.size() - 1; i >= 0; i--) {
                Islem domainIslem = domainIslemler.get(i);
                Islemler islem = Islemler.fromDomainModel(domainIslem);
                if (islem != null) {
                    islemler.add(islem);
                }
            }
        }

        islemler_lstview.setItems(islemler);
    }
}
