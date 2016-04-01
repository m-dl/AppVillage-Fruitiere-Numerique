package theron_b.com.visitetablette.place;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.Container;

import java.io.File;
import java.util.Map;

import theron_b.com.visitetablette.tool.BitmapLoader;
import theron_b.com.visitetablette.tool.DataExplorer;
import theron_b.com.visitetablette.tool.MyString;

public class PlaceMainUI {
    private String  m_Path;
    private String  m_Text;

    private boolean m_French;
    private boolean m_Inside;
    private boolean m_Image;
    private boolean m_Video;

    private ImageView m_FakeImageView;

    public PlaceMainUI(String path, boolean french) {
        m_Path = path;
        m_French = french;
        m_FakeImageView = null;
    }

    public void setCard(LinearLayout insideLayout, LinearLayout photoLayout, LinearLayout videoLayout, CardView placeMainTextCardView) {
        m_Inside = isContent("Interieur");
        m_Image = isContent("Photo");
        m_Video = isContent("Video");
        if (!m_Inside) {
            insideLayout.setVisibility(View.GONE);
        }
        if (!m_Image) {
            photoLayout.setVisibility(View.GONE);
        }
        if (!m_Video) {
            videoLayout.setVisibility(View.GONE);
        }
        if (!m_Inside && !m_Image && !m_Video) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            placeMainTextCardView.setLayoutParams(layoutParams);
        }
    }

    public void setBackgroundImage(ImageView m_BackgroundImageView) {
        DataExplorer dataExplorer = new DataExplorer(m_Path);
        BitmapLoader bitmapLoader = new BitmapLoader(dataExplorer.getImageInDirectory(""));
        bitmapLoader.setImageView(m_BackgroundImageView, 1024, 384, true);
        if (bitmapLoader.isM_FourByThree()) {
            CollapsingToolbarLayout.LayoutParams layoutParams;
            layoutParams = new CollapsingToolbarLayout.LayoutParams(600, 600);
            layoutParams.gravity = Gravity.CENTER;
            m_BackgroundImageView.setLayoutParams(layoutParams);
        }
    }

    private boolean isContent(String directory) {
        File file = new File(m_Path + "/" + directory + "/");
        return file.exists();
    }

    public boolean isM_Inside() {
        return m_Inside;
    }

    public boolean isM_Image() {
        return m_Image;
    }

    public boolean isM_Video() {
        return m_Video;
    }

    public void setContent(Map<ImageView, String> imageViewStringMap, TextView textView) {
        for (Map.Entry<ImageView, String> entry : imageViewStringMap.entrySet()) {
            setImageView(entry.getKey(), entry.getValue());
        }
        setTextView(textView);
    }

    private void setImageView(ImageView key, String value) {
        DataExplorer dataExplorer = new DataExplorer(m_Path);
        BitmapLoader bitmapLoader = new BitmapLoader(dataExplorer.getImageInDirectory(value));
        bitmapLoader.setImageView(key, 933, 525);
    }

    private void setTextView(TextView textView) {
        MyString myString = new MyString();
        String file;
        file = m_French ? "/content_fr.txt" : "/content_en.txt";
        StringBuilder text = myString.getStringFromFile(m_Path + file);
        m_Text = text.toString();
        textView.setText(text);
    }

    public String getText() {
        return m_Text;
    }

    public void setMoreButton(TextView placeMainTextMoreButton, TextView placeMainTextTextView, ImageView placeMainTextAlphaImage,
                              CardView placeMainTextCardView, RelativeLayout parentView, Context context) {
        int textSize = 400;
        if (m_Text.length() >= textSize) {
            placeMainTextMoreButton.setVisibility(View.VISIBLE);
            String text = m_French ? "Plus..." : "More...";
            placeMainTextMoreButton.setText(text);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, textSize * 2);
            placeMainTextTextView.setLayoutParams(layoutParams);
            fillEmptySpace(parentView, new ImageView(context));
        } else {
            placeMainTextMoreButton.setVisibility(View.INVISIBLE);
            placeMainTextAlphaImage.setVisibility(View.INVISIBLE);
        }
        ViewDimension viewDimension = new ViewDimension(placeMainTextCardView);
        viewDimension.execute();
    }

    private void fillEmptySpace(RelativeLayout parentView, ImageView imageView) {
        m_FakeImageView = imageView;
        m_FakeImageView.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                1300));
        parentView.addView(m_FakeImageView);
    }

    public void removeEmptySpace() {
        m_FakeImageView.setVisibility(View.GONE);
        m_FakeImageView = null;
    }
}
