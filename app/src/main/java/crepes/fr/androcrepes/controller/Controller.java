package crepes.fr.androcrepes.controller;

import android.app.Application;

import crepes.fr.androcrepes.model.Commande;
import crepes.fr.androcrepes.model.Commandes;
import crepes.fr.androcrepes.model.Plats;


public class Controller extends Application {

    //private static final String SERVER_IP = "10.0.3.2";
    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 7777;

    private String mServerIp = "";
    private int mServerPort = 0;

    private Commandes mCommandes = new Commandes();
    private Plats mPlats = new Plats();

    private int mCurrentCommandeId = 0;
    private Commande mCurrentCommande = null;

    public Commandes getCommandes() {
        return mCommandes;
    } // Commandes

    public Plats getPlats() {
        return mPlats;
    } // Plats


    public String getServerIp() {
        return mServerIp;
//        return (mServerIp.isEmpty()) ? SERVER_IP : mServerIp;
    } // String

    public void setServerIp(final String pIp) {
        mServerIp = pIp;
    } // void

    public int getServerPort() {
        return mServerPort;
        //return (0 == mServerPort) ? SERVER_PORT : mServerPort;
    } // int

    public void setServerPort(final int pPort) {
        mServerPort = pPort;
    } // void


    public void setCurrentCommande(final int pCurrentId) {

        if (mCurrentCommandeId == pCurrentId) {

        } else {
            mCurrentCommande = mCommandes.getCommande(pCurrentId);
            mCurrentCommandeId = pCurrentId;
        }

    } // void

    public Commande getCurrentCommande() {
        return mCurrentCommande;
    } // Commande

} // class
