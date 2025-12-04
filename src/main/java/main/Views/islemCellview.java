package main.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import main.Controllers.Musteri.islemler_cellController;
import main.Models.Islemler;

public class islemCellview extends ListCell<Islemler> {
    @Override
    protected void updateItem(Islemler islemler, boolean Empty) {
        super.updateItem(islemler, Empty);
        if(Empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/islemler_cell.fxml"));
            islemler_cellController controller = new islemler_cellController(islemler);
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
