package theron_b.com.visitetablette.tool;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import theron_b.com.visitetablette.tool.Listener.MyOnGlobalLayoutListener;

public class Tool {

    private double screenWidthInPixels;
    private double scale;
    private Context mContext;

    public Tool() {
        screenWidthInPixels = 0;
        scale = 0;
    }

    public Tool(Context context) {
        mContext = context;
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidthInPixels = (double)config.screenWidthDp * dm.density;
        if (screenWidthInPixels < 1920)
            scale = 1920 / screenWidthInPixels;
        if (screenWidthInPixels > 1920)
            scale = screenWidthInPixels / 1920;
    }

    public final int resizeContent(double resize) {
        if (screenWidthInPixels != 1920) {
            resize = resize / scale;
            return ((int)Math.round(resize));
        } else {
            return ((int)Math.round(resize));
        }
    }

    public int moveToCorrectPlace(int padding, int i) {
        if (screenWidthInPixels != 1920) {
            return (Math.round(padding / i));
        } else {
            return (0);
        }
    }

    public int setTextForVideo(int videoSize) {
        return ((int)screenWidthInPixels - videoSize);
    }

    public int resizePhoto(double resize, double photoSize) {
        if (screenWidthInPixels != 1920) {
            resize = resize / scale;
            return ((int)Math.round(resize));
        } else {
            return ((int)photoSize);
        }
    }

    public int getViewWidth(View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        MyOnGlobalLayoutListener myOnGlobalLayoutListener = new MyOnGlobalLayoutListener(view);
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(myOnGlobalLayoutListener);
        }
        while (!myOnGlobalLayoutListener.getM_Listened())
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return myOnGlobalLayoutListener.getM_Width();
    }
}
