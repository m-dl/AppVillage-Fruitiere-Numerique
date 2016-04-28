package theron_b.com.visitetablette.main.google_maps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class GetLocation implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient                 m_GoogleApiClient;

    private Location                        m_LastLocation;
    private Location                        m_CurrentLocation;

    private LocationRequest                 m_LocationRequest;
    private LocationListener                m_LocationListener;

    private LatLng                          m_CurrentLatLng;

    private String                          m_LatitudeText;
    private String                          m_LongitudeText;
    private String                          m_LastUpdateTime;

    private Boolean                         m_RequestingLocationUpdates;
    private Boolean                         m_CameraCentered;

    private Maps                            m_Maps;

    private Context                         m_Context;


    public GetLocation(Context context, Boolean requestingLocationUpdates, Maps maps) {
        m_Context = context;
        m_RequestingLocationUpdates = requestingLocationUpdates;
        m_Maps = maps;
        buildGoogleApiClient();
        m_LocationListener = this;
        m_CameraCentered = false;
        createLocationRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        m_GoogleApiClient = new GoogleApiClient.Builder(m_Context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        m_GoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        m_LocationRequest = new LocationRequest();
        m_LocationRequest.setInterval(1000);
        m_LocationRequest.setFastestInterval(500);
        m_LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        m_LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                m_GoogleApiClient);
        if (m_LastLocation != null) {
            m_LatitudeText = (String.valueOf(m_LastLocation.getLatitude()));
            m_LongitudeText = (String.valueOf(m_LastLocation.getLongitude()));
        }
        if (m_RequestingLocationUpdates)
            startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (m_LocationRequest == null) {
            createLocationRequest();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                m_GoogleApiClient, m_LocationRequest, m_LocationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Error", "GetLocation.java get an error in onConnectionSuspended method [msg]: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Error", "GetLocation.java get an error in onConnectionFailed method [msg]: " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        m_CurrentLocation = location;
        m_CurrentLatLng = new LatLng(m_CurrentLocation.getLatitude(), m_CurrentLocation.getLongitude());
        m_LastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if (!m_CameraCentered && m_Maps != null) {
            m_Maps.centerCamera(m_CurrentLatLng);
            m_CameraCentered = true;
        }
    }

    public void stopLocationUpdates() {
        if (m_GoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    m_GoogleApiClient, m_LocationListener);
    }

    public void onResume() {
        if (m_GoogleApiClient.isConnected() && !m_RequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    public GoogleApiClient getM_GoogleApiClient() {
        return m_GoogleApiClient;
    }

    public void setM_GoogleApiClient(GoogleApiClient m_GoogleApiClient) {
        this.m_GoogleApiClient = m_GoogleApiClient;
    }

    public Location getM_LastLocation() {
        return m_LastLocation;
    }

    public void setM_LastLocation(Location m_LastLocation) {
        this.m_LastLocation = m_LastLocation;
    }

    public Location getM_CurrentLocation() {
        return m_CurrentLocation;
    }

    public void setM_CurrentLocation(Location m_CurrentLocation) {
        this.m_CurrentLocation = m_CurrentLocation;
    }

    public LocationRequest getM_LocationRequest() {
        return m_LocationRequest;
    }

    public void setM_LocationRequest(LocationRequest m_LocationRequest) {
        this.m_LocationRequest = m_LocationRequest;
    }

    public LocationListener getM_LocationListener() {
        return m_LocationListener;
    }

    public void setM_LocationListener(LocationListener m_LocationListener) {
        this.m_LocationListener = m_LocationListener;
    }

    public String getM_LatitudeText() {
        return m_LatitudeText;
    }

    public void setM_LatitudeText(String m_LatitudeText) {
        this.m_LatitudeText = m_LatitudeText;
    }

    public String getM_LongitudeText() {
        return m_LongitudeText;
    }

    public void setM_LongitudeText(String m_LongitudeText) {
        this.m_LongitudeText = m_LongitudeText;
    }

    public String getM_LastUpdateTime() {
        return m_LastUpdateTime;
    }

    public void setM_LastUpdateTime(String m_LastUpdateTime) {
        this.m_LastUpdateTime = m_LastUpdateTime;
    }

    public Boolean getM_RequestingLocationUpdates() {
        return m_RequestingLocationUpdates;
    }

    public void setM_RequestingLocationUpdates(Boolean m_RequestingLocationUpdates) {
        this.m_RequestingLocationUpdates = m_RequestingLocationUpdates;
    }

    public LatLng getM_CurrentLatLng() {
        return m_CurrentLatLng;
    }

    public void setM_CurrentLatLng(LatLng m_CurrentLatLng) {
        this.m_CurrentLatLng = m_CurrentLatLng;
    }

}
