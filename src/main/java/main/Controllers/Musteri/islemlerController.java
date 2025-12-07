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

        // Mevcut müşteriyi Model'den al
        Musteri currentMusteri = Model.getInstance().getCurrentMusteri();
        
        if (currentMusteri != null && currentMusteri.getIslemler() != null) {
            // Müşterinin işlemlerini domain model'den Islemler model'ine dönüştür
            ArrayList<Islem> domainIslemler = currentMusteri.getIslemler();
            int sayac = 0;
            final int MAX_ISLEM_SAYISI = 20;
            
            // Queue'dan gelen işlemleri al (son 20 işlem)
            for (int i = 0; i < domainIslemler.size(); i++) {
                if (sayac >= MAX_ISLEM_SAYISI) {
                    break; // 20 işlemden fazlasını gösterme
                }
                Islem domainIslem = domainIslemler.get(i);
                Islemler islem = Islemler.fromDomainModel(domainIslem);
                if (islem != null) {
                    islemler.add(islem);
                    sayac++;
                }
            }
        }

        islemler_lstview.setItems(islemler);
    }
}
