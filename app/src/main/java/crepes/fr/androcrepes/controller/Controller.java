package crepes.fr.androcrepes.controller;

import android.app.Application;

import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plats;


public class Controller extends Application {

    private Plats mPlats = new Plats();

    private Commandes mCommandes = new Commandes();

//    private ModelCart myCart = new ModelCart();

//    public ModelProducts getProducts(int pPosition) {
//        return myProducts.get(pPosition);
//    }

//    public void setProducts(ModelProducts Products) {
//        myProducts.add(Products);
//    }

//    public void setPlats(ModelProducts Products) {
//        myProducts.add(Products);
//    }

    public Plats getPlats() {
        return mPlats;
    }

    public Commandes getCommandes() {
        return mCommandes;
    }

//    public int getProductsArraylistSize() {
//        return myProducts.size();
//    }
} // class
