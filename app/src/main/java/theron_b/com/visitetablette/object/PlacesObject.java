package theron_b.com.visitetablette.object;

import java.io.Serializable;

public class PlacesObject implements Serializable {
    private String m_Name;
    private String m_Path;
    private int    m_Rayon;

    public PlacesObject() {
    }

    public PlacesObject(String name, String path) {
        m_Rayon = 0;
        m_Name = name;
        m_Path = path;
    }

    public PlacesObject(String name, String path, int rayon) {
        m_Rayon = rayon;
        m_Name = name;
        m_Path = path;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public String getM_Path() {
        return m_Path;
    }

    public void setM_Path(String m_Path) {
        this.m_Path = m_Path;
    }

    public int getM_Rayon() {
        return m_Rayon;
    }
}