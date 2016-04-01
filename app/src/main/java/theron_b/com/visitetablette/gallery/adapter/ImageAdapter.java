package theron_b.com.visitetablette.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.tool.BitmapLoader;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<File> m_Image;
    private ArrayList<Bitmap> m_Bitmap;
    private String transition;

    public ImageAdapter(Context context1, ArrayList<File> image, String string) {
        transition = string;
        m_Image = image;
        context = context1;
        m_Bitmap = new ArrayList<>();
        ArrayListImage arrayListImage = new ArrayListImage();
        arrayListImage.run();
    }

    @Override
    public int getCount() {
        return m_Image.size();
    }

    @Override
    public Object getItem(int position) {
        return m_Image.get(position);
    }

    @Override
    public long getItemId(int position) {
        return m_Bitmap.get(position).getGenerationId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 338));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setTransitionName(transition);
        imageView.setImageBitmap(m_Bitmap.get(position));
        return imageView;
    }

    class ArrayListImage extends Thread {

        public ArrayListImage() {
        }

        public void run() {
            for (int i = 0 ; i < m_Image.size() ; ++i) {
                BitmapLoader bitmapLoader = new BitmapLoader(m_Image.get(i).toString());
                bitmapLoader.setImage(600, 338);
                m_Bitmap.add(bitmapLoader.getM_Bitmap());
            }
        }
    }

    public void onDestroy() {
        m_Image.clear();
        m_Bitmap.clear();
    }

}
