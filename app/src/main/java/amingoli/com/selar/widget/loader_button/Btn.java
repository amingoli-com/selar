package amingoli.com.selar.widget.loader_button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class Btn extends AppCompatButton {
    public Btn(Context context) {
        super(context);
        init(context);
    }

    public Btn(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyle(attrs);
        init(context);
    }

    public Btn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStyle(attrs);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            setTypeface(null, getTypeface().getStyle());
            ////Log.e("called","----------------------->"+getTypeface().getStyle());
        }
    }

    public void setStyle(AttributeSet attrs){
        if (attrs != null) {
            try {
                int style = attrs.getAttributeIntValue(

                        "http://schemas.android.com/apk/res/android",
                        "textStyle",
                        Typeface.NORMAL);

                setTypeface(null, style);

                ////Log.e("called","02----------------------->"+style);

            }
            catch (Exception e) {
                ////Log.e("errrrrrr", e.getMessage());
            }
        }
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        String fontPath = "fonts/iran_sans_mobile.ttf";
        if(style== Typeface.BOLD){
            fontPath = "fonts/iran_sans_mobile.ttf";
        }
        tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        super.setTypeface(tf);

    }

    @Override
    public void setTypeface(Typeface tf) {
        String fontPath = "fonts/iran_sans_mobile.ttf";
        tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        super.setTypeface(tf);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
