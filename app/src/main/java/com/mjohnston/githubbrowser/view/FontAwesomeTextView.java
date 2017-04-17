package com.mjohnston.githubbrowser.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;

/**
 * TextView that will use the FontAwesome font
 */
public class FontAwesomeTextView extends AppCompatTextView {

    protected String text;

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesomeTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        if (!isInEditMode()) {
            Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "FontAwesome.otf");
            setTypeface(fontAwesome);

            if (getText() != null) {
                setText(Html.fromHtml(getText().toString()));
            } else {
                setText("");
            }
        }


    }

}
