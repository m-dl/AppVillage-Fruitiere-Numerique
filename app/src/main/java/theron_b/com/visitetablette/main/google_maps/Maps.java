package theron_b.com.visitetablette.main.google_maps;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.main.CurrentState;
import theron_b.com.visitetablette.main.user_interface.MarkerCardPresentation;
import theron_b.com.visitetablette.object.PlacesObject;
import theron_b.com.visitetablette.tool.DataExplorer;

public class Maps implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap m_GoogleMap;
    private GetLocation m_GetLocation;
    private ArrayList<PlacesObject> m_ListPlacesObjects;

    public Maps() {
        MapFragment mapFragment = (MapFragment) CurrentState.getInstance().getM_FragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        m_GoogleMap = map;
    }

    public void cinqMinutes(Context context, Activity activity, LatLng latLng) {
        if (latLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            m_GoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            LayoutInflater inflater = activity.getLayoutInflater();
            View layout = inflater.inflate(R.layout.five_minute,
                    (ViewGroup) activity.findViewById(R.id.toast_layout_root));
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 60);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    public void centerCamera(LatLng currentLatLng) {
        if (currentLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(18).build();
            m_GoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void centerCamera(LatLng currentLatLng, float zoom) {
        if (currentLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(zoom).build();
            m_GoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (!CurrentState.getInstance().getM_FABButton()) {
            CurrentState.getInstance().removeFragment();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!CurrentState.getInstance().getM_FABButton()) {
            CurrentState.getInstance().removeFragment();
            MarkerCardPresentation markerCardPresentation = new MarkerCardPresentation();
            for (int i = 0; i < m_ListPlacesObjects.size(); ++i) {
                if (m_ListPlacesObjects.get(i).getM_Name().equals(marker.getTitle())) {
                    CurrentState.getInstance().setM_placeObject(m_ListPlacesObjects.get(i));
                }
            }
            markerCardPresentation.setStatusAndDistance(CurrentState.getInstance().getM_placeObject().getM_Rayon(), marker.getPosition(), m_GetLocation.getM_CurrentLatLng());
            FragmentTransaction transaction = CurrentState.getInstance().getM_FragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up);
            transaction.add(R.id.fragment, markerCardPresentation);
            transaction.commit();
            centerCamera(marker.getPosition(), m_GoogleMap.getCameraPosition().zoom);
            CurrentState.getInstance().setM_fragment(true);
            CurrentState.getInstance().setM_smallcard(true);
        }
        return true;
    }

    public void loadVisite(String element) {
        m_GoogleMap.clear();
        DataExplorer dataExplorer = new DataExplorer(element, m_GoogleMap);
        m_ListPlacesObjects = dataExplorer.getM_ListPlacesObjects();
    }

    public ArrayList<PlacesObject> getM_ListPlacesObjects() {
        return m_ListPlacesObjects;
    }

    public void setLocation(GetLocation m_getLocation) {
        m_GetLocation = m_getLocation;
    }

    public void changeView(boolean enblabled) {
        if (!enblabled)
            m_GoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else
            m_GoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}