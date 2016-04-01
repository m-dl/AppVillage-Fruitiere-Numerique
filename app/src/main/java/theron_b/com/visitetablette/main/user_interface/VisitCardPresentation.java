package theron_b.com.visitetablette.main.user_interface;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.tool.BitmapLoader;
import theron_b.com.visitetablette.tool.MyString;
import theron_b.com.visitetablette.object.PlacesObject;


public class VisitCardPresentation extends Fragment {

    private PlacesObject        m_PlaceObject;
    private String              m_Visite;
    private View                m_View;
    private Boolean             m_French;

    public VisitCardPresentation() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_View = inflater.inflate(R.layout.main_fragment_visit_card, container, false);
        String path = m_PlaceObject.getM_Path();
        setVisitePicture(path);
        setVisiteContent(path);
        setVisiteName();
        setVisiteTime(path);
        return m_View;
    }

    private void setVisitePicture(String path) {
        ArrayList<File> image = getVisitePictureImagePath(path);
        for (int i = 0 ; i < 3 ; ++i) {
            BitmapLoader bitmapLoader = new BitmapLoader(String.valueOf(image.get(i)));
            ImageView imageView = (ImageView) m_View.findViewById(R.id.visiteimage1 + i);
            int widht = (int)Math.round(400 * 1.75);
            int height = (int)Math.round(226 * 1.75);
            bitmapLoader.setImageView(imageView, widht, height);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(widht, height);
            layoutParams.setMargins(0, 100, 0, 0);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.visiteDuration);
            if (i == 0)
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.visiteTitle);
            else if (i == 1)
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            else
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.visiteTitle);
            imageView.setLayoutParams(layoutParams);
        }
    }

    private ArrayList<File> getVisitePictureImagePath(String path) {
        File lookImage = new File(path);
        ArrayList<File> inLookImage = new ArrayList<>(Arrays.asList(lookImage.listFiles()));
        int i = 0;
        int j = 0;
        ArrayList<File> image = new ArrayList<>();
        MyString myString = new MyString();
        while (i < inLookImage.size()) {
            if (myString.isImage(inLookImage.get(i).toString())) {
                if (j < 3)
                    image.add(j++, new File(inLookImage.get(i).toString()));
            }
            ++i;
        }
        return image;
    }

    private void setVisiteTime(String path) {
        MyString myString = new MyString();
        TextView textView = (TextView) m_View.findViewById(R.id.visiteDuration);
        String file;
        if (m_French)
            file = "/duree_fr.txt";
        else
            file = "/duree_en.txt";
        textView.setText(myString.getStringFromFile(path + file));
    }

    private void setVisiteName() {
        TextView textView = (TextView) m_View.findViewById(R.id.visiteTitle);
        textView.setText(m_Visite);
    }

    private void setVisiteContent(String path) {
        MyString myString = new MyString();
        TextView textView = (TextView) m_View.findViewById(R.id.visiteContent);
        String file;
        if (m_French)
            file = "/content_fr.txt";
        else
            file = "/content_en.txt";
        textView.setText(myString.getStringFromFile(path + file));
    }

    public void setM_PlaceObject(PlacesObject m_PlaceObject) {
        this.m_PlaceObject = m_PlaceObject;
    }

    public void setM_French(Boolean m_French) {
        this.m_French = m_French;
    }

    public void setM_Visite(String m_Visite) {
        this.m_Visite = m_Visite;
    }

}
