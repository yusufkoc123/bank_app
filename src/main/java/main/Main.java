package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.View;
import main.dataStructures.ArrayList;

/*
 * Bu sınıf, bankacılık uygulamasının ana giriş noktasıdır.
 * Uygulamanın başlatılması, veri yükleme/kaydetme ve ana pencerelerin gösterilmesi gibi temel işlevleri yönetir.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage)  {
        dosyaIslemleri.tumVerileriYukle();
        
        Model.getInstance().getView().showLoginWindow();

        musterileriYazdir();
    }
    
    @Override
    public void stop() {
        dosyaIslemleri.tumVerileriKaydet();
    }
    

    public static void main(String[] args) {
        launch(args);
    }

    private static void musterileriYazdir() {
    }
}