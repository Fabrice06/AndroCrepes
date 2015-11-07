package crepes.fr.androcrepes.network;

import android.os.AsyncTask;
import android.util.Log;

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


public class Client {

    private static final String TAG = Client.class.getSimpleName();

    public interface ClientCallBack {
        void singleFromClient(String pString); // envoi un callback quand une information est reçue du serveur
        void listeFromClient(List<String> pListData); // envoi un callback quand des données sont reçues du serveur suite à une requête LISTE
        void quantiteFromClient(List<String> pListData); // envoi un callback quand des données sont reçues du serveur suite à une requête QUANTITE

        void connectedFromClient(); // envoi un callback quand le client est connecté
        void errorFromClient(String pError); // envoi un callback pour transmettre un message en cas d'erreur
        void disconnectedFromClient(); // envoi un callback quand le client est vraiment déconnecté
    } // interface

    private static ClientCallBack mCallBack;

    private static String mIp = "";
    private static int mPort = 0;

    private static List<String> mDatas = new ArrayList<String>();

    private static Socket mSocket;
    private static Connection mConnection;
    private static ReadMessages mReadMessages;

    private static PrintWriter mWriter = new PrintWriter(System.out, true);
    private static BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));


    // instance singleton
    protected static Client mInstance;

    private Client() {
    } // constructeur privé


    public static Client getInstance(final ClientCallBack pCallback, final String pIp, final int pPort) {

        if (null == mInstance) {
            mInstance = new Client();

//            mInstance.mCallBack = pCallback;
            mInstance.mIp = pIp;
            mInstance.mPort = pPort;
            mDatas.clear();

//            mConnection = new Connection();
//            mConnection.execute();
        }
        mInstance.mCallBack = pCallback;
        
        return mInstance;
    } // Plats


    public void connect() {
        if (!isRunning()) {
            mConnection = new Connection();
            mConnection.execute();
        } // if
    } // void


    public Boolean send(final EnumSendWord pEnumSendWord, final String pMessage) {
        return write(pEnumSendWord, pMessage);
    } // void

    public void test() {
//write(EnumSendWord.COMMANDE, "10 crepe au jambon");
//write(EnumSendWord.LISTE, "");
//write(EnumSendWord.QUANTITE, "");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.LISTE, "");
//write(EnumSendWord.QUANTITE, "");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au fromage");
//write(EnumSendWord.AJOUT, "17 crepe au jambon");
//write(EnumSendWord.AJOUT, "2 crepe au fromage");
//write(EnumSendWord.AJOUT, "7 crepe au sucre");
//write(EnumSendWord.LISTE, "");
//write(EnumSendWord.QUANTITE, "");
//write(EnumSendWord.COMMANDE, "crepe au jambon");
//write(EnumSendWord.COMMANDE, "crepe au fromage");
//write(EnumSendWord.COMMANDE, "crepe au fromage");
//write(EnumSendWord.COMMANDE, "crepe au fromage");
//write(EnumSendWord.LISTE, "");
//write(EnumSendWord.QUANTITE, "");
    } // void


    public Boolean isRunning() {
        boolean nReturn = false; // valeur de retour par défaut

        if (null != mReadMessages) {
            nReturn = (mReadMessages.getStatus() == AsyncTask.Status.RUNNING);
        } // if
        return nReturn;
    } // boolean


    private Boolean write(final EnumSendWord pEnumSendWord, final String pMessage) {

        Boolean nReturn = false; // valeur de retour par défaut

        mDatas.clear();

//        if (null != mReadMessages) {
            if (isRunning()) {
                String nMessage = pEnumSendWord.getValue();
                if (!pMessage.isEmpty()) {
                    nMessage = nMessage + " " + pMessage;
                }
                mWriter.println(nMessage);
                nReturn = true;
            } // if
//        } // if
    return nReturn;
    } // Boolean


    public void disconnect() {

        if(write(EnumSendWord.LOGOUT, "")) {
            mReadMessages.cancel(true);
        } // if

        if (null != mSocket) {
            mWriter.close();
            try {
                mReader.close();
                mSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            } // catch
        } // if

        mCallBack.disconnectedFromClient();
    } // void

    private static class Connection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // ceinture et bretelle: ce cas peut-il arriver ??
            //logout();
            Log.d(TAG, "Connection.onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... pVoid) {

            boolean nReturn = false; // valeur de retour par défaut

            try {
                mSocket = new Socket(mIp, mPort);
                Log.d(TAG, "doInBackground connected");

                mWriter = new PrintWriter(mSocket.getOutputStream(), true);
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                nReturn= true;

                } catch (IOException e) {
                    Log.d(TAG, "doInBackground IOException");
                    e.printStackTrace();
                }

            return nReturn;
            } // boolean

        @Override
        protected void onPostExecute(Boolean pBoolean) {

            String nLog= pBoolean ? "Connected to server\n" : "Could not connect to server\n";
            Log.d(TAG, "onPostExecute " + nLog);

            if (pBoolean) {
                mReadMessages = new ReadMessages();
                mReadMessages.execute();

                mCallBack.connectedFromClient();

            } else {
                mCallBack.errorFromClient("not connected");
            } // else
        } // void
    } // class


    private static class ReadMessages extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            while (!isCancelled()) {
                try {
                    String nData = mReader.readLine();

                    if (!nData.isEmpty()) {
                        publishProgress(nData.trim());
                    } // if

                } catch (IOException e) {
                    Log.i("Client ReadMessages", "doInBackground IOException");
                    e.printStackTrace();

                    break;
                } // catch
            } // while

            // remplace ReadMessages.onPostExecute
            //mCallBack.doPostExecute();

        return null;
        }

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
                    }

                    mDatas.clear();

                } else {
                    //fixme: trouver et checker la taille max
                    mDatas.add(messages[0]);
                } // else
            } // else
        } // void
    } // class
} // class