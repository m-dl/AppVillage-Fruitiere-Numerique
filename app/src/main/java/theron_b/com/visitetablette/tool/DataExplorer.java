package theron_b.com.visitetablette.tool;

import android.os.Environment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import theron_b.com.visitetablette.object.PlacesObject;

public class DataExplorer {

    private MyString                       m_MyString;
    private ArrayList<PlacesObject>        m_ListPlacesObjects;
    private String                         m_Path;

    public DataExplorer(String path) {
        m_Path = path;
    }

    public DataExplorer(String path, GoogleMap googleMap) {
        m_MyString = new MyString();
        m_ListPlacesObjects = new ArrayList<>();
        File file = mountData(path);
        if (file != null)  {
            ArrayList<File> m_fileList = new ArrayList<>(Arrays.asList(file.listFiles()));
            fillMap(m_fileList, googleMap);
        }
    }

    private void fillMap(ArrayList<File> m_fileList, GoogleMap googleMap) {
        int end = m_fileList.size();
        for (int i = 0; i < end ; ++i) {
            if (!m_fileList.get(i).getName().contains("visite-info")) {
                if (!m_fileList.get(i).getName().contains("visite-overview")) {
                    StringBuilder Content = new StringBuilder();
                    try {
                        Content = fillContent(Content, m_fileList.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    addMarker(Content, googleMap, m_fileList.get(i).toString());
                } else {
                    m_ListPlacesObjects.add(new PlacesObject("visite-overview", m_fileList.get(i).toString()));
                }
            } else {
                m_ListPlacesObjects.add(new PlacesObject("visite-info", m_fileList.get(i).toString()));
            }
        }
    }

    private void addMarker(StringBuilder content, GoogleMap googleMap, String path) {
        String[] line = content.toString().split("\n");
        String title = line[0];
        String[] position = line[1].split(",");
        Double Lat = Double.parseDouble(position[0]);
        Double Lng = Double.parseDouble(position[1]);
        int rayon = 25;
        if (line.length > 2) {
            if (line[2].charAt(0) >= '0' && line[2].charAt(0) <= '9') {
                rayon = Integer.parseInt(line[2]);
            }
        }
        m_ListPlacesObjects.add(new PlacesObject(title, path, rayon));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Lat, Lng))
                .title(title));
    }

    private StringBuilder fillContent(StringBuilder content, File file) throws IOException {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new FileReader(new File(file, "/marker.txt")));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line);
            content.append('\n');
        }
        return (content);
    }

    private File mountData(String file) {
        //file = m_MyString.stringNormalizer(file);
        //file = "visite-village";
        File sdcard = new File(Environment.getExternalStorageDirectory() + "/VisiteTablette/" + file);
        if (!sdcard.exists())
            return null;
        return (sdcard);
    }

    public ArrayList<PlacesObject> getM_ListPlacesObjects() {
        return m_ListPlacesObjects;
    }

    public String getImageInDirectory(String value) {
        File file = new File(m_Path + value);
        ArrayList<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
        Collections.sort(files);
        MyString myString = new MyString();
        for (int i = 0 ; i < files.size() ; ++i) {
            if (myString.isImage(files.get(i).toString())) {
                return (files.get(i).toString());
            }
        }
        return null;
    }

}
