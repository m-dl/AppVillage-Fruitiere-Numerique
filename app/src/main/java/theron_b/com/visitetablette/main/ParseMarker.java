package theron_b.com.visitetablette.main;

import android.os.Environment;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.tool.MyString;

public class ParseMarker {

    public ParseMarker() {

    }

    public GoogleMap initParseMarker(GoogleMap googleMap, LatLng userLocation, String visite) {
        if (visite.equals("endroits eloignes")) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(userLocation).zoom(15).build()));
        }
        else {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(userLocation).zoom(18).build()));
        }
        googleMap.clear();
        File sdcard = Environment.getExternalStorageDirectory();
        File checkMounted = new File(sdcard + "/VisiteTablette");

        if (!checkMounted.isDirectory())
            sdcard = new File("/storage/extSdCard");
        visite = visite.replaceAll(" ", "-");
        String path = "/VisiteTablette/" + visite;

        File pointEloigne = new File(sdcard + path);
        ArrayList<File> directory = new ArrayList<>(Arrays.asList(pointEloigne.listFiles()));
        for (int i = 0 ; i < directory.size() ; ++i) {
            if (!directory.get(i).getName().equals("visite-overview") || !directory.get(i).getName().equals("visite-info")) {
                MyString myString = new MyString();
                String[] line =myString.getStringFromFile(directory.get(i) + "/marker.txt").toString().split("\n");
                String title = line[0];
                String[] position = line[1].split(",");
                Double Lat = Double.parseDouble(position[0]);
                Double Lng = Double.parseDouble(position[1]);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Lat, Lng))
                        .title(title));
            }
        }
        return (googleMap);
    }
}
