package theron_b.com.visitetablette.main.user_interface;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.main.CurrentState;
import theron_b.com.visitetablette.tool.BitmapLoader;
import theron_b.com.visitetablette.tool.MyString;

public class MarkerCardPresentation extends Fragment{

    private Boolean             m_Status;
    private Float               m_Distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_marker_card, container, false);
        setText(view);
        setImage(view);
        return view;
    }

    private void setText(View view) {
        TextView textView1 = (TextView) view.findViewById(R.id.MarkerTitle);
        textView1.setText(CurrentState.getInstance().getM_placeObject().getM_Name());
        TextView textView2 = (TextView) view.findViewById(R.id.time);
        textView2.setText(getTime());
        TextView textView3 = (TextView) view.findViewById(R.id.statusVisite);
        textView3.setText(getStatus());
    }

    private String getStatus() {
        if (m_Status != null) {
            return (CurrentState.getInstance().getM_french() ?
                    m_Status ? "Commencer la visite" : "Veuillez vous rapprocher pour commencer la visite"
                    : m_Status ? "Start the visite" : "Please get closer to start the visit");
        }
        return null;
    }

    public void setStatusAndDistance(int rayon, LatLng placePosition, LatLng userPosition) {
        Location placeLocation = new Location("placeLocation");
        placeLocation.setLatitude(placePosition.latitude);
        placeLocation.setLongitude(placePosition.longitude);
        if (userPosition != null) {
            Location userLocation = new Location("userLocation");
            userLocation.setLatitude(userPosition.latitude);
            userLocation.setLongitude(userPosition.longitude);
            m_Distance = placeLocation.distanceTo(userLocation);
            m_Status = CurrentState.getInstance().isM_DemoMode() || m_Distance <= rayon;
        }
    }

    public String getTime() {
        if (m_Distance != null) {
            int time = Math.round(m_Distance / (400.0f / 6.0f));
            return (CurrentState.getInstance().getM_french() ? "\t\t\t\tA " + time + "\nminutes de vous" : "\t\t\t\tAt " + time + "\nfrom you");
        }
        return null;
    }

    public void setImage(View view) {
        File lookImage = new File(CurrentState.getInstance().getM_placeObject().getM_Path());
        ArrayList<File> inLookImage = new ArrayList<>(Arrays.asList(lookImage.listFiles()));
        File image = null;
        MyString mystring = new MyString();
        for (int i = 0 ; i < inLookImage.size() ; ++i) {
            if (mystring.isImage(inLookImage.get(i).toString())) {
                image = new File(inLookImage.get(i).toString());
                break;
            }
        }
        final BitmapLoader bitmapLoader = new BitmapLoader(String.valueOf(image));
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        int widht = 600;
        int height = (int)Math.round(widht / 1.7777777777777777777777777777778);
        bitmapLoader.setImageView(imageView, widht, height, true, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CurrentState.getInstance().setM_RGB(bitmapLoader.getRGB());
                CurrentState.getInstance().setM_TextColor(bitmapLoader.getM_TitleTextColor());
            }
        }, 250);
    }

}
