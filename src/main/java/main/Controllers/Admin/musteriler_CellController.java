package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import main.Models.Musteri;
import main.Models.Model;
import main.Veznedar;
import main.dosyaIslemleri;

import java.net.URL;
import java.util.Optional;
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
        if (musteri != null) {
            mustericell_isim_lbl.textProperty().bind(musteri.isimProperty());
            mustericell_soyisim_lbl.textProperty().bind(musteri.soyisimProperty());
            mustericell_musteriID_lbl.textProperty().bind(musteri.musterIDProperty());
            mustericell_tc_lbl.textProperty().bind(musteri.tcnoProperty());
            mustericell_tel_lbl.textProperty().bind(musteri.telnoProperty());
            mustericell_kayittarih_lbl.textProperty().bind(musteri.kayittarihProperty());
            
            // Silme butonuna event handler ekle
            musteri_sil_btn.setOnAction(e -> musteriSil());
        }
    }
    
    private void musteriSil() {
        if (musteri == null) {
            return;
        }
        
        // Onay dialogu göster
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Müşteri Silme Onayı");
        alert.setHeaderText("Müşteriyi silmek istediğinizden emin misiniz?");
        alert.setContentText("Müşteri: " + musteri.getIsim() + " " + musteri.getSoyisim() + 
                           "\nMüşteri ID: " + musteri.getMusterID() + 
                           "\n\nBu işlem geri alınamaz!");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Müşteri ID'sini al ve int'e çevir
                int musteriId = Integer.parseInt(musteri.getMusterID());
                
                // Müşteriyi sil
                Veznedar.musteriSil(musteriId);
                
                // Dosyaya kaydet
                dosyaIslemleri.tumMusterileriKaydet();
                
                // Listeyi yenile
                Model.getInstance().getView().refreshMusterilerView();
                
                // Başarı mesajı
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Başarılı");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Müşteri başarıyla silindi!");
                successAlert.showAndWait();
            } catch (NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Hata");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Müşteri ID geçersiz!");
                errorAlert.showAndWait();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Hata");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Müşteri silinirken bir hata oluştu: " + e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }
}
