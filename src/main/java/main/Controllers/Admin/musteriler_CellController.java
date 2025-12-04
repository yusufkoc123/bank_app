package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.Models.Musteri;

import java.net.URL;
import java.util.ResourceBundle;

public class musteriler_CellController implements Initializable {
    public Label mustericell_isim_lbl;
    public Label mustericell_soyisim_lbl;
    public Label mustericell_musteriID_lbl;
    public Button musteri_sil_btn;
    public Label mustericell_tc_lbl;
    public Label mustericell_tel_lbl;
    public Label mustericell_kayittarih_lbl;

    private final Musteri musteri;

    public musteriler_CellController(Musteri musteri) {
        this.musteri = musteri;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
