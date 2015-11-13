package crepes.fr.androcrepes.commons.framework;

import android.app.ProgressDialog;
import android.content.Context;

import crepes.fr.androcrepes.R;

/**
 * Created by vince on 13/11/2015.
 */
public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomProgressBar);
        setIndeterminate(true);
    } // constructeur

    public void showMessage(final String pMessage, final boolean pCancelable) {
        setMessage(pMessage);
        setCancelable(pCancelable);
    } // void
} // class
