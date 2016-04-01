package theron_b.com.visitetablette.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import java.util.ArrayList;

import theron_b.com.visitetablette.object.PlacesObject;
import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.tool.BitmapLoader;
import theron_b.com.visitetablette.tool.DataExplorer;

public class VisitInfo {

    public VisitInfo(ArrayList<PlacesObject> m_listPlacesObjects) {
        CurrentState.getInstance().setM_placeObject(getPlaceObject(m_listPlacesObjects, "visite-info"));
    }

    private PlacesObject getPlaceObject(ArrayList<PlacesObject> m_listPlacesObjects, String string) {
        for (int i = 0; i < m_listPlacesObjects.size(); ++i) {
            if (m_listPlacesObjects.get(i).getM_Name().equals(string)) {
                return (m_listPlacesObjects.get(i));
            }
        }
        return null;
    }

    public void prepareImage(final Activity activity) {
        DataExplorer dataExplorer = new DataExplorer(CurrentState.getInstance().getM_placeObject().getM_Path());
        final BitmapLoader bitmapLoader = new BitmapLoader(dataExplorer.getImageInDirectory(""));
        bitmapLoader.setPaletteWithoutImage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(activity.getString(R.string.saved_info_visite_RGB), bitmapLoader.getRGB());
                editor.putString(activity.getString(R.string.saved_info_visite_title_text_color), bitmapLoader.getM_TitleTextColor());
                editor.apply();
            }
        }, 250);
    }

}
