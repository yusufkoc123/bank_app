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
import java.util.regex.Pattern;

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
    
    // Validation patterns
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZçğıöşüÇĞIİÖŞÜ\\s]{2,50}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 50;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        register_btn.setOnAction(e -> musteriKaydet());
        mk_error_lbl.setText("");
        mkmusterino_lbl.setText("");
        
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
    }
    
    // İsim/soyisim alanları için sadece harf girişi
    private void setupNameField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("^[a-zA-ZçğıöşüÇĞIİÖŞÜ\\s]*$")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    // Bakiye alanları için sadece sayı girişi
    private void setupNumericField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("^\\d*\\.?\\d*$")) {
                return change;
            }
            return null;
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
        if (isim.length() < 2) {
            mk_error_lbl.setText("İsim en az 2 karakter olmalıdır!");
            mkisim_fld.requestFocus();
            return;
        }
        if (isim.length() > 50) {
            mk_error_lbl.setText("İsim en fazla 50 karakter olabilir!");
            mkisim_fld.requestFocus();
            return;
        }
        if (!NAME_PATTERN.matcher(isim).matches()) {
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
        if (!NAME_PATTERN.matcher(soyisim).matches()) {
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
            // Boşsa 0 olarak kalır
        }
        
        // Müşteri oluştur (eksik alanlar için default değerler)
        String adi = isim;
        String soyad = soyisim;
        String tcKimlik = ""; // Formda yok, boş bırakılıyor
        String adres = ""; // Formda yok, boş bırakılıyor
        int telNo = 0; // Formda yok, default değer
        
        Musteri yeniMusteri = new Musteri(adi, soyad, tcKimlik, adres, telNo, sifre);
        
        // Müşteri numarasını göster
        int musteriId = yeniMusteri.getMusteriId();
        mkmusterino_lbl.setText(String.valueOf(musteriId));
        
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
        vadesiz_box.setSelected(false);
        vadesizbakiye_fld.clear();
        vadesizbakiye_fld.setDisable(true);
        vadeli_box.setSelected(false);
        vadelibakiye_fld.clear();
        vadelibakiye_fld.setDisable(true);
        // Müşteri numarasını temizleme, bir sonraki kayıtta güncellenecek
    }
}
