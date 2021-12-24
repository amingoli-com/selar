package amingoli.com.selar.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

import amingoli.com.selar.R;


public class LoaderButton extends RelativeLayout {

    public Btn btn;
    public AVLoadingIndicatorView loader;

    String btntext, indicator;
    int btncolor, loaderIndicatorColor;
    int indicatorID;
    float btnTextSize;

    public static int btn_id = (int) 124l;
    public static int loader_id = (int) 1242;

    public LoaderButton(Context context) {
        this(context, null);
    }

    public LoaderButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, null);
    }

    public LoaderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //btn = new Btn(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
        btn = new Btn(context);
        loader = new AVLoadingIndicatorView(context, attrs, R.style.AVLoadingIndicatorView);
        init(context, attrs);
    }


    @SuppressLint("WrongConstant")
    public void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoaderButton, 0, 0);
        btntext = a.getString(R.styleable.LoaderButton_btnText);
        btncolor = a.getInteger(R.styleable.LoaderButton_btnTextColor, Color.WHITE);

        btnTextSize = a.getDimensionPixelSize(R.styleable.LoaderButton_btnTextSize, dpToPx(14));

        //a.getDimension(R.styleable.LoaderButton_btnTextSize, 14);
        loaderIndicatorColor = a.getInteger(R.styleable.LoaderButton_loaderIndicatorColor, context.getResources().getColor(R.color.primary));
        indicatorID = a.getInteger(R.styleable.LoaderButton_loaderIndicator, 0);


        switch (indicatorID) {
            case 0:
                indicator = "BallPulseIndicator";
                break;
            case 1:
                indicator = "BallGridPulseIndicator";
                break;
            case 2:
                indicator = "BallClipRotateIndicator";
                break;
            case 3:
                indicator = "BallClipRotatePulseIndicator";
                break;
            case 4:
                indicator = "SquareSpinIndicator";
                break;
            case 5:
                indicator = "BallClipRotateMultipleIndicator";
                break;
            case 6:
                indicator = "BallPulseRiseIndicator";
                break;
            case 7:
                indicator = "BallRotateIndicator";
                break;
            case 8:
                indicator = "CubeTransitionIndicator";
                break;
            case 9:
                indicator = "BallZigZagIndicator";
                break;
            case 10:
                indicator = "BallZigZagDeflectIndicator";
                break;
            case 11:
                indicator = "BallTrianglePathIndicator";
                break;
            case 12:
                indicator = "BallScaleIndicator";
                break;
            case 13:
                indicator = "LineScaleIndicator";
                break;
            case 14:
                indicator = "LineScalePartyIndicator";
                break;
            case 15:
                indicator = "BallScaleMultipleIndicator";
                break;
            case 16:
                indicator = "BallPulseSyncIndicator";
                break;
            case 17:
                indicator = "BallBeatIndicator";
                break;
            case 18:
                indicator = "LineScalePulseOutIndicator";
                break;
            case 19:
                indicator = "LineScalePulseOutRapidIndicator";
                break;
            case 20:
                indicator = "BallScaleRippleIndicator";
                break;
            case 21:
                indicator = "BallScaleRippleMultipleIndicator";
                break;
            case 22:
                indicator = "BallSpinFadeLoaderIndicator";
                break;
            case 23:
                indicator = "LineSpinFadeLoaderIndicator";
                break;
            case 24:
                indicator = "TriangleSkewSpinIndicator";
                break;
            case 25:
                indicator = "PacmanIndicator";
                break;
            case 26:
                indicator = "BallGridBeatIndicator";
                break;
            case 27:
                indicator = "SemiCircleSpinIndicator";
                break;
        }

        a.recycle();


        LayoutParams btn_lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        btn_lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        btn.setLayoutParams(btn_lp);
        btn.setText(btntext);
        btn.setTextColor(btncolor);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        btn.setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        btn.setBackgroundResource(outValue.resourceId);
        btn.setId(btn_id);
        btn.setTag(btn_id);


        LayoutParams loading_lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        loading_lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        loader.setLayoutParams(loading_lp);
        loader.setScrollBarStyle(R.style.AVLoadingIndicatorView);
        loader.setIndicator(indicator);
        loader.setIndicatorColor(loaderIndicatorColor);
        loader.setId(loader_id);
        loader.setTag(loader_id);
        loader.setVisibility(GONE);

        this.addView(btn, 0);
        this.addView(loader, 1);


    }


    public void setBtnText(String str) {
        btn.setText(str);
    }

    public void setBtnTextSize(float size) {
        btn.setTextSize(size);
    }

    public void setIndicator(String indicator) {
        loader.setIndicator(indicator);
    }

    public void showLoader() {
        ////Log.e("meyy","showLoader called");
        btn.setVisibility(GONE);
        loader.setVisibility(VISIBLE);
    }

    public void hideLoader() {
        ////Log.e("meyy","hideLoader called");
        btn.setVisibility(VISIBLE);
        loader.setVisibility(GONE);
    }

    public void setEnable(boolean enable) {
        btn.setEnabled(enable);
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
