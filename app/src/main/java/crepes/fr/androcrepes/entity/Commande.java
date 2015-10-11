package crepes.fr.androcrepes.entity;

import java.util.ArrayList;

// commande n° 1: liste des Demande (plats + quantités) de la commande n°1
public class Commande {

    private static int numero= 0;

    private ArrayList<Demande> plats = new ArrayList<Demande>();

    public Commande() {
        numero++;
    } // constructeur par défaut


} // class
