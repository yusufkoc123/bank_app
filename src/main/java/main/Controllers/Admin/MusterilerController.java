package main.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.Models.Musteri;
import main.Veznedar;
import main.Views.musteriler_cellView;

import java.net.URL;
import java.util.ResourceBundle;
import main.dataStructures.ArrayList;

public class MusterilerController implements Initializable {
    public ListView<Musteri> musteriler_listview;
    public TextField arama_fld;
    
    private ObservableList<Musteri> tumMusteriler;
    private FilteredList<Musteri> filtrelenmisMusteriler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        musteriler_listview.setCellFactory(listView -> new musteriler_cellView());
        yukleMusteriler();
        aramaFiltrelemeBagla();
    }

    private void yukleMusteriler() {
        tumMusteriler = FXCollections.observableArrayList();
        ArrayList<main.Musteri> domainMusteriler = Veznedar.getMusteriler();
        for(int i = 0; i < domainMusteriler.size(); i++){
            main.Musteri domainMusteri = domainMusteriler.get(i);
            Musteri uiMusteri = Musteri.fromDomainModel(domainMusteri);
            if(uiMusteri != null){
                tumMusteriler.add(uiMusteri);
            }
        }
        
        filtrelenmisMusteriler = new FilteredList<>(tumMusteriler, p -> true);
        musteriler_listview.setItems(filtrelenmisMusteriler);
    }
    
    private void aramaFiltrelemeBagla() {
        if (arama_fld != null) {
            arama_fld.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrelenmisMusteriler.setPredicate(musteri -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    
                    String aramaMetni = newValue.toLowerCase();
                    String isim = musteri.getIsim() != null ? musteri.getIsim().toLowerCase() : "";
                    String soyisim = musteri.getSoyisim() != null ? musteri.getSoyisim().toLowerCase() : "";
                    String tc = musteri.getTcno() != null ? musteri.getTcno().toLowerCase() : "";
                    String musteriId = musteri.getMusterID() != null ? musteri.getMusterID().toLowerCase() : "";
                    String telno = musteri.getTelno() != null ? musteri.getTelno().toLowerCase() : "";
                    
                    return isim.contains(aramaMetni) || 
                           soyisim.contains(aramaMetni) || 
                           tc.contains(aramaMetni) || 
                           musteriId.contains(aramaMetni) || 
                           telno.contains(aramaMetni) ||
                           (isim + " " + soyisim).contains(aramaMetni);
                });
            });
        }
    }
    
    public void refreshMusteriler() {
        yukleMusteriler();
        aramaFiltrelemeBagla();
    }
}
