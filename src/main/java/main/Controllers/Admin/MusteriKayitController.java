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

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 50;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        register_btn.setOnAction(e -> musteriKaydet());
        mk_error_lbl.setText(null);
        mkmusterino_lbl.setText(null);
        
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
        
        vadesizbakiye_fld.setDisable(true);
        vadelibakiye_fld.setDisable(true);
        
        setupNameField(mkisim_fld);
        setupNameField(mksoyisim_fld);
        
        setupNumericField(vadesizbakiye_fld);
        setupNumericField(vadelibakiye_fld);
        
        setupNumericOnlyField(mktc_fld);
        setupNumericOnlyFieldWithMaxLength(mktel_fld, 10);
        
        mkisim_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
        mksoyisim_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
        mktc_fld.textProperty().addListener((obs, oldVal, newVal) -> musteriIdOlustur());
    }
    
    private int geciciMusteriId = -1;
    
    private void musteriIdOlustur() {
        String isim = mkisim_fld.getText().trim();
        String soyisim = mksoyisim_fld.getText().trim();
        String tc = mktc_fld.getText().trim();
        
        if (!isim.isEmpty() && !soyisim.isEmpty() && !tc.isEmpty() && tc.length() == 11) {
            try {
                String adres = mkadres_fld.getText().trim();
                String telNo = mktel_fld.getText().trim();
                String sifre = mksifre_fld.getText().isEmpty() ? "temp123" : mksifre_fld.getText();
                
                Musteri geciciMusteri = new Musteri(isim, soyisim, tc, adres, telNo, sifre);
                geciciMusteriId = geciciMusteri.getMusteriId();
                
                mkmusterino_lbl.setText(String.format("%06d", geciciMusteriId));
            } catch (Exception e) {
                mkmusterino_lbl.setText("");
            }
        } else {
            mkmusterino_lbl.setText("");
            geciciMusteriId = -1;
        }
    }
    
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
        mk_error_lbl.setStyle("-fx-text-fill: red;");
        
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
        
        String sifreHataMesaji = sifreGuvenlikKontrolu(sifre);
        if (sifreHataMesaji != null) {
            mk_error_lbl.setText(sifreHataMesaji);
            mksifre_fld.requestFocus();
            return;
        }
        
        if (!vadesiz_box.isSelected() && !vadeli_box.isSelected()) {
            mk_error_lbl.setText("En az bir hesap türü seçmelisiniz!");
            return;
        }
        
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
        }
        
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
        if (!tcKimlikDogrula(tcKimlik)) {
            mk_error_lbl.setText("Geçersiz TC Kimlik No!.");
            mktc_fld.requestFocus();
            return;
        }
        
        String telNo = mktel_fld.getText().trim();
        if (!telNo.isEmpty()) {
            if (!isSadeceRakam(telNo)) {
                mk_error_lbl.setText("Telefon numarası sadece rakam içermelidir!");
                mktel_fld.requestFocus();
                return;
            }
            if (telNo.length() != 10) {
                mk_error_lbl.setText("Telefon numarası 10 haneli olmalıdır!");
                mktel_fld.requestFocus();
                return;
            }
        }
        
        String adres = mkadres_fld.getText().trim();
        
        String adi = isim;
        String soyad = soyisim;
        
        Musteri yeniMusteri = new Musteri(adi, soyad, tcKimlik, adres, telNo, sifre);
        
        int musteriId = yeniMusteri.getMusteriId();
        mkmusterino_lbl.setText(String.format("%06d", musteriId));
        
        ArrayList<Musteri> musteriler = Veznedar.getMusteriler();
        musteriler.add(yeniMusteri);
        
        if (vadesiz_box.isSelected()) {
            yeniMusteri.mHesapAcVadesiz();
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
            ArrayList<main.Hesap> hesaplar = yeniMusteri.getHesaplar();
            for(int i = 0; i < hesaplar.size(); i++) {
                main.Hesap h = hesaplar.get(i);
                if(h.getHesapTuru() != null && "vadeli".equals(h.getHesapTuru().getHesapTuru())) {
                    h.setBakiye(vadeliBakiye);
                    break;
                }
            }
        }
        
        main.Models.Model.getInstance().getView().refreshMusterilerView();
        
        mk_error_lbl.setText("Müşteri başarıyla kaydedildi!");
        mk_error_lbl.setStyle("-fx-text-fill: green;");
        
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
    }
    
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
    
    private boolean isSadeceRakam(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
    private boolean tcKimlikDogrula(String tcKimlik) {
        if (tcKimlik == null || tcKimlik.length() != 11) {
            return false;
        }
        
        int toplam = 0;
        for (int i = 0; i < 10; i++) {
            toplam += Character.getNumericValue(tcKimlik.charAt(i));
        }
        
        int kalan = toplam % 10;
        
        int sonBasamak = Character.getNumericValue(tcKimlik.charAt(10));
        
        return kalan == sonBasamak;
    }
    
    private String sifreGuvenlikKontrolu(String sifre) {
        if (sifre.length() < MIN_PASSWORD_LENGTH) {
            return "Şifre en az " + MIN_PASSWORD_LENGTH + " karakter olmalıdır!";
        }
        
        boolean hasKucukHarf = false;
        boolean hasBuyukHarf = false;
        boolean hasRakam = false;
        boolean hasNoktalama = false;
        
        String noktalamaIsaretleri = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        for (int i = 0; i < sifre.length(); i++) {
            char c = sifre.charAt(i);
            if (c >= 'a' && c <= 'z') {
                hasKucukHarf = true;
            } else if (c >= 'A' && c <= 'Z') {
                hasBuyukHarf = true;
            } else if (c >= '0' && c <= '9') {
                hasRakam = true;
            } else if (noktalamaIsaretleri.indexOf(c) >= 0) {
                hasNoktalama = true;
            }
        }
        
        if (!hasKucukHarf) {
            return "Şifre en az 1 küçük harf içermelidir!";
        }
        
        if (!hasBuyukHarf) {
            return "Şifre en az 1 büyük harf içermelidir!";
        }
        
        if (!hasRakam) {
            return "Şifre en az 1 rakam içermelidir!";
        }
        
        if (!hasNoktalama) {
            return "Şifre en az 1 noktalama işareti içermelidir!";
        }
        
        return null;
    }
}
