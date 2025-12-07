package main.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Controllers.Admin.AdminController;
import main.Controllers.Musteri.MusteriController;
import main.Models.Model;

public class View {
    private HesapTuru giristuru;
    private final ObjectProperty<MusteriMenuOptions> musterisecilenmenu;
    private AnchorPane islemlerView;
    private AnchorPane hesaplarimView;
    private final ObjectProperty<AdminMenuOptions> adminsecilenmenu;
    private AnchorPane MusterikayitView;
    private AnchorPane MusterilerView;
    private AnchorPane ParayatirView;
    private main.Controllers.Admin.MusterilerController musterilerController;

    public View(){
        this.giristuru=HesapTuru.Musteri;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/anasayfa.fxml"));
            return loader.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
        try {
            return new FXMLLoader(getClass().getResource("/Fxml/Musteri/hesaplarim.fxml")).load();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AnchorPane getProfilView() {
        try {
            return new FXMLLoader(getClass().getResource("/Fxml/Musteri/Profil.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void musteriWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Musteri/Musteri.fxml"));
        MusteriController controller = new MusteriController();
        loader.setController(controller);
        createStage(loader);

    }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Musteriler.fxml"));
                MusterilerView = loader.load();
                musterilerController = loader.getController();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  MusterilerView;
    }
    
    public void refreshMusterilerView() {
        // Eğer controller varsa direkt refresh et (en hızlı yöntem)
        if(musterilerController != null) {
            musterilerController.refreshMusteriler();
        } else {
            // Controller yoksa cache'i null yap ve yeniden yükle
            MusterilerView = null;
            // Eğer şu an müşteriler görünüyorsa, view'ı yeniden yükle
            AdminMenuOptions currentMenu = Model.getInstance().getView().getAdminsecilenmenu().get();
            if(currentMenu == AdminMenuOptions.MUSTERILER) {
                // Menu'yu değiştirip geri değiştirerek view'ı yeniden yükle
                Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.MUSTERI_KAYIT);
                Model.getInstance().getView().getAdminsecilenmenu().set(AdminMenuOptions.MUSTERILER);
            }
        }
    }

    public AnchorPane getParayatirView() {
        if(ParayatirView == null){
            try {
                ParayatirView=new FXMLLoader(getClass().getResource("/Fxml/Admin/Parayatir.fxml")).load();
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
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
            stage.setResizable(false);
            stage.setTitle("İnü Bank");
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  closeStage(Stage stage){
        stage.close();
    }

}
