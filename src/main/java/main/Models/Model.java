package main.Models;

import main.Musteri;
import main.Views.View;

public class Model {
    private static Model model;
    private final View view;

    // Giriş yapan aktif müşteri
    private Musteri currentMusteri;

    public Model() {
        this.view = new View();
    }

    public static synchronized Model getInstance(){
        if(model==null){
            model =new Model();
        }
        return model;
    }

    public View getView() {
        return view;
    }

    public Musteri getCurrentMusteri() {
        return currentMusteri;
    }

    public void setCurrentMusteri(Musteri currentMusteri) {
        this.currentMusteri = currentMusteri;
    }
}
