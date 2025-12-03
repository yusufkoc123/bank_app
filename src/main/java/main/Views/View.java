package main.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Controllers.Admin.AdminController;
import main.Controllers.Musteri.MusteriController;

public class View {
    private HesapTuru giristuru;
    //musteri
    private final ObjectProperty<MusteriMenuOptions> musterisecilenmenu;
    private AnchorPane anasayfaView;
    private AnchorPane islemlerView;
    private AnchorPane hesaplarimView;
    //admin
    private final ObjectProperty<AdminMenuOptions> adminsecilenmenu;
    private AnchorPane MusterikayitView;
    private AnchorPane MusterilerView;
    private AnchorPane ParayatirView;

    //musteri görunum
    public View(){
        this.giristuru=HesapTuru.MUSTERI;
        this.musterisecilenmenu = new SimpleObjectProperty<>();
        this.adminsecilenmenu = new SimpleObjectProperty<>();
    }

    public HesapTuru getGiristuru() {
        return giristuru;
    }

    public void setGiristuru(HesapTuru giristuru) {
        this.giristuru = giristuru;
    }

    public ObjectProperty<MusteriMenuOptions> getmusterisecilenmenu(){

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

    public void musteriWindow(){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/Musteri.fxml"));
            MusteriController controller = new MusteriController();
            loader.setController(controller);
            createStage(loader);

    }

    //admin görünüm

    public ObjectProperty<AdminMenuOptions> getAdminsecilenmenu(){
        return adminsecilenmenu;
    }

    public AnchorPane getMusterikayitView() {
        if(MusterikayitView == null){
            try {
                MusterikayitView= new FXMLLoader(getClass().getResource("/Fxml/Admin/Musterikayit.fxml")).load();

            }catch (Exception e){
                e.printStackTrace();
            }
        }return MusterikayitView;
    }

    public AnchorPane getMusterilerView() {
        if(MusterilerView == null){
            try {
                MusterilerView=new FXMLLoader(getClass().getResource("/Fxml/Admin/Musteriler.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  MusterilerView;
    }

    public AnchorPane getParayatirView() {
        if(ParayatirView == null){
            try {
                MusterilerView=new FXMLLoader(getClass().getResource("Fxml/Admin/Parayatir.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ParayatirView;
    }

    public void AdminWindow(){
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
