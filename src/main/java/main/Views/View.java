package main.Views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Controllers.Admin.AdminController;
import main.Controllers.Musteri.MusteriController;

public class View {
    //musteri
    private final StringProperty musterisecilenmenu;
    private AnchorPane anasayfaView;
    private AnchorPane islemlerView;
    private AnchorPane hesaplarimView;
    //admin
    private AnchorPane MusterikayitView;

    //musteri görunum
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

    public void showClientWindow(){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/Musteri.fxml"));
            MusteriController controller = new MusteriController();
            loader.setController(controller);
            createStage(loader);

    }

    //admin görünüm




    public void showAdminWindow(){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
            AdminController controller = new AdminController();
            loader.setController(controller);
            createStage(loader);
    }















    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
        createStage(loader);
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
