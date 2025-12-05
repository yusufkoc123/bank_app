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

    public Islemler(String sender, String receiver, String miktar, LocalDate date, String message) {
        this.sender = new SimpleStringProperty(this,"Gönderen", sender);
        this.receiver=new SimpleStringProperty(this,"Alıcı", receiver);
        this.miktar=new SimpleStringProperty(this,"Miktar", miktar);
        this.date=new SimpleObjectProperty<>(this,"Tarih",date);
        this.message=new SimpleStringProperty(this,"Mesaj",message);
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
}
