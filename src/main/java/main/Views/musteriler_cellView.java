package main.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import main.Controllers.Admin.musteriler_CellController;
import main.Controllers.Musteri.islemler_cellController;
import main.Models.Musteri;

public class musteriler_cellView extends ListCell<Musteri> {
    @Override
    protected void updateItem(Musteri musteri, boolean empty) {
        super.updateItem(musteri, empty);
        if (empty){
            setText(null);
            setGraphic(null);
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/musteriler_cell.fxml"));
            musteriler_CellController controller = new musteriler_CellController(musteri);
            loader.setController(controller);
            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
