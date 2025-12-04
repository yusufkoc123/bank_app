package main.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.Models.Musteri;
import main.Veznedar;
import main.Views.musteriler_cellView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MusterilerController implements Initializable {
    public ListView<Musteri> musteriler_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        musteriler_listview.setCellFactory(listView -> new musteriler_cellView());
        yukleMusteriler();
    }

    private void yukleMusteriler() {
        ObservableList<Musteri> musteriler = FXCollections.observableArrayList(
            Veznedar.getMusteriler().stream()
                .map(Musteri::fromDomainModel)
                .collect(Collectors.toList())
        );
        musteriler_listview.setItems(musteriler);
    }
}
