package main.Controllers.Musteri;

import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Models.Model;
import main.Musteri;
import main.Hesap;

import java.net.URL;
import java.util.ResourceBundle;

public class anasayfaController implements Initializable {
    public Label vadesiz_bakiye_lbl;
    public Label vadesiz_id_lbl;
    public Label vadeli_id_lbl;
    public Label vadeli_bakiye_lbl;
    public ListView islem_listview;
    public TextField sm_hesapid_fld;
    public TextField sm_miktar_fld;
    public TextField sm_acıklama_fld;
    public Button sm_gonder_btn;
    public Label musteri_id_lbl;
    public Label musteri_ad_soyad_lbl;
    public Label sm_error;
    public ChoiceBox sm_gonderenhsp_chcbx;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Musteri current = Model.getInstance().getCurrentMusteri();

        if (current != null) {
            // ID label'i
            musteri_id_lbl.setText(String.valueOf(current.getMusteriId()));
            // Ad + soyad label'i
            musteri_ad_soyad_lbl.setText(current.getAdi() + " " + current.getSoyad());

            // Hesapları bul ve göster
            Hesap vadesizHesap = null;
            Hesap vadeliHesap = null;

            for (Hesap hesap : current.getHesaplar()) {
                if (hesap.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    vadesizHesap = hesap;
                } else if (hesap.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    vadeliHesap = hesap;
                }
            }

            // Vadesiz hesap bilgilerini göster
            if (vadesizHesap != null) {
                vadesiz_id_lbl.setText(String.valueOf(vadesizHesap.getHesapId()));
                vadesiz_bakiye_lbl.setText(String.valueOf(vadesizHesap.getBakiye()));
            } else {
                vadesiz_id_lbl.setText("-");
                vadesiz_bakiye_lbl.setText("0");
            }

            // Vadeli hesap bilgilerini göster
            if (vadeliHesap != null) {
                vadeli_id_lbl.setText(String.valueOf(vadeliHesap.getHesapId()));
                vadeli_bakiye_lbl.setText(String.valueOf(vadeliHesap.getBakiye()));
            } else {
                vadeli_id_lbl.setText("-");
                vadeli_bakiye_lbl.setText("0");
            }
        }

        // Para gönderme butonu event handler
        sm_gonder_btn.setOnAction(e -> {
            sm_error.setText(""); // Önceki hata mesajını temizle

            try {
                Musteri musteri = Model.getInstance().getCurrentMusteri();
                if (musteri == null) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Müşteri bilgisi bulunamadı!");
                    return;
                }

                // Hesap listesi boş mu kontrol et
                if (musteri.getHesaplar().isEmpty()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Hesabınız bulunamadı!");
                    return;
                }

                // TextField'lardan değerleri al
                String hesapIdStr = sm_hesapid_fld.getText().trim();
                String miktarStr = sm_miktar_fld.getText().trim();

                // Boş kontrolü
                if (hesapIdStr.isEmpty() || miktarStr.isEmpty()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Lütfen tüm alanları doldurun!");
                    return;
                }

                // Sayısal değer kontrolü
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

                // Gönderen hesap olarak müşterinin ilk hesabını kullan
                Hesap gonderilenHesap = musteri.getHesaplar().get(0);
                int gonderilenHesapId = gonderilenHesap.getHesapId();

                // Kendi hesabına gönderme kontrolü
                boolean kendiHesabinaGonderiyor = false;
                for (Hesap hesap : musteri.getHesaplar()) {
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

                // Miktarın bakiyeden küçük olması kontrolü (miktar pozitif kontrolünden önce)
                if (miktar > gonderilenHesap.getBakiye()) {
                    sm_error.setTextFill(Color.RED);
                    sm_error.setText("Yetersiz bakiye! Mevcut bakiye: " + gonderilenHesap.getBakiye() + " TL");
                    return;
                }

                // Miktar pozitif mi kontrol et
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

                // Para gönderme işlemi
                boolean basarili = musteri.paraGonder(gonderilenHesapId, gonderilecekHesapId, miktar);

                if (basarili) {
                    sm_error.setTextFill(Color.GREEN);
                    sm_error.setText("İşlem başarılı!");
                    sm_hesapid_fld.clear();
                    sm_miktar_fld.clear();
                    sm_acıklama_fld.clear();

                    // Bakiyeleri güncelle
                    guncelleHesapBilgileri();

                    // 5 saniye sonra mesajı temizle (UI donmayacak şekilde)
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

    // Hesap bilgilerini güncelleme metodu
    private void guncelleHesapBilgileri() {
        Musteri current = Model.getInstance().getCurrentMusteri();

        if (current != null) {
            Hesap vadesizHesap = null;
            Hesap vadeliHesap = null;

            for (Hesap hesap : current.getHesaplar()) {
                if (hesap.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    vadesizHesap = hesap;
                } else if (hesap.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    vadeliHesap = hesap;
                }
            }

            // Vadesiz hesap bilgilerini güncelle
            if (vadesizHesap != null) {
                vadesiz_id_lbl.setText(String.valueOf(vadesizHesap.getHesapId()));
                vadesiz_bakiye_lbl.setText(String.valueOf(vadesizHesap.getBakiye()));
            } else {
                vadesiz_id_lbl.setText("-");
                vadesiz_bakiye_lbl.setText("0");
            }

            // Vadeli hesap bilgilerini güncelle
            if (vadeliHesap != null) {
                vadeli_id_lbl.setText(String.valueOf(vadeliHesap.getHesapId()));
                vadeli_bakiye_lbl.setText(String.valueOf(vadeliHesap.getBakiye()));
            } else {
                vadeli_id_lbl.setText("-");
                vadeli_bakiye_lbl.setText("0");
            }
        }
    }
}
