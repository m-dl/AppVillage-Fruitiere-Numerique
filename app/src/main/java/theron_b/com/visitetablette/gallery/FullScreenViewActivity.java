package theron_b.com.visitetablette.gallery;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;

import theron_b.com.visitetablette.gallery.adapter.FullScreenImageAdapter;
import theron_b.com.visitetablette.R;

public class FullScreenViewActivity extends Activity{

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_fullscreen_view);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		int position = getIntent().getIntExtra("position", 0);

        ArrayList<String> imagePaths = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra("filesPath")));
        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                imagePaths);

		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);

//        if (!MainActivity.french) {
//            Button button = (Button) findViewById(R.id.btnClose);
//            button.setText("Back");
//        }
	}
}
