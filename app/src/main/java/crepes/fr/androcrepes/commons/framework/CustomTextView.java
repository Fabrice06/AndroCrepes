package crepes.fr.androcrepes.commons.framework;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vince on 21/11/2015.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context pContext, AttributeSet pAttributeSet, int pDefStyle) {
        super(pContext, pAttributeSet, pDefStyle);
        init();
    }

    public CustomTextView(Context pContext, AttributeSet pAttributeSet) {
        super(pContext, pAttributeSet);
        init();
    }

    public CustomTextView(Context pContext) {
        super(pContext);
        init();
    }

    private void init() {
        Typeface nTypeface = Typeface.createFromAsset(getContext().getAssets(), "Milasian.ttf");
        setTypeface(nTypeface);
        setTextSize(30);
    }

}
