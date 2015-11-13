package crepes.fr.androcrepes.controller;

import android.app.Application;

import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plats;


public class Controller extends Application {

    //fixme: à dégager via le menu settings
//    public static final String SERVER_IP = "10.0.3.2";
    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 7777;
    //fixme: à dégager via le menu settings

    //fixme a dégager via string xml
    public static final String LOGOUT = "logout";
    public static final String LOGON = "logon";
    public static final String WAIT = "Thinking...";
    // a dégager via string xml

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
