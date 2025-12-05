package main.Controllers.Musteri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.Models.Islemler;
import main.Models.Model;
import main.Views.islemCellview;

import java.net.URL;
import java.time.LocalDate;
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
        
        islemler.add(new Islemler("Ahmet Yılmaz", "Ayşe Demir", "5000", LocalDate.now().minusDays(2), "Para transferi"));
        islemler.add(new Islemler("Ayşe Demir", "Ahmet Yılmaz", "2500", LocalDate.now().minusDays(1), "Para transferi"));
        islemler.add(new Islemler("Mehmet Kaya", "Ahmet Yılmaz", "10000", LocalDate.now(), "Para transferi"));
        islemler.add(new Islemler("Ahmet Yılmaz", "Fatma Şahin", "7500", LocalDate.now().minusDays(5), "Para transferi"));
        islemler.add(new Islemler("Ayşe Demir", "Mehmet Kaya", "3000", LocalDate.now().minusDays(3), "Para transferi"));
        islemler.add(new Islemler("Ahmet Yılmaz", "Ayşe Demir", "5000", LocalDate.now().minusDays(2), "Para transferi"));
        islemler.add(new Islemler("Ayşe Demir", "Ahmet Yılmaz", "2500", LocalDate.now().minusDays(1), "Para transferi"));
        islemler.add(new Islemler("Mehmet Kaya", "Ahmet Yılmaz", "10000", LocalDate.now(), "Para transferi"));
        islemler.add(new Islemler("Ahmet Yılmaz", "Fatma Şahin", "7500", LocalDate.now().minusDays(5), "Para transferi"));
        islemler.add(new Islemler("Ayşe Demir", "Mehmet Kaya", "3000", LocalDate.now().minusDays(3), "Para transferi"));
        
        islemler_lstview.setItems(islemler);
    }
}
