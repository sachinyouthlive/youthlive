package com.yl.youthlive.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 8/9/2017.
 */

public class custom_textfont extends android.support.v7.widget.AppCompatTextView {

    private Context c;
    public custom_textfont(Context c) {
        super(c);
        this.c = c;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/Champagne_Limousines_Bold.ttf");
        setTypeface(tfs);

    }
    public custom_textfont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/Champagne_Limousines_Bold.ttf");
        setTypeface(tfs);
        // TODO Auto-generated constructor stub
    }

    public custom_textfont(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        Typeface tfs = Typeface.createFromAsset(c.getAssets(),
                "fonts/Champagne_Limousines_Bold.ttf");
        setTypeface(tfs);

    }

}
