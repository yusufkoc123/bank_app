package main.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import main.Controllers.Musteri.islemler_cellController;
import main.Models.Islemler;

public class islemCellview extends ListCell<Islemler> {
    @Override
    protected void updateItem(Islemler islemler, boolean empty) {
        super.updateItem(islemler, empty);
        if (empty || islemler == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/islemler_cell.fxml"));
                islemler_cellController controller = new islemler_cellController(islemler);
                loader.setController(controller);
                setText(null);
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
                setText(null);
                setGraphic(null);
            }
        }
    }
}
