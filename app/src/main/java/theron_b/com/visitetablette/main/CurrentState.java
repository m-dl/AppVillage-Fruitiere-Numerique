package theron_b.com.visitetablette.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.object.PlacesObject;

public class CurrentState {

    public static CurrentState uniqueInstance;

    private FragmentManager m_FragmentManager;

    private boolean m_FABButton;
    private boolean m_french;
    private boolean m_fragment;
    private boolean m_smallcard;
    private boolean m_Satellite;
    private boolean m_DemoMode;

    private PlacesObject    m_placeObject;
    private String          m_RGB;
    private String          m_TextColor;

    private CurrentState() {
        m_FABButton = false;
        m_french = true;
        m_fragment = false;
        m_smallcard = false;
        m_DemoMode = false;
        m_RGB = null;
    }

    public void removeFragment() {
        if (m_fragment) {
            Fragment fragment = m_FragmentManager.findFragmentById(R.id.fragment);
            FragmentTransaction fragmentTransaction = m_FragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up);
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            m_fragment = false;
        }
    }

    public boolean getM_french() {
        return m_french;
    }

    public void setM_french(boolean m_french) {
        this.m_french = m_french;
    }

    public FragmentManager getM_FragmentManager() {
        return m_FragmentManager;
    }


    public void setM_fragment(boolean m_fragment) {
        this.m_fragment = m_fragment;
    }

    public boolean getM_FABButton() {
        return m_FABButton;
    }

    public void setM_FABButton(boolean m_FABButton) {
        this.m_FABButton = m_FABButton;
    }

    public void setM_smallcard(boolean m_smallcard) {
        this.m_smallcard = m_smallcard;
    }

    public boolean getM_smallcard() {
        return m_smallcard;
    }

    public PlacesObject getM_placeObject() {
        return m_placeObject;
    }

    public void setM_placeObject(PlacesObject m_placeObject) {
        this.m_placeObject = m_placeObject;
    }

    public String getM_RGB() {
        if (m_RGB == null)
            m_RGB = "#a6da57";
        return m_RGB;
    }

    public void setM_RGB(String m_RGB) {
        this.m_RGB = m_RGB;
    }

    public void setM_TextColor(String textColor) {
        this.m_TextColor = textColor;
    }

    public String getM_TextColor() {
        return m_TextColor;
    }

    public boolean getM_Satellite() {
        return m_Satellite;
    }

    public void setM_Satellite(boolean satellite) {
        m_Satellite = satellite;
    }

    public boolean isM_DemoMode() {
        return m_DemoMode;
    }

    public void setM_DemoMode(boolean m_DemoMode) {
        this.m_DemoMode = m_DemoMode;
    }

    public void setM_FragmentManager(FragmentManager m_FragmentManager) {
        this.m_FragmentManager = m_FragmentManager;
    }


    public static CurrentState getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new CurrentState();
        return uniqueInstance;
    }
}
