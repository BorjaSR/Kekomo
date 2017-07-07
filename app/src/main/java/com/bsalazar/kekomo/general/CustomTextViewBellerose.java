package com.bsalazar.kekomo.general;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by bsalazar on 06/07/2017.
 */

public class CustomTextViewBellerose extends TextView {


    public CustomTextViewBellerose(Context context) {
        super(context);
        fuente();
    }

    public CustomTextViewBellerose(Context context, AttributeSet attrs) {
        super(context, attrs);
        fuente();
    }

    public CustomTextViewBellerose(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            fuente();
    }

    private void fuente() {
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Bellerose.ttf");
        this.setTypeface(myCustomFont);
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }
}
