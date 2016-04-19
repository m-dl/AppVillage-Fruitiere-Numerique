package theron_b.com.visitetablette.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.ArrayList;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.main.user_interface.VisitCardPresentation;
import theron_b.com.visitetablette.object.PlacesObject;

public class VisiteData {

    private ArrayList<PlacesObject> m_ListPlacesObjects;
    
    public VisiteData() {

    }

    public VisiteData(ArrayList<PlacesObject> list) {
        m_ListPlacesObjects = list;
    }

    public void displayVisitPresentation(FragmentManager fragmentManager, FloatingActionButton user_position, FloatingActionButton info, boolean french, String visite) {
        VisitCardPresentation visitCardPresentation = new VisitCardPresentation();
        visitCardPresentation.setM_French(french);
        visitCardPresentation.setM_PlaceObject(getOverviewInfo());
        visitCardPresentation.setM_Visite(visite);
        user_position.setVisibility(View.INVISIBLE);
        info.setVisibility(View.INVISIBLE);
        MyFragmentTransaction myFragmentTransaction = new MyFragmentTransaction(visitCardPresentation, fragmentManager);
        if(myFragmentTransaction != null)
            myFragmentTransaction.run();
    }

    private PlacesObject getOverviewInfo() {
        PlacesObject tmp = null;
        for (int i = 0 ; i < m_ListPlacesObjects.size() ; ++i) {
            if (m_ListPlacesObjects.get(i).getM_Name().equals("visite-overview")) {
                tmp = m_ListPlacesObjects.get(i);
            }
        }
        return tmp;
    }

    public boolean visitHasInfo() {
        for (int i = 0 ; i < m_ListPlacesObjects.size() ; ++i) {
            if (m_ListPlacesObjects.get(i).getM_Name().equals("visite-info"))
                return true;
        }
        return false;
    }

    public ArrayList<PlacesObject> getM_ListPlacesObjects() {
        return m_ListPlacesObjects;
    }

    public void setM_ListPlacesObjects(ArrayList<PlacesObject> m_ListPlacesObjects) {
        this.m_ListPlacesObjects = m_ListPlacesObjects;
    }

    class MyFragmentTransaction extends Thread {
        private VisitCardPresentation m_VisitCardPresentation;
        private FragmentManager         m_FragmentManager;

        public MyFragmentTransaction(VisitCardPresentation visitCardPresentation, FragmentManager fragmentManager) {
            m_VisitCardPresentation = visitCardPresentation;
            m_FragmentManager = fragmentManager;
        }

        public void run() {
            FragmentTransaction transaction = m_FragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up);
            transaction.add(R.id.fragment, m_VisitCardPresentation);
            transaction.commit();
        }

    }

}
