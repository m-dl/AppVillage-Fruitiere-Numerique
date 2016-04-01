//package theron_b.com.visitetablette.gallery;
//
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import theron_b.com.visitetablette.R;
//import theron_b.com.visitetablette.gallery.Urry.RecyclerViewFragment;
//
//public class MultipleGallery extends AppCompatActivity {
//
//    @InjectView(R.id.toolbar)
//    Toolbar toolbar;
//    @InjectView(R.id.viewpager)
//    ViewPager viewPager;
//    @InjectView(R.id.tabs)
//    TabLayout tabLayout;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.galery_multiplegallery);
//
//        ButterKnife.inject(this);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
////                if (position == 0)
////                    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment.newInstance();
////                    return RecyclerViewFragment.newInstance();
////                else
//                return RecyclerViewFragment.newInstance();
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return "Tab " + position;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
//
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}