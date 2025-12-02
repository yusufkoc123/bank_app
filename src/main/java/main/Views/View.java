package main.Views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Controllers.Musteri.MusteriController;

public class View {
    private final StringProperty musterisecilenmenu;
    private AnchorPane anasayfaView;
    private AnchorPane islemlerView;
    private AnchorPane hesaplarimView;
    public View(){
        this.musterisecilenmenu = new SimpleStringProperty();

    }
    public StringProperty getmusterisecilenmenu(){
        return musterisecilenmenu;
    }


    public AnchorPane getAnasayfaView() {
        if(anasayfaView == null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/anasayfa.fxml"));
                anasayfaView = loader.load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return anasayfaView;
    }
    public AnchorPane getIslemlerView() {
        if(islemlerView == null){
            try {
                islemlerView=new  FXMLLoader(getClass().getResource("/Fxml/Musteri/islemler.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return islemlerView;
    }

    public AnchorPane getHesaplarimView() {
        if(hesaplarimView == null){
            try {
                hesaplarimView=new  FXMLLoader(getClass().getResource("/Fxml/Musteri/hesaplarim.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return hesaplarimView;
    }

    public void showLoginWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
            createStage(loader);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showClientWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/Musteri.fxml"));

            // Musteri.fxml controller'ı dışarıdan veriliyor
            MusteriController controller = new MusteriController();
            loader.setController(controller);

            createStage(loader);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createStage(FXMLLoader loader){
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("İnü Bank");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  closeStage(Stage stage){
      stage.close();
    }

}
