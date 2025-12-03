package main.Controllers.Musteri;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Models.Model;
import main.Musteri;
import main.Hesap;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class hesaplarimController implements Initializable {
    public ChoiceBox<Hesap> hesapsecim_chcbx;
    public Button transfer_vadeli_btn;
    public TextField transfer_vadeli_fld;
    public Label bakiye2_hesaplarim_lbl;
    public Label hesap_id_hesaplarim_lbl;
    public Label islem_limit_hesaplarim_lbl;
    public Label open_date_hesaplarim_lbl;
    public Label bakiye_hesaplarim_lbl;
    public Label hesap_id2_hesaplarim_lbl;
    public Label cekim_limit_hesaplarim_lbl;
    public Label open_date2_hesaplarim_lbl;
    public TextField transfer_vadesiz_fld;
    public Button transfer_vadesiz_btn;

    private Hesap vadesizHesap;
    private Hesap vadeliHesap;

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle) {
        yukleHesapBilgileri();
        hesapsecim_chcbxDoldur();
        transferButonlariniBagla();
    }

    private void yukleHesapBilgileri() {
        Musteri current = Model.getInstance().getCurrentMusteri();

        if (current != null) {
            // Hesapları bul
            vadesizHesap = null;
            vadeliHesap = null;

            for (Hesap hesap : current.getHesaplar()) {
                if (hesap.getHesapTuru().getHesapTuru().equals("vadesiz")) {
                    vadesizHesap = hesap;
                } else if (hesap.getHesapTuru().getHesapTuru().equals("vadeli")) {
                    vadeliHesap = hesap;
                }
            }

            // Vadesiz hesap bilgilerini göster
            if (vadesizHesap != null) {
                hesap_id_hesaplarim_lbl.setText(String.valueOf(vadesizHesap.getHesapId()));
                bakiye_hesaplarim_lbl.setText("₺ " + vadesizHesap.getBakiye());
                islem_limit_hesaplarim_lbl.setText("10"); // Varsayılan işlem limiti
                open_date_hesaplarim_lbl.setText(tarihFormatla(new Date())); // Varsayılan tarih
            } else {
                hesap_id_hesaplarim_lbl.setText("-");
                bakiye_hesaplarim_lbl.setText("₺ 0");
                islem_limit_hesaplarim_lbl.setText("-");
                open_date_hesaplarim_lbl.setText("-");
            }

            // Vadeli hesap bilgilerini göster
            if (vadeliHesap != null) {
                hesap_id2_hesaplarim_lbl.setText(String.valueOf(vadeliHesap.getHesapId()));
                bakiye2_hesaplarim_lbl.setText("₺ " + vadeliHesap.getBakiye());
                cekim_limit_hesaplarim_lbl.setText("10"); // Varsayılan çekim limiti
                open_date2_hesaplarim_lbl.setText(tarihFormatla(new Date())); // Varsayılan tarih
            } else {
                hesap_id2_hesaplarim_lbl.setText("-");
                bakiye2_hesaplarim_lbl.setText("₺ 0");
                cekim_limit_hesaplarim_lbl.setText("-");
                open_date2_hesaplarim_lbl.setText("-");
            }
        }
    }

    private void hesapsecim_chcbxDoldur() {
        Musteri current = Model.getInstance().getCurrentMusteri();
        if (current != null) {
            hesapsecim_chcbx.getItems().clear();
            hesapsecim_chcbx.getItems().addAll(current.getHesaplar());

            // ChoiceBox için toString formatını ayarla
            hesapsecim_chcbx.setConverter(new javafx.util.StringConverter<Hesap>() {
                @Override
                public String toString(Hesap hesap) {
                    if (hesap == null) return "";
                    return hesap.getHesapTuru().getHesapTuru() + " - " + hesap.getHesapId();
                }

                @Override
                public Hesap fromString(String string) {
                    return null;
                }
            });
        }
    }

    private void transferButonlariniBagla() {
        // Vadesiz hesaptan vadeli hesaba transfer
        transfer_vadeli_btn.setOnAction(e -> {
            try {
                Musteri musteri = Model.getInstance().getCurrentMusteri();
                if (musteri == null) {
                    hataGoster("Müşteri bilgisi bulunamadı!");
                    return;
                }

                if (vadesizHesap == null || vadeliHesap == null) {
                    hataGoster("Vadesiz veya vadeli hesap bulunamadı!");
                    return;
                }

                String miktarStr = transfer_vadeli_fld.getText().trim();
                if (miktarStr.isEmpty()) {
                    hataGoster("Lütfen transfer miktarını girin!");
                    return;
                }

                int miktar;
                try {
                    miktar = Integer.parseInt(miktarStr);
                } catch (NumberFormatException ex) {
                    hataGoster("Miktar sayı olmalıdır!");
                    return;
                }

                if (miktar <= 0) {
                    hataGoster("Miktar 0'dan büyük olmalıdır!");
                    return;
                }

                if (miktar > vadesizHesap.getBakiye()) {
                    hataGoster("Yetersiz bakiye! Mevcut bakiye: " + vadesizHesap.getBakiye() + " TL");
                    return;
                }

                // Transfer işlemi
                boolean basarili = musteri.paraGonder(vadesizHesap.getHesapId(), vadeliHesap.getHesapId(), miktar);

                if (basarili) {
                    System.out.println("Transfer başarılı!");
                    transfer_vadeli_fld.clear();
                    yukleHesapBilgileri();
                } else {
                    hataGoster("Transfer başarısız!");
                }
            } catch (Exception ex) {
                hataGoster("Bir hata oluştu: " + ex.getMessage());
            }
        });

        // Vadeli hesaptan vadesiz hesaba transfer
        transfer_vadesiz_btn.setOnAction(e -> {
            try {
                Musteri musteri = Model.getInstance().getCurrentMusteri();
                if (musteri == null) {
                    hataGoster("Müşteri bilgisi bulunamadı!");
                    return;
                }

                if (vadesizHesap == null || vadeliHesap == null) {
                    hataGoster("Vadesiz veya vadeli hesap bulunamadı!");
                    return;
                }

                String miktarStr = transfer_vadesiz_fld.getText().trim();
                if (miktarStr.isEmpty()) {
                    hataGoster("Lütfen transfer miktarını girin!");
                    return;
                }

                int miktar;
                try {
                    miktar = Integer.parseInt(miktarStr);
                } catch (NumberFormatException ex) {
                    hataGoster("Miktar sayı olmalıdır!");
                    return;
                }

                if (miktar <= 0) {
                    hataGoster("Miktar 0'dan büyük olmalıdır!");
                    return;
                }

                if (miktar > vadeliHesap.getBakiye()) {
                    hataGoster("Yetersiz bakiye! Mevcut bakiye: " + vadeliHesap.getBakiye() + " TL");
                    return;
                }

                // Transfer işlemi
                boolean basarili = musteri.paraGonder(vadeliHesap.getHesapId(), vadesizHesap.getHesapId(), miktar);

                if (basarili) {
                    System.out.println("Transfer başarılı!");
                    transfer_vadesiz_fld.clear();
                    yukleHesapBilgileri();
                } else {
                    hataGoster("Transfer başarısız!");
                }
            } catch (Exception ex) {
                hataGoster("Bir hata oluştu: " + ex.getMessage());
            }
        });
    }

    private void hataGoster(String mesaj) {
        System.err.println("Hata: " + mesaj);
    }

    private String tarihFormatla(Date tarih) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(tarih);
    }
}
