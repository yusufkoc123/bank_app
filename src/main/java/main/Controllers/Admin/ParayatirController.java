package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import main.Musteri;
import main.Hesap;
import main.Veznedar;
import main.dosyaIslemleri;
import main.dataStructures.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ParayatirController implements Initializable {

    public TextField deposit_search_fld;
    public Button deposit_search_btn;
    public Label pyc_soyad_lbl;
    public Label pyc_telno_lbl;
    public Label pyc_tcno_lbl;
    public Label pyc_ad_lbl;
    public Label pyc_adres_lbl;
    public Label pyc_musteriid_lbl;
    public Label pyc_bakiye_lbl;
    public Label pyc_hesapturu_lbl;
    public Label pyc_hesapid_lbl;
    public TextField pyc_miktar_fld;
    public Button pyc_topla_btn;
    public Button pyc_cek_btn;
    public Label pyc_error_lbl;
    
    private Musteri seciliMusteri;
    private Hesap seciliHesap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Arama butonu
        deposit_search_btn.setOnAction(e -> musteriAra());
        
        // Para Topla butonu
        pyc_topla_btn.setOnAction(e -> paraYatir());
        
        // Para Çek butonu
        pyc_cek_btn.setOnAction(e -> paraCek());
        
        // Miktar alanına sadece sayı girişi izin ver
        setupNumericField(pyc_miktar_fld);
        
        // Başlangıçta label'ları temizle
        if (pyc_error_lbl != null) {
            pyc_error_lbl.setText("");
        }
        temizle();
    }
    
    private void setupNumericField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("^[0-9]+$")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    private void musteriAra() {
        String hesapNoStr = deposit_search_fld.getText().trim();
        if (hesapNoStr.isEmpty()) {
            temizle();
            return;
        }
        
        try {
            int hesapId = Integer.parseInt(hesapNoStr);
            seciliMusteri = null;
            seciliHesap = null;
            
            // Tüm müşterilerde hesabı ara
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                ArrayList<Hesap> hesaplar = m.getHesaplar();
                for (int j = 0; j < hesaplar.size(); j++) {
                    Hesap h = hesaplar.get(j);
                    if (h.getHesapId() == hesapId) {
                        seciliMusteri = m;
                        seciliHesap = h;
                        break;
                    }
                }
                if (seciliMusteri != null) break;
            }
            
            if (seciliMusteri != null && seciliHesap != null) {
                // Müşteri bilgilerini göster
                pyc_ad_lbl.setText(seciliMusteri.getAdi());
                pyc_soyad_lbl.setText(seciliMusteri.getSoyad());
                pyc_tcno_lbl.setText(seciliMusteri.getTCkimlik());
                pyc_telno_lbl.setText(String.valueOf(seciliMusteri.getTelNo()));
                pyc_adres_lbl.setText(seciliMusteri.getAdres());
                pyc_musteriid_lbl.setText(String.format("%06d", seciliMusteri.getMusteriId()));
                
                // Hesap bilgilerini göster
                pyc_hesapid_lbl.setText(String.valueOf(seciliHesap.getHesapId()));
                pyc_bakiye_lbl.setText(String.valueOf(seciliHesap.getBakiye()));
                pyc_hesapturu_lbl.setText(seciliHesap.getHesapTuru().getHesapTuru());
                
                // Hata mesajını temizle
                pyc_error_lbl.setText("");
            } else {
                temizle();
                pyc_error_lbl.setText("Hesap bulunamadı!");
                pyc_error_lbl.setTextFill(Color.RED);
            }
        } catch (NumberFormatException e) {
            temizle();
            pyc_error_lbl.setText("Geçerli bir hesap numarası girin!");
            pyc_error_lbl.setTextFill(Color.RED);
        }
    }
    
    private void paraYatir() {
        pyc_error_lbl.setText("");
        if (seciliMusteri == null || seciliHesap == null) {
            pyc_error_lbl.setText("Önce bir hesap seçin!");
            pyc_error_lbl.setTextFill(Color.RED);
            return;
        }
        
        String miktarStr = pyc_miktar_fld.getText().trim();
        if (miktarStr.isEmpty()) {
            pyc_error_lbl.setText("Miktar girin!");
            pyc_error_lbl.setTextFill(Color.RED);
            return;
        }
        
        try {
            int miktar = Integer.parseInt(miktarStr);
            if (miktar <= 0) {
                pyc_error_lbl.setText("Miktar 0'dan büyük olmalıdır!");
                pyc_error_lbl.setTextFill(Color.RED);
                return;
            }
            
            // Para yatır
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                if (m.getMusteriId() == seciliMusteri.getMusteriId()) {
                    m.paraYatir(seciliHesap.getHesapId(), miktar);
                    // Güncel bakiyeyi al
                    ArrayList<Hesap> hesaplar = m.getHesaplar();
                    for (int j = 0; j < hesaplar.size(); j++) {
                        Hesap h = hesaplar.get(j);
                        if (h.getHesapId() == seciliHesap.getHesapId()) {
                            seciliHesap = h;
                            break;
                        }
                    }
                    break;
                }
            }
            
            // Bakiyeyi güncelle
            pyc_bakiye_lbl.setText(String.valueOf(seciliHesap.getBakiye()));
            
            // Verileri kaydet
            dosyaIslemleri.tumMusterileriKaydet();
            
            // Başarı mesajı
            pyc_error_lbl.setText("Para başarıyla yatırıldı!");
            pyc_error_lbl.setTextFill(Color.GREEN);
            pyc_miktar_fld.clear();
        } catch (NumberFormatException e) {
            pyc_error_lbl.setText("Geçerli bir miktar girin!");
            pyc_error_lbl.setTextFill(Color.RED);
        }
    }
    
    private void paraCek() {
        pyc_error_lbl.setText("");
        if (seciliMusteri == null || seciliHesap == null) {
            pyc_error_lbl.setText("Önce bir hesap seçin!");
            pyc_error_lbl.setTextFill(Color.RED);
            return;
        }
        
        String miktarStr = pyc_miktar_fld.getText().trim();
        if (miktarStr.isEmpty()) {
            pyc_error_lbl.setText("Miktar girin!");
            pyc_error_lbl.setTextFill(Color.RED);
            return;
        }
        
        try {
            int miktar = Integer.parseInt(miktarStr);
            if (miktar <= 0) {
                pyc_error_lbl.setText("Miktar 0'dan büyük olmalıdır!");
                pyc_error_lbl.setTextFill(Color.RED);
                return;
            }
            
            if (miktar > seciliHesap.getBakiye()) {
                pyc_error_lbl.setText("Yetersiz bakiye! Mevcut bakiye: " + seciliHesap.getBakiye() + " TL");
                pyc_error_lbl.setTextFill(Color.RED);
                return;
            }
            
            // Para çek
            ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
            for (int i = 0; i < musteriler.size(); i++) {
                Musteri m = musteriler.get(i);
                if (m.getMusteriId() == seciliMusteri.getMusteriId()) {
                    m.paraCek(seciliHesap.getHesapId(), miktar);
                    // Güncel bakiyeyi al
                    ArrayList<Hesap> hesaplar = m.getHesaplar();
                    for (int j = 0; j < hesaplar.size(); j++) {
                        Hesap h = hesaplar.get(j);
                        if (h.getHesapId() == seciliHesap.getHesapId()) {
                            seciliHesap = h;
                            break;
                        }
                    }
                    break;
                }
            }
            
            // Bakiyeyi güncelle
            pyc_bakiye_lbl.setText(String.valueOf(seciliHesap.getBakiye()));
            
            // Verileri kaydet
            dosyaIslemleri.tumMusterileriKaydet();
            
            // Başarı mesajı
            pyc_error_lbl.setText("Para başarıyla çekildi!");
            pyc_error_lbl.setTextFill(Color.GREEN);
            pyc_miktar_fld.clear();
        } catch (NumberFormatException e) {
            pyc_error_lbl.setText("Geçerli bir miktar girin!");
            pyc_error_lbl.setTextFill(Color.RED);
        }
    }
    
    private void temizle() {
        pyc_ad_lbl.setText("");
        pyc_soyad_lbl.setText("");
        pyc_tcno_lbl.setText("");
        pyc_telno_lbl.setText("");
        pyc_adres_lbl.setText("");
        pyc_musteriid_lbl.setText("");
        pyc_hesapid_lbl.setText("");
        pyc_bakiye_lbl.setText("");
        pyc_hesapturu_lbl.setText("");
        pyc_miktar_fld.clear();
        if (pyc_error_lbl != null) {
            pyc_error_lbl.setText("");
            pyc_error_lbl.setTextFill(Color.BLACK);
        }
        seciliMusteri = null;
        seciliHesap = null;
    }
}
