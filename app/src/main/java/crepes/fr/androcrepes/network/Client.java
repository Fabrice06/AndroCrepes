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

//fixme voir si singleton opportun et possible
public class Client {

    public interface ClientCallBack {
        void singleFromClient(String pString);
        void listeFromClient(List<String> pListData);
        void quantiteFromClient(List<String> pListData);
        void connectedFromClient();
        } // interface

    private ClientCallBack mCallBack;

    private String mIp = "";
    private int mPort = 0;

    private List<String> mDatas = new ArrayList<String>();

    private Socket mSocket;
    private Connection mConnection;
    private ReadMessages mReadMessages;

    private PrintWriter mWriter = new PrintWriter(System.out, true);
    private BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));


    public Client(final ClientCallBack pCallback, final String pIp, final int pPort) {
        this.mCallBack = pCallback;
        this.mIp = pIp;
        this.mPort = pPort;
        mDatas.clear();
        } // constructeur

    public void connect() {
        mConnection = new Connection();
        mConnection.execute();
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

    private Boolean write(final EnumSendWord pEnumSendWord, final String pMessage) {

        Boolean nReturn = false; // valeur de retour par défaut

        mDatas.clear();

        if (null != mReadMessages) {
            if (mReadMessages.getStatus() == AsyncTask.Status.RUNNING) {
                String nMessage = pEnumSendWord.getValue();
                if (!pMessage.isEmpty()) {
                    nMessage = nMessage + " " + pMessage;
                    }
                mWriter.println(nMessage);
                nReturn = true;
                } // if
            } // if
        return nReturn;
        } // Boolean


    public void logout() {

        if(write(EnumSendWord.LOGOUT, "")) {
            mReadMessages.cancel(true);
            }

        if (null != mSocket) {
            mWriter.close();
            try {
                mReader.close();
                mSocket.close();
                } catch (IOException e) {
                e.printStackTrace();
                }
            } // if
        } // void

    private class Connection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // ceinture et bretelle: ce cas peut-il arriver ??
            //logout();
            Log.i("Client Connection", "Connection.onPreExecute");
            }

        @Override
        protected Boolean doInBackground(Void... pVoid) {

            // valeur de retour par défaut
            boolean nReturn = false;

            try {
                mSocket = new Socket(mIp, mPort);
                Log.i("Client Connection", "doInBackground connected");

                mWriter = new PrintWriter(mSocket.getOutputStream(), true);
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                nReturn= true;

                } catch (IOException e) {
                Log.i("Client Connection", "doInBackground IOException");
                e.printStackTrace();
                }

            return nReturn;
            } // boolean

        @Override
        protected void onPostExecute(Boolean nBoolean) {

            String nLog= nBoolean ? "Connected to server\n" : "Could not connect to server\n";
            Log.i("Client Connection", "onPostExecute " + nLog);

            if (nBoolean) {
                mReadMessages = new ReadMessages();
                mReadMessages.execute();

                mCallBack.connectedFromClient();
                } else {
                //fixme: prévenir l'utilisateur
                }
            } // void
        } // class


    private class ReadMessages extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            while (!isCancelled()) {
                try {
                    String nData = mReader.readLine();

                    if (!nData.isEmpty()) {
                        publishProgress(nData.trim());
                        }

                    } catch (IOException e) {
                    Log.i("Client ReadMessages", "doInBackground IOException");
                    e.printStackTrace();
                    break;
                    }
                }

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
                }

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
            }
        } // class
} // class