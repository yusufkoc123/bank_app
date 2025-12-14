package main.Models;

import main.Musteri;
import main.Veznedar;
import main.Views.View;

public class Model {
    private static Model model;
    private final View view;
    private Musteri currentMusteri;
    private Veznedar currentVeznedar;

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
    
    public Veznedar getCurrentVeznedar() {
        return currentVeznedar;
    }
    
    public void setCurrentVeznedar(Veznedar currentVeznedar) {
        this.currentVeznedar = currentVeznedar;
    }
}
