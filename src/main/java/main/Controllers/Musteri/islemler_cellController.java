package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import main.Models.Islemler;
import main.Models.Model;
import main.Musteri;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class islemler_cellController implements Initializable {
    public Label islem_tarih_lbl;
    public Label gonderen_ad_lbl;
    public Label alan_ad_lbl;
    public Label islem_miktar_lbl;
    public Label islem_birimi_lbl;
    public Label islem_mesaj_lbl;
    private final Islemler islemler;

    public islemler_cellController(Islemler islemler) {
        this.islemler = islemler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (islemler != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            if (islemler.getDate() != null) {
                islem_tarih_lbl.setText(islemler.getDate().format(formatter));
            }
            gonderen_ad_lbl.textProperty().bind(islemler.senderProperty());
            alan_ad_lbl.textProperty().bind(islemler.receiverProperty());
            islem_mesaj_lbl.textProperty().bind(islemler.messageProperty());
            islem_mesaj_lbl.visibleProperty().bind(
                javafx.beans.binding.Bindings.createBooleanBinding(
                    () -> islemler.getMessage() != null && !islemler.getMessage().isEmpty(),
                    islemler.messageProperty()
                )
            );
            
            islemler.miktarProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    islem_miktar_lbl.setText(newVal);
                    miktarRenginiAyarla();
                }
            });
            if (islemler.getMiktar() != null) {
                islem_miktar_lbl.setText(islemler.getMiktar());
            }
            
            miktarRenginiAyarla();
        }
    }
    
    private void miktarRenginiAyarla() {
        Musteri currentMusteri = Model.getInstance().getCurrentMusteri();
        if (currentMusteri == null || islem_miktar_lbl == null) return;
        
        String musteriAdi = currentMusteri.getAdi() + " " + currentMusteri.getSoyad();
        String islemTuru = islemler.getIslemTuru();
        String gonderici = islemler.getSender();
        String alici = islemler.getReceiver();
        
        boolean paraGeliyor = false;
        
        if (musteriAdi.equals(alici)) {
            paraGeliyor = true;
        } else if (musteriAdi.equals(gonderici)) {
            paraGeliyor = false;
        } else {
            if (islemTuru != null) {
                if ("Para Yatırma".equals(islemTuru)) {
                    paraGeliyor = "Banka".equals(gonderici);
                } else if ("Para Çekme".equals(islemTuru)) {
                    paraGeliyor = false;
                } else if ("Para Transferi".equals(islemTuru)) {
                    paraGeliyor = musteriAdi.equals(alici);
                }
            }
        }
        
        String baseStyle = "-fx-background-radius: 2; -fx-font-size: 1.4em; -fx-padding: 5; -fx-font-weight: 700;";
        String birimBaseStyle = "-fx-background-radius: 5; -fx-font-size: 2em; -fx-padding: 5; -fx-font-weight: 700;";
        
        if (paraGeliyor) {
            islem_miktar_lbl.setStyle(baseStyle + " -fx-text-fill: #00aa00;");
            if (islem_birimi_lbl != null) {
                islem_birimi_lbl.setStyle(birimBaseStyle + " -fx-text-fill: #00aa00;");
            }
        } else {
            islem_miktar_lbl.setStyle(baseStyle + " -fx-text-fill: #ff0000;");
            if (islem_birimi_lbl != null) {
                islem_birimi_lbl.setStyle(birimBaseStyle + " -fx-text-fill: #ff0000;");
            }
        }
    }
}
