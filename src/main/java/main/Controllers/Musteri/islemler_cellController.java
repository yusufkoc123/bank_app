package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.Models.Islemler;
import main.Musteri;

import java.net.URL;
import java.util.ResourceBundle;

public class islemler_cellController implements Initializable {
    public Label islem_tarih_lbl;
    public Label gönderen_ad_lbl;
    public Label alan_ad_lbl;
    public Label islem_miktar_lbl;
    public Label islem_birimi_lbl;
    private final Islemler islemler;




    public  islemler_cellController(Islemler islemler) {
        this.islemler = islemler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
