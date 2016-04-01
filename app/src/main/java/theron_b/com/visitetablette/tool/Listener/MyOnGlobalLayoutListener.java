package theron_b.com.visitetablette.tool.Listener;

import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import theron_b.com.visitetablette.R;

public class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private int m_Width;
    private int m_Height;

    private boolean m_Listened;

    private View    m_View;

    public MyOnGlobalLayoutListener(View view) {
        m_View = view;
        m_Height = 0;
        m_Width = 0;
        m_Listened = false;
    }

    @Override
    public void onGlobalLayout() {
        m_View.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        setM_Width(m_View.getWidth());
        setM_Height(m_View.getHeight());
        m_Listened = true;
    }

    public int getM_Width() {
        return m_Width;
    }

    public void setM_Width(int m_Width) {
        this.m_Width = m_Width;
    }

    public int getM_Height() {
        return m_Height;
    }

    public void setM_Height(int m_Height) {
        this.m_Height = m_Height;
    }

    public boolean getM_Listened() {
        return m_Listened;
    }
}
