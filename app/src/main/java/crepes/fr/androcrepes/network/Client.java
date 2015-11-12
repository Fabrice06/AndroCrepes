package crepes.fr.androcrepes.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import crepes.fr.androcrepes.commons.EnumReceiveWord;
import crepes.fr.androcrepes.commons.EnumSendWord;
import crepes.fr.androcrepes.commons.Tools;

/**
 * <b>Cette classe centralise la communication avec le serveur distant.</b>
 * <p>
 *     Le pattern design singleton est appliqué à cette classe.
 * </p>
 */
public class Client {

    private static final String TAG = Client.class.getSimpleName();

    /**
     * <b>ClientCallBack est une interface.</b>
     * <p>
     *     Cette interface comporte 6 callback afin de communiquer avec les activités:
     *     <ul>
     *         <li>singleFromClient(String pString): une information est reçue du serveur</li>
     *         <li>listeFromClient(List<String> pListData): des données sont reçues du serveur suite à une requête LISTE</li>
     *         <li>quantiteFromClient(List<String> pListData): des données sont reçues du serveur suite à une requête QUANTITE</li>
     *
     *         <li>connectedFromClient(): la connection est établie</li>
     *         <li>errorFromClient(String pError): une erreur est transmise</li>
     *         <li>disconnectedFromClient(): la déconnection est effective</li>
     *     </ul>
     * </p>
     */
    public interface ClientCallBack {
        /**
         * Envoi un callback quand une information est reçue du serveur.
         *
         * @param pString
         *      Le message reçu de type String.
         */
        void singleFromClient(String pString);

        /**
         * Envoi un callback quand des données sont reçues du serveur suite à une requête LISTE.
         *
         * @param pListData
         *      Les données reçues sous la forme d'une collection de String.
         */
        void listeFromClient(List<String> pListData);

        /**
         * Envoi un callback quand des données sont reçues du serveur suite à une requête QUANTITE.
         *
         * <p><b>Attention:</b><br>
         *     La collection comporte successivement la séquence suivante: le nom du plat puis la quantité disponible.
         * </p>
         *
         * @param pListData
         *      Les données reçues sous la forme d'une collection de String.
         */
        void quantiteFromClient(List<String> pListData);

        /**
         * Envoi un callback quand la connection est établie.
         */
        void connectedFromClient();

        /**
         * Envoi un callback pour transmettre un message en cas d'erreur.
         *
         * @param pError
         *      Un message d'erreur de type String
         */
        void errorFromClient(String pError);

        /**
         * Envoi un callback quand la connection n'est plus effective
         */
        void disconnectedFromClient();
    } // interface

    private static ClientCallBack mCallBack;

    /**
     * Adresse IP du serveur distant.
     */
    private static String mIp = "";

    /**
     * Port ud serveur distant.
     */
    private static int mPort = 0;

    /**
     * Collection des informations reçues par le serveur distant.
     */
    private static List<String> mDatas = new ArrayList<String>();

    private static Socket mSocket;
    private static Connection mConnection;
    private static ReadMessages mReadMessages;

    private static PrintWriter mWriter = new PrintWriter(System.out, true);
    private static BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Instance singleton
     */
    protected static Client mInstance;

    /**
     * Constructeur privé
     *
     * @see getInstance
     */
    private Client() {
    } // constructeur

    /**
     * Retourne une instance unique de la classe Client.
     *
     * @param pCallback
     *      Classe d'implémentation de l'interface
     *
     * @param pIp
     *      Adresse IP du serveur de type String
     *
     * @param pPort
     *      Port d'écoute du serveur de type int
     *
     * @return L'instance unique de type Client.
     */
    public static Client getInstance(final ClientCallBack pCallback, final String pIp, final int pPort) {

        if (null == mInstance) {
            mInstance = new Client();

            mInstance.mIp = pIp;
            mInstance.mPort = pPort;
            mDatas.clear();
        } // if

        mInstance.mCallBack = pCallback;

    return mInstance;
    } // Plats

    /**
     * Réalise la connexion avec le serveur, si elle n'est pas déjà établie et active.
     *
     * @see isRunning
     */
    public void connect() {
        if (!isRunning()) {
            mConnection = new Connection();
            mConnection.execute();
        } // if
    } // void

    /**
     * Détermine si la connexion est établie et active.
     *
     * @return Vrai, si la connexion est établie et active.
     */
    public Boolean isRunning() {
        boolean nReturn = false; // valeur de retour par défaut

        if (null != mReadMessages) {
            nReturn = (mReadMessages.getStatus() == AsyncTask.Status.RUNNING);
        } // if

    return nReturn;
    } // boolean

    /**
     * Réalise la conception et l'envoi d'un message au serveur.
     *
     * @param pEnumSendWord
     *      Le préfixe du message de type EnumSendWord.
     *
     * @param pMessage
     *      Le complément éventuel du message de type string.
     *
     * @return Vrai, si le message a été transmis.
     *
     * @see EnumSendWord
     * @see isRunning
     */
    public Boolean send(final EnumSendWord pEnumSendWord, final String pMessage) {
        Boolean nReturn = false; // valeur de retour par défaut

        // La collection des informations reçues par le serveur est vidée.
        mDatas.clear();

        if (isRunning()) {
            String nMessage = pEnumSendWord.getValue();
            if (!pMessage.isEmpty()) {
                nMessage = nMessage + " " + pMessage;
            }
            mWriter.println(nMessage);
            nReturn = true;
        } // if

    return nReturn;
    } // Boolean

    /**
     * Réalise l'ensemble des opérations nécessaires à l'arrêt de la connexion.
     *
     * @see disconnectedFromClient
     */
    public void disconnect() {

        // transmission du message d'arrêt au serveur
        if(send(EnumSendWord.LOGOUT, "")) {
            mReadMessages.cancel(true);
        } // if

        // fin propre
        if (null != mSocket) {
//            try {
//                mWriter.close();
//                mReader.close();
//                mSocket.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } // catch
            mWriter.close();
            try {
                mReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // if

        // envoi du message de fin de connexion
        mCallBack.disconnectedFromClient();
    } // void

    /**
     * Première étape de la connexion au serveur distant
     *
     * <p><b>Attention:</b><br>
     *     Cette classe est asynchrone: elle hérite de la classe AsyncTask, issue de la librairie Android.
     * </p>
     */
    private static class Connection extends AsyncTask<Void, Void, Boolean> {

        /**
         * Réalise l'implémentation des objets nécessaires à la connexion:
         * <ul>
         *   <li>Socket: pour la liaison</li>
         *   <li>PrintWriter: pour l'envoi des messages</li>
         *   <li>BufferedReader: pour la réception et le lecture des messages</li>
         * </ul>
         *
         * @param pVoid
         *
         * @return Vrai, si la connexion est établie.
         */
        @Override
        protected Boolean doInBackground(Void... pVoid) {
            boolean nReturn = false; // valeur de retour par défaut

            try {
                mSocket = new Socket(mIp, mPort);

                mWriter = new PrintWriter(mSocket.getOutputStream(), true);
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                nReturn= true;

            } catch (IOException e) {
                e.printStackTrace();
            } // catch

        return nReturn;
        } // boolean

        /**
         * Réalise la fin de l'étape asynchrone doInBackground(...).
         * <p><b>Attention:</b><br>
         *     En cas d'échec de l'étape précédente, le lancement de l'étape suivante ne sera pas réalisé.
         * </p>
         * <p>
         *     Cette méthode comporte 2 callback:
         *     <ul>
         *         <li>connectedFromClient(): la connection est établie</li>
         *         <li>errorFromClient(String pError): une erreur est transmise</li>
         *     </ul>
         * </p>
         *
         * @param pBoolean
         *      Vrai, si la connexion a été établie avec succès.
         */
        @Override
        protected void onPostExecute(Boolean pBoolean) {

            if (pBoolean) {
                mReadMessages = new ReadMessages();
                mReadMessages.execute();

                mCallBack.connectedFromClient();

            } else {
                //fixme donner à l'utilisateur les piste pour résoudre son pb
                mCallBack.errorFromClient("Impossible de se connecter au serveur !");
            } // else
        } // void
    } // private class --------------------------------------------------------------

    /**
     * Seconde étape de la connexion au serveur distant
     *
     * <p><b>Attention:</b><br>
     *     Cette classe est asynchrone: elle hérite de la classe AsyncTask, issue de la librairie Android.
     * </p>
     */
    private static class ReadMessages extends AsyncTask<Void, String, Void> {


        /**
         * Réalise l'écoute des messages reçus du serveur distant.
         *
         * @see isCancelled
         *
         * @param pVoid
         *
         * @return null
         */
        @Override
        protected Void doInBackground(Void... v) {
            while (!isCancelled()) {
                try {
                    String nData = mReader.readLine();

                    if (!nData.isEmpty()) {
                        publishProgress(nData.trim());
                    } // if

                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                } // catch
            } // while

            // remplace ReadMessages.onPostExecute
            //mCallBack.doPostExecute();

        return null;
        }

        /**
         * Réalise la lecture et l'analyse des messages reçus du serveur distant.
         * <p>
         *     Deux familles de données sont définies:
         *     <ul>
         *         <li>Read: lecture de données initiée avec une requête LISTE ou QUANTITE</li>
         *         <li>Create et Update: réponse aux manipulations de données initiée avec une requête AJOUT ou COMMANDE</li>
         *     </ul>
         * </p>
         *
         * @see EnumSendWord
         * @see EnumReceiveWord
         *
         * @param messages
         *      Un message de type String
         */
        @Override
        protected void onProgressUpdate(String... messages) {

            if (0 == mDatas.size()) {

                if ((messages[0].indexOf(":")) >= 0) { // c'est une action GET (LISTE ou QUANTITE)
                    //fixme: trouver et checker la taille max
                    mDatas.add("DEBUTLISTE");

                } else { // c'est une action PUT, POST, DELETE
                    //fixme: trouver et checker la taille max

                    mCallBack.singleFromClient(messages[0]);
                    mDatas.clear();
                } // else

            } else { // c'est une action GET (LISTE ou QUANTITE)

                if (EnumReceiveWord.FINLISTE.getValue().equals(messages[0])) {
                    // si le dernier est un integer, c'est une quantité
                    if (Tools.isInteger(mDatas.get(mDatas.size() - 1))) {
                        mDatas.set(0, EnumSendWord.QUANTITE.getValue());
                        mCallBack.quantiteFromClient(mDatas);

                    } else {
                        mDatas.set(0, EnumSendWord.LISTE.getValue());
                        mCallBack.listeFromClient(mDatas);
                    } // else

                    mDatas.clear();

                } else {
                    //fixme: trouver et checker la taille max
                    mDatas.add(messages[0]);
                } // else
            } // else
        } // void
    } // private class --------------------------------------------------------------
} // class