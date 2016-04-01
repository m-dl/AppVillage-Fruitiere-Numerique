package theron_b.com.visitetablette.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import theron_b.com.visitetablette.gallery.helper.TouchImageView;
import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.tool.BitmapLoader;

public class FullScreenImageAdapter extends PagerAdapter {

    static int i = 0;

	private Activity _activity;
	private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;
    private BitmapLoader bitmapLoader;
    // constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this._activity = activity;
		this._imagePaths = imagePaths;
        bitmapLoader = null;
	}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;
 
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
 
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //todo
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);
//        ++i;
//        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
//        if (bitmapLoader == null) {
//            bitmapLoader = new BitmapLoader(_imagePaths.get(position));
//            bitmapLoader.setImage(options);
//            imgDisplay.setImageBitmap(bitmapLoader.getM_Bitmap());
//        } else {
//            if (i > 3) {
//                bitmapLoader.destroy();
//                bitmapLoader = null;
//            }
//            bitmapLoader = new BitmapLoader(_imagePaths.get(position));
//            bitmapLoader.setImage(options);
//            imgDisplay.setImageBitmap(bitmapLoader.getM_Bitmap());
//        }
        
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		}); 

        ((ViewPager) container).addView(viewLayout);
 
        return viewLayout;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }

}
