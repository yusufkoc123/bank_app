package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.Models.Islemler;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class mainislemler_cellController implements Initializable {
    public Label islem_tarih_lbl;
    public Label gönderen_ad_lbl;
    public Label alan_ad_lbl;
    public Label islem_miktar_lbl;
    public Label islem_birimi_lbl;
    private final Islemler islemler;

    public mainislemler_cellController(Islemler islemler) {
        this.islemler = islemler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (islemler != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            if (islemler.getDate() != null) {
                islem_tarih_lbl.setText(islemler.getDate().format(formatter));
            }
            gönderen_ad_lbl.textProperty().bind(islemler.senderProperty());
            alan_ad_lbl.textProperty().bind(islemler.receiverProperty());
            islem_miktar_lbl.textProperty().bind(islemler.miktarProperty());
        }
    }
}

