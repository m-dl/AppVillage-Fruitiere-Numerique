//package theron_b.com.visitetablette.visite_guidee;
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v13.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import java.io.File;
//import theron_b.com.visitetablette.R;
//import theron_b.com.visitetablette.main.MainActivity;
//
//public class ScreenSlideActivity extends FragmentActivity {
//
//    private int NUM_PAGES = 5;
//
//    private ViewPager mPager;
//
//    private PagerAdapter mPagerAdapter;
//    public static Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_screen_slide);
//
//        NUM_PAGES = getNumberOfVideo();
//        mPager = (ViewPager) findViewById(R.id.pager);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
//        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                invalidateOptionsMenu();
//            }
//        });
//        context = getApplicationContext();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
//        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
//
//        if (MainActivity.french) {
//            MenuItem item2 = menu.findItem(R.id.action_previous);
//            item2.setTitle(R.string.action_precedent);
//
//            MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
//                    (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
//                            ? R.string.action_fini
//                            : R.string.action_suivant);
//            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        } else {
//            MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
//                    (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
//                            ? R.string.action_finish
//                            : R.string.action_next);
//            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_previous:
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//                return true;
//
//            case R.id.action_next:
//                if (mPager.getCurrentItem() + 1 == NUM_PAGES)
//                    ScreenSlideActivity.this.finish();
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public int getNumberOfVideo() {
//        File sdcard = Environment.getExternalStorageDirectory();
//        File checkMounted = new File(sdcard + "/VisiteTablette");
//        File[] files;
//
//        if (!checkMounted.isDirectory())
//            sdcard = new File("/mnt/sdcard2/");
//        String path = "/VisiteTablette/demo/";
//        sdcard = new File(sdcard + path);
//        files = sdcard.listFiles();
//        return files.length;
//    }
//
//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return ScreenSlidePageFragment.create(position);
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//    }
//}
