package main.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import main.Musteri;
import main.Veznedar;
import main.dosyaIslemleri;
import main.dataStructures.ArrayList;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MusteriKayitController implements Initializable {
    public TextField mkisim_fld;
    public TextField mksoyisim_fld;
    public TextField mksifre_fld;
    public Label mkmusterino_lbl;
    public CheckBox vadesiz_box;
    public TextField vadesizbakiye_fld;
    public CheckBox vadeli_box;
    public TextField vadelibakiye_fld;
    public Button register_btn;
    public Label mk_error_lbl;
    public TextField mkadres_fld;
    public TextField mktc_fld;
    public TextField mktel_fld;

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 50;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        register_btn.setOnAction(e -> musteriKaydet());
        mk_error_lbl.setText(null);
        mkmusterino_lbl.setText(null);
        
        // Checkbox değişikliklerinde bakiye alanlarını aktif/pasif yap
        vadesiz_box.setOnAction(e -> {
            vadesizbakiye_fld.setDisable(!vadesiz_box.isSelected());
            if (vadesiz_box.isSelected()) {
                vadesizbakiye_fld.clear();
            }
        });
        vadeli_box.setOnAction(e -> {
            vadelibakiye_fld.setDisable(!vadeli_box.isSelected());
            if (vadeli_box.isSelected()) {
                vadelibakiye_fld.clear();
            }
        });
        
        // Başlangıçta bakiye alanlarını pasif yap
        vadesizbakiye_fld.setDisable(true);
        vadelibakiye_fld.setDisable(true);
        
        // İsim ve soyisim alanlarına sadece harf girişi izin ver
        setupNameField(mkisim_fld);
        setupNameField(mksoyisim_fld);
        
        // Bakiye alanlarına sadece sayı girişi izin ver
        setupNumericField(vadesizbakiye_fld);
        setupNumericField(vadelibakiye_fld);
        
        // TC ve telefon alanlarına sadece rakam girişi izin ver
        setupNumericOnlyField(mktc_fld);
        setupNumericOnlyFieldWithMaxLength(mktel_fld, 10);
        
        // Form alanları değiştiğinde müşteri ID'sini oluştur
        mkisim_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
        mksoyisim_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
        mktc_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
    }
    
    private int geciciMusteriId = -1;
    
    private void musteriIdOlustur() {
        String isim = mkisim_fld.getText().trim();
        String soyisim = mksoyisim_fld.getText().trim();
        String tc = mktc_fld.getText().trim();
        
        // Temel bilgiler girildiyse ID oluştur
        if (!isim.isEmpty() && !soyisim.isEmpty() && !tc.isEmpty() && tc.length() == 11) {
            try {
                // Geçici müşteri oluştur (sadece ID için, listeye eklenmeden)
                String adres = mkadres_fld.getText().trim();
                String telNoStr = mktel_fld.getText().trim();
                int telNo = telNoStr.isEmpty() ? 0 : Integer.parseInt(telNoStr);
                String sifre = mksifre_fld.getText().isEmpty() ? "temp123" : mksifre_fld.getText();
                
                // Geçici müşteri oluştur
                Musteri geciciMusteri = new Musteri(isim, soyisim, tc, adres, telNo, sifre);
                geciciMusteriId = geciciMusteri.getMusteriId();
                
                // ID'yi göster
                mkmusterino_lbl.setText(String.format("%06d", geciciMusteriId));
                
                // Geçici müşteriyi listeden çıkar (eğer eklenmişse)
                ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
                for (int i = 0; i < musteriler.size(); i++) {
                    Musteri m = musteriler.get(i);
                    if (m.getMusteriId() == geciciMusteriId && m.getMPassword().equals(sifre)) {
                        musteriler.removeAt(i);
                        break;
                    }
                }
            } catch (Exception e) {
                // Hata durumunda ID gösterme
                mkmusterino_lbl.setText("");
            }
        } else {
            mkmusterino_lbl.setText("");
            geciciMusteriId = -1;
        }
    }
    
    // İsim/soyisim alanları için sadece harf girişi
    private void setupNameField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            for (int i = 0; i < newText.length(); i++) {
                char c = newText.charAt(i);
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || 
                      c == 'ç' || c == 'ğ' || c == 'ı' || c == 'ö' || c == 'ş' || c == 'ü' ||
                      c == 'Ç' || c == 'Ğ' || c == 'İ' || c == 'Ö' || c == 'Ş' || c == 'Ü' ||
                      c == ' ')) {
                    return null;
                }
            }
            return change;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    // Bakiye alanları için sadece sayı girişi
    private void setupNumericField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            boolean hasDot = false;
            for (int i = 0; i < newText.length(); i++) {
                char c = newText.charAt(i);
                if (c >= '0' && c <= '9') {
                    continue;
                } else if (c == '.' && !hasDot) {
                    hasDot = true;
                } else {
                    return null;
                }
            }
            return change;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    // TC ve telefon için sadece rakam girişi
    private void setupNumericOnlyField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            for (int i = 0; i < newText.length(); i++) {
                char c = newText.charAt(i);
                if (c < '0' || c > '9') {
                    return null;
                }
            }
            return change;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    // Telefon için sadece rakam girişi ve maksimum uzunluk sınırlaması
    private void setupNumericOnlyFieldWithMaxLength(TextField field, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            if (newText.length() > maxLength) {
                return null;
            }
            for (int i = 0; i < newText.length(); i++) {
                char c = newText.charAt(i);
                if (c < '0' || c > '9') {
                    return null;
                }
            }
            return change;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    private void musteriKaydet() {
        mk_error_lbl.setText("");
        mk_error_lbl.setStyle("-fx-text-fill: red;"); // Hata mesajları için kırmızı renk
        
        // İsim validasyonu
        String isim = mkisim_fld.getText().trim();
        if (isim.isEmpty()) {
            mk_error_lbl.setText("İsim alanı boş olamaz!");
            mkisim_fld.requestFocus();
            return;
        }
        if (isim.length() < 3) {
            mk_error_lbl.setText("İsim en az 3 karakter olmalıdır!");
            mkisim_fld.requestFocus();
            return;
        }
        if (isim.length() > 50) {
            mk_error_lbl.setText("İsim en fazla 50 karakter olabilir!");
            mkisim_fld.requestFocus();
            return;
        }
        if (!isSadeceHarf(isim)) {
            mk_error_lbl.setText("İsim sadece harf içermelidir!");
            mkisim_fld.requestFocus();
            return;
        }
        
        // Soyisim validasyonu
        String soyisim = mksoyisim_fld.getText().trim();
        if (soyisim.isEmpty()) {
            mk_error_lbl.setText("Soyisim alanı boş olamaz!");
            mksoyisim_fld.requestFocus();
            return;
        }
        if (soyisim.length() < 2) {
            mk_error_lbl.setText("Soyisim en az 2 karakter olmalıdır!");
            mksoyisim_fld.requestFocus();
            return;
        }
        if (soyisim.length() > 50) {
            mk_error_lbl.setText("Soyisim en fazla 50 karakter olabilir!");
            mksoyisim_fld.requestFocus();
            return;
        }
        if (!isSadeceHarf(soyisim)) {
            mk_error_lbl.setText("Soyisim sadece harf içermelidir!");
            mksoyisim_fld.requestFocus();
            return;
        }
        
        // Şifre validasyonu
        String sifre = mksifre_fld.getText();
        if (sifre.isEmpty()) {
            mk_error_lbl.setText("Şifre alanı boş olamaz!");
            mksifre_fld.requestFocus();
            return;
        }
        if (sifre.length() < MIN_PASSWORD_LENGTH) {
            mk_error_lbl.setText("Şifre en az " + MIN_PASSWORD_LENGTH + " karakter olmalıdır!");
            mksifre_fld.requestFocus();
            return;
        }
        if (sifre.length() > MAX_PASSWORD_LENGTH) {
            mk_error_lbl.setText("Şifre en fazla " + MAX_PASSWORD_LENGTH + " karakter olabilir!");
            mksifre_fld.requestFocus();
            return;
        }
        if (sifre.contains(" ")) {
            mk_error_lbl.setText("Şifre boşluk içeremez!");
            mksifre_fld.requestFocus();
            return;
        }
        
        // En az bir hesap türü seçilmeli
        if (!vadesiz_box.isSelected() && !vadeli_box.isSelected()) {
            mk_error_lbl.setText("En az bir hesap türü seçmelisiniz!");
            return;
        }
        
        // Vadesiz hesap bakiye validasyonu
        int vadesizBakiye = 0;
        if (vadesiz_box.isSelected()) {
            String vadesizBakiyeStr = vadesizbakiye_fld.getText().trim();
            if (!vadesizBakiyeStr.isEmpty()) {
                try {
                    double bakiyeDouble = Double.parseDouble(vadesizBakiyeStr);
                    if (bakiyeDouble < 0) {
                        mk_error_lbl.setText("Vadesiz hesap bakiyesi negatif olamaz!");
                        vadesizbakiye_fld.requestFocus();
                        return;
                    }
                    if (bakiyeDouble > Integer.MAX_VALUE) {
                        mk_error_lbl.setText("Vadesiz hesap bakiyesi çok büyük!");
                        vadesizbakiye_fld.requestFocus();
                        return;
                    }
                    vadesizBakiye = (int) bakiyeDouble;
                } catch (NumberFormatException e) {
                    mk_error_lbl.setText("Vadesiz hesap bakiyesi geçerli bir sayı olmalıdır!");
                    vadesizbakiye_fld.requestFocus();
                    return;
                }
            }
            // Boşsa 0 olarak kalır
        }
        
        // Vadeli hesap bakiye validasyonu
        int vadeliBakiye = 0;
        if (vadeli_box.isSelected()) {
            String vadeliBakiyeStr = vadelibakiye_fld.getText().trim();
            if (!vadeliBakiyeStr.isEmpty()) {
                try {
                    double bakiyeDouble = Double.parseDouble(vadeliBakiyeStr);
                    if (bakiyeDouble < 0) {
                        mk_error_lbl.setText("Vadeli hesap bakiyesi negatif olamaz!");
                        vadelibakiye_fld.requestFocus();
                        return;
                    }
                    if (bakiyeDouble > Integer.MAX_VALUE) {
                        mk_error_lbl.setText("Vadeli hesap bakiyesi çok büyük!");
                        vadelibakiye_fld.requestFocus();
                        return;
                    }
                    vadeliBakiye = (int) bakiyeDouble;
                } catch (NumberFormatException e) {
                    mk_error_lbl.setText("Vadeli hesap bakiyesi geçerli bir sayı olmalıdır!");
                    vadelibakiye_fld.requestFocus();
                    return;
                }
            }
        }
        
        // TC Kimlik validasyonu
        String tcKimlik = mktc_fld.getText().trim();
        if (tcKimlik.isEmpty()) {
            mk_error_lbl.setText("TC Kimlik No alanı boş olamaz!");
            mktc_fld.requestFocus();
            return;
        }
        if (tcKimlik.length() != 11) {
            mk_error_lbl.setText("TC Kimlik No 11 haneli olmalıdır!");
            mktc_fld.requestFocus();
            return;
        }
        if (!isSadeceRakam(tcKimlik)) {
            mk_error_lbl.setText("TC Kimlik No sadece rakam içermelidir!");
            mktc_fld.requestFocus();
            return;
        }
        
        // Telefon validasyonu
        String telNoStr = mktel_fld.getText().trim();
        int telNo = 0;
        if (!telNoStr.isEmpty()) {
            if (!isSadeceRakam(telNoStr)) {
                mk_error_lbl.setText("Telefon numarası sadece rakam içermelidir!");
                mktel_fld.requestFocus();
                return;
            }
            if (telNoStr.length() != 10) {
                mk_error_lbl.setText("Telefon numarası 10 haneli olmalıdır!");
                mktel_fld.requestFocus();
                return;
            }
            try {
                telNo = Integer.parseInt(telNoStr);
            } catch (NumberFormatException e) {
                mk_error_lbl.setText("Telefon numarası geçerli bir sayı olmalıdır!");
                mktel_fld.requestFocus();
                return;
            }
        }
        
        // Adres
        String adres = mkadres_fld.getText().trim();
        
        // Müşteri oluştur
        String adi = isim;
        String soyad = soyisim;
        
        Musteri yeniMusteri = new Musteri(adi, soyad, tcKimlik, adres, telNo, sifre);
        
        // Eğer önceden oluşturulmuş ID varsa onu kullan
        if (geciciMusteriId != -1 && geciciMusteriId != yeniMusteri.getMusteriId()) {
            // Farklı ID oluştuysa, önceki ID'yi kullanmaya çalış
            // Ama ID kontrolü yapıldığı için genelde aynı ID oluşur
        }
        
        // Müşteri numarasını göster
        int musteriId = yeniMusteri.getMusteriId();
        mkmusterino_lbl.setText(String.format("%06d", musteriId));
        
        // Müşteriyi önce listeye ekle (hesap açma metodları için gerekli)
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        musteriler.add(yeniMusteri);
        
        // Seçilen hesap türlerine göre hesap aç
        if (vadesiz_box.isSelected()) {
            yeniMusteri.mHesapAcVadesiz();
            // Açılan vadesiz hesabı bul ve bakiyesini ayarla
            ArrayList<main.Hesap> hesaplar = yeniMusteri.getHesaplar();
            for(int i = 0; i < hesaplar.size(); i++) {
                main.Hesap h = hesaplar.get(i);
                if(h.getHesapTuru() != null && "vadesiz".equals(h.getHesapTuru().getHesapTuru())) {
                    h.setBakiye(vadesizBakiye);
                    break;
                }
            }
        }
        
        if (vadeli_box.isSelected()) {
            yeniMusteri.mHesapAcVadeli();
            // Açılan vadeli hesabı bul ve bakiyesini ayarla
            ArrayList<main.Hesap> hesaplar = yeniMusteri.getHesaplar();
            for(int i = 0; i < hesaplar.size(); i++) {
                main.Hesap h = hesaplar.get(i);
                if(h.getHesapTuru() != null && "vadeli".equals(h.getHesapTuru().getHesapTuru())) {
                    h.setBakiye(vadeliBakiye);
                    break;
                }
            }
        }
        
        // Verileri kaydet
        dosyaIslemleri.tumMusterileriKaydet();
        
        // Müşteriler listesini güncelle
        main.Models.Model.getInstance().getView().refreshMusterilerView();
        
        // Başarı mesajı
        mk_error_lbl.setText("Müşteri başarıyla kaydedildi!");
        mk_error_lbl.setStyle("-fx-text-fill: green;");
        
        // Formu temizle
        formuTemizle();
    }
    
    private void formuTemizle() {
        mkisim_fld.clear();
        mksoyisim_fld.clear();
        mksifre_fld.clear();
        mktc_fld.clear();
        mktel_fld.clear();
        mkadres_fld.clear();
        vadesiz_box.setSelected(false);
        vadesizbakiye_fld.clear();
        vadesizbakiye_fld.setDisable(true);
        vadeli_box.setSelected(false);
        vadelibakiye_fld.clear();
        vadelibakiye_fld.setDisable(true);
        mkmusterino_lbl.setText("");
        // Müşteri numarasını temizleme, bir sonraki kayıtta güncellenecek
    }
    
    // Sadece harf kontrolü (Türkçe karakterler dahil)
    private boolean isSadeceHarf(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || 
                  c == 'ç' || c == 'ğ' || c == 'ı' || c == 'ö' || c == 'ş' || c == 'ü' ||
                  c == 'Ç' || c == 'Ğ' || c == 'İ' || c == 'Ö' || c == 'Ş' || c == 'Ü' ||
                  c == ' ')) {
                return false;
            }
        }
        return true;
    }
    
    // Sadece rakam kontrolü
    private boolean isSadeceRakam(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
