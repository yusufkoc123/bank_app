package main.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Islemler {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final StringProperty miktar;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;
    private final StringProperty islemTuru;

    public Islemler(String sender, String receiver, String miktar, LocalDate date, String message, String islemTuru) {
        this.sender = new SimpleStringProperty(this,"Gönderen", sender);
        this.receiver=new SimpleStringProperty(this,"Alıcı", receiver);
        this.miktar=new SimpleStringProperty(this,"Miktar", miktar);
        this.date=new SimpleObjectProperty<>(this,"Tarih",date);
        this.message=new SimpleStringProperty(this,"Mesaj",message);
        this.islemTuru=new SimpleStringProperty(this,"İşlem Türü", islemTuru != null ? islemTuru : "");
    }
    public StringProperty senderProperty() {
        return this.sender;
    }
    public StringProperty receiverProperty() {
        return this.receiver;
    }
    public StringProperty miktarProperty() {
        return this.miktar;
    }
    public ObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }
    public StringProperty messageProperty() {
        return this.message;
    }
    
    public StringProperty islemTuruProperty() {
        return this.islemTuru;
    }

    public String getSender() {
        return sender.get();
    }

    public String getReceiver() {
        return receiver.get();
    }

    public String getMiktar() {
        return miktar.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getMessage() {
        return message.get();
    }

    public String getIslemTuru() {
        return islemTuru.get();
    }

    public static Islemler fromDomainModel(main.Islem domainIslem) {
        if (domainIslem == null) {
            return null;
        }
        return new Islemler(
            domainIslem.getGondericiAdi(),
            domainIslem.getAliciAdi(),
            String.valueOf(domainIslem.getMiktar()),
            domainIslem.getTarih(),
            domainIslem.getMesaj(),
            domainIslem.getIslemTuru()
        );
    }
}
