package main.Controllers.Musteri;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Models.Model;
import main.Models.Islemler;
import main.Musteri;
import main.Hesap;
import main.Islem;
import main.Views.mainislemler_cellView;
import main.dataStructures.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;

public class anasayfaController implements Initializable {
    public Label vadesiz_bakiye_lbl;
    public Label vadesiz_id_lbl;
    public Label vadeli_id_lbl;
    public Label vadeli_bakiye_lbl;
    public ListView<Islemler> islem_listview;
    public TextField sm_hesapid_fld;
    public TextField sm_miktar_fld;
    public TextField sm_acıklama_fld;
    public Button sm_gonder_btn;
    public Label musteri_id_lbl;
    public Label musteri_ad_soyad_lbl;
    public Label sm_error;
    public ChoiceBox<Hesap> sm_gonderenhsp_chcbx;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Musteri current = Model.getInstance().getCurrentMusteri();

        // İşlemler ListView'ını ayarla
        islem_listview.setCellFactory(listView -> new mainislemler_cellView());
        yukleIslemler();

        if (current != null) {
            musteri_id_lbl.setText(String.valueOf(current.getMusteriId()));
            musteri_ad_soyad_lbl.setText(current.getAdi() + " " + current.getSoyad());

            Hesap vadesizHesap = null;
            Hesap vadeliHesap = null;

            ArrayList<Hesap> hesaplar = current.getHesaplar();
            for (int i = 0; i < hesaplar.size(); i++) {
                Hesap hesap = hesaplar.get(i);
                if (hesap.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    vadesizHesap = hesap;
                } else if (hesap.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    vadeliHesap = hesap;
                }
            }

            if (vadesizHesap != null) {
                vadesiz_id_lbl.setText(String.valueOf(vadesizHesap.getHesapId()));
                vadesiz_bakiye_lbl.setText(String.valueOf(vadesizHesap.getBakiye()));
            } else {
                vadesiz_id_lbl.setText("-");
                vadesiz_bakiye_lbl.setText("0");
            }

            if (vadeliHesap != null) {
                vadeli_id_lbl.setText(String.valueOf(vadeliHesap.getHesapId()));
                vadeli_bakiye_lbl.setText(String.valueOf(vadeliHesap.getBakiye()));
            } else {
                vadeli_id_lbl.setText("-");
                vadeli_bakiye_lbl.setText("0");
            }

            // Gönderen hesap ChoiceBox'ını doldur
            ObservableList<Hesap> hesapListesi = FXCollections.observableArrayList();
            main.dataStructures.ArrayList<Hesap> hesaplarList = current.getHesaplar();
            for (int i = 0; i < hesaplarList.size(); i++) {
                hesapListesi.add(hesaplarList.get(i));
            }
            sm_gonderenhsp_chcbx.setItems(hesapListesi);
            sm_gonderenhsp_chcbx.setConverter(new javafx.util.StringConverter<Hesap>() {
                @Override
                public String toString(Hesap hesap) {
                    if (hesap == null) {
                        return "";
                    }
                    return hesap.getHesapTuru().getHesapTuru() + " - " + hesap.getHesapId() + " (Bakiye: " + hesap.getBakiye() + " TL)";
                }

                @Override
                public Hesap fromString(String string) {
                    return null;
                }
            });
            // İlk hesabı varsayılan olarak seç
            if (!hesapListesi.isEmpty()) {
                sm_gonderenhsp_chcbx.setValue(hesapListesi.get(0));
            }
        }

        sm_gonder_btn.setOnAction(e -> {
            sm_error.setText(""); // Önceki hata mesajını temizle

            try {
                Musteri musteri = Model.getInstance().getCurrentMusteri();
                if (musteri == null) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Müşteri bilgisi bulunamadı!");
                    return;
                }

                if (musteri.getHesaplar().isEmpty()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Hesabınız bulunamadı!");
                    return;
                }

                // Gönderen hesabı kontrol et
                Hesap secilenGonderilenHesap = sm_gonderenhsp_chcbx.getValue();
                if (secilenGonderilenHesap == null) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Lütfen gönderen hesabı seçin!");
                    return;
                }

                String hesapIdStr = sm_hesapid_fld.getText().trim();
                String miktarStr = sm_miktar_fld.getText().trim();
                String aciklama = sm_acıklama_fld.getText().trim();

                if (hesapIdStr.isEmpty() || miktarStr.isEmpty()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Lütfen hesap numarası ve miktar alanlarını doldurun!");
                    return;
                }

                int gonderilecekHesapId;
                int miktar;
                try {
                    gonderilecekHesapId = Integer.parseInt(hesapIdStr);
                    miktar = Integer.parseInt(miktarStr);
                } catch (NumberFormatException ex) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Hesap ID ve miktar sayı olmalıdır!");
                    return;
                }

                int gonderilenHesapId = secilenGonderilenHesap.getHesapId();

                boolean kendiHesabinaGonderiyor = false;
                ArrayList<Hesap> hesaplarList = musteri.getHesaplar();
                for (int i = 0; i < hesaplarList.size(); i++) {
                    Hesap hesap = hesaplarList.get(i);
                    if (hesap.getHesapId() == gonderilecekHesapId) {
                        kendiHesabinaGonderiyor = true;
                        break;
                    }
                }
                if (kendiHesabinaGonderiyor) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Kendi hesabınıza para gönderemezsiniz!");
                    return;
                }

                if (miktar > secilenGonderilenHesap.getBakiye()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Yetersiz bakiye! Mevcut bakiye: " + secilenGonderilenHesap.getBakiye() + " TL");
                    return;
                }

                if (miktar <= 0) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Miktar 0'dan büyük olmalıdır!");
                    return;
                }
                if (miktar >70000) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Miktar 70.000'dan küçük olmalıdır!");
                    return;
                }

                // Açıklama varsa mesaja ekle
                String mesaj = "Para Transferi - Hesap ID: " + gonderilenHesapId + " -> " + gonderilecekHesapId;
                if (!aciklama.isEmpty()) {
                    mesaj += " - Açıklama: " + aciklama;
                }
                
                boolean basarili = musteri.paraGonder(gonderilenHesapId, gonderilecekHesapId, miktar, mesaj);

                if (basarili) {
                    sm_error.setTextFill(Color.GREEN);
                    sm_error.setText("İşlem başarılı!");
                    sm_hesapid_fld.clear();
                    sm_miktar_fld.clear();
                    sm_acıklama_fld.clear();

                    guncelleHesapBilgileri();
                    yukleIslemler(); // İşlemler listesini güncelle

                    PauseTransition pause = new PauseTransition(Duration.seconds(5));
                    pause.setOnFinished(event -> sm_error.setText(""));
                    pause.play();
                } else {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Para gönderilemedi! Hesap bulunamadı veya işlem başarısız.");
                }
            } catch (Exception ex) {
                sm_error.setTextFill(Color.RED);
                sm_error.setText("Bir hata oluştu: " + ex.getMessage());
            }
        });
    }

    private void guncelleHesapBilgileri() {
        Musteri current = Model.getInstance().getCurrentMusteri();

        if (current != null) {
            Hesap vadesizHesap = null;
            Hesap vadeliHesap = null;

            main.dataStructures.ArrayList<Hesap> hesaplar = current.getHesaplar();
            for (int i = 0; i < hesaplar.size(); i++) {
                Hesap hesap = hesaplar.get(i);
                if (hesap.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    vadesizHesap = hesap;
                } else if (hesap.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    vadeliHesap = hesap;
                }
            }

            if (vadesizHesap != null) {
                vadesiz_id_lbl.setText(String.valueOf(vadesizHesap.getHesapId()));
                vadesiz_bakiye_lbl.setText(String.valueOf(vadesizHesap.getBakiye()));
            } else {
                vadesiz_id_lbl.setText("-");
                vadesiz_bakiye_lbl.setText("0");
            }

            if (vadeliHesap != null) {
                vadeli_id_lbl.setText(String.valueOf(vadeliHesap.getHesapId()));
                vadeli_bakiye_lbl.setText(String.valueOf(vadeliHesap.getBakiye()));
            } else {
                vadeli_id_lbl.setText("-");
                vadeli_bakiye_lbl.setText("0");
            }

            // ChoiceBox'ı güncelle (bakiye değişikliklerini yansıtmak için)
            ObservableList<Hesap> hesapListesi = FXCollections.observableArrayList();
            main.dataStructures.ArrayList<Hesap> hesaplarList = current.getHesaplar();
            for (int i = 0; i < hesaplarList.size(); i++) {
                hesapListesi.add(hesaplarList.get(i));
            }
            Hesap seciliHesap = sm_gonderenhsp_chcbx.getValue();
            sm_gonderenhsp_chcbx.setItems(hesapListesi);
            if (seciliHesap != null) {
                // Aynı hesabı tekrar seç (ID'ye göre)
                javafx.collections.ObservableList<Hesap> items = hesapListesi;
                for (int i = 0; i < items.size(); i++) {
                    Hesap h = items.get(i);
                    if (h.getHesapId() == seciliHesap.getHesapId()) {
                        sm_gonderenhsp_chcbx.setValue(h);
                        break;
                    }
                }
            }
        }
    }

    private void yukleIslemler() {
        ObservableList<Islemler> islemler = FXCollections.observableArrayList();
        Musteri currentMusteri = Model.getInstance().getCurrentMusteri();

        if (currentMusteri != null && currentMusteri.getIslemler() != null) {
            ArrayList<Islem> domainIslemler = currentMusteri.getIslemler();
            final int MAX_ISLEM_SAYISI = 5; // Anasayfada sadece son 5 işlemi göster

            for (int i = 0; i < domainIslemler.size() && i < MAX_ISLEM_SAYISI; i++) {
                Islem domainIslem = domainIslemler.get(i);
                Islemler islem = Islemler.fromDomainModel(domainIslem);
                if (islem != null) {
                    islemler.add(islem);
                }
            }
        }
        islem_listview.setItems(islemler);
    }
}
