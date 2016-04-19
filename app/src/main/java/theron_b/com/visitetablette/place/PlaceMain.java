package theron_b.com.visitetablette.place;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.object.PlacesObject;
import theron_b.com.visitetablette.gallery.BasicGallery;
import theron_b.com.visitetablette.tool.Param;
import theron_b.com.visitetablette.tool.Speaker;

public class PlaceMain extends AppCompatActivity {

    private PlacesObject    m_PlacesObject;

    private final int CHECK_CODE = 0x1;

    private Speaker         m_Speaker;
    private boolean         m_Play;
    private String          m_Text;

    private Activity        m_Activity;

    private boolean         m_French;
    private PlaceMainUI     m_PlaceMainUI;

    public static final String MY_OBJECT = "MY_OBJECT";

    @Bind(R.id.backgroundImageView)
    ImageView                           m_BackgroundImageView;

    @Bind(R.id.appBarLayout)
    AppBarLayout                        m_AppBarLayout;

    @Bind(R.id.toolbar)
    Toolbar                             m_Toolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout             m_CollapsingToolbarLayout;

    @Bind(R.id.placeMainInside)
    LinearLayout                        insideLayout;

    @Bind(R.id.placeMainPhoto)
    LinearLayout                        photoLayout;

    @Bind(R.id.placeMainVideo)
    LinearLayout                        videoLayout;

    @Bind(R.id.placeMainParentView)
    RelativeLayout                      parentView;

    @Bind(R.id.placeMainInsideTextView)
    TextView                            placeMainInsideTextView;

    @Bind(R.id.placeMainImageTextView)
    TextView                            placeMainImageTextView;

    @Bind(R.id.placeMainVideoTextView)
    TextView                            placeMainVideoTextView;

    @Bind(R.id.placeMainTextTextView)
    TextView                            placeMainTextTextView;

    @Bind(R.id.placeMainTextCardView)
    android.support.v7.widget.CardView  placeMainTextCardView;

    @Bind(R.id.placeMainInsideImageView)
    ImageView                           placeMainInsideImageView;

    @Bind(R.id.placeMainImageImageView)
    ImageView                           placeMainImageImageView;

    @Bind(R.id.placeMainVideoImageView)
    ImageView                           placeMainVideoImageView;

    @Bind(R.id.fab_speak)
    FloatingActionButton                floatingActionButton;

    @Bind(R.id.placeMainTextMoreButton)
    TextView                              placeMainTextMoreButton;

    @Bind(R.id.placeMainTextAlphaImage)
    ImageView                              placeMainTextAlphaImage;

    public PlaceMain() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.place_placemain);
        ButterKnife.bind(this);
        m_Activity = PlaceMain.this;
        m_PlacesObject = (PlacesObject) getIntent().getSerializableExtra(MY_OBJECT);
        m_French = getIntent().getBooleanExtra("french", true);
        setParam();
        setUI();
    }

    private void setUI() {
        m_AppBarLayout.setBackgroundColor(Color.parseColor(getIntent().getStringExtra("RGB")));
        m_PlaceMainUI = new PlaceMainUI(m_PlacesObject.getM_Path(), m_French);
        m_PlaceMainUI.setBackgroundImage(m_BackgroundImageView);
        m_PlaceMainUI.setCard(insideLayout, photoLayout, videoLayout, placeMainTextCardView);
        m_PlaceMainUI.setContent(getImageViewMap(m_PlaceMainUI), placeMainTextTextView);
        m_Play = false;
        m_Text = m_PlaceMainUI.getText();
        m_PlaceMainUI.setMoreButton(placeMainTextMoreButton, placeMainTextTextView, placeMainTextAlphaImage, placeMainTextCardView, parentView, m_Activity.getApplicationContext());
        checkTTS();
    }

    private Map<ImageView,String> getImageViewMap(PlaceMainUI placeMainUI) {
        Map<ImageView, String> imageViewStringMap = new HashMap<>();
        if (placeMainUI.isM_Inside())
            imageViewStringMap.put(placeMainInsideImageView, "/Interieur/");
        if (placeMainUI.isM_Image())
            imageViewStringMap.put(placeMainImageImageView, "/Photo/");
        if (placeMainUI.isM_Video())
            imageViewStringMap.put(placeMainVideoImageView, "/Video/");
        return  (imageViewStringMap);
    }

    private void setParam() {
        Param param = new Param();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        param.paramSetCollapsingActionBar(m_CollapsingToolbarLayout, m_PlacesObject.getM_Name(), getIntent().getStringExtra("RGB"), getIntent().getStringExtra("TextColor"));
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                m_Speaker = new Speaker(getApplicationContext(), m_French, m_Text);
            } else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_speak)
    public void onFabVisiteInfoClick() {
        if (m_Play) {
            floatingActionButton.setImageResource(R.mipmap.ic_play);
            m_Play = false;
            m_Speaker.pause();
        } else {
            if (m_Speaker.speak()) {
                floatingActionButton.setImageResource(R.mipmap.ic_pause);
                m_Play = true;
            } else {
                String message = m_French ? "Merci de patienter" : "Please wait";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_Speaker.destroy();
    }

    @Override
        protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onClickInside(View view) {
//        Intent intent = new Intent(m_Activity, MultipleGallery.class);
//        intent.putExtra("directory", m_PlacesObject.getM_Path() + "/" + "Interieur");
//        intent.putExtra("actionBarTitle", "Interieur");
//        intent.putExtra("actionBarColor", getIntent().getStringExtra("RGB"));
//        intent.putExtra("actionBarTitleColor", getIntent().getStringExtra("TextColor"));
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                m_Activity,
//                Pair.create((View)placeMainImageTextView, getResources().getString(R.string.TitleTransition))
//        );
//        ActivityCompat.startActivity(m_Activity, intent, options.toBundle());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                m_Activity,
                Pair.create((View)placeMainImageTextView, getResources().getString(R.string.TitleTransition))
        );
        launchVisitePlace(options, "Interieur");
    }


    public void onClickPhoto(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                m_Activity,
                Pair.create((View)placeMainImageTextView, getResources().getString(R.string.TitleTransition))
        );
        launchVisitePlace(options, "Photo");
    }

    public void onClickVideo(View view) {
        //todo
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                m_Activity,
//                Pair.create((View)placeMainImageTextView, getResources().getString(R.string.TitleTransition))
//        );
//        launchVisitePlace(options, "Video");
    }

    public void onClickMore(View view) {
        placeMainTextMoreButton.setVisibility(View.INVISIBLE);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        placeMainTextTextView.setLayoutParams(layoutParams);
        placeMainTextAlphaImage.setVisibility(View.INVISIBLE);
        ViewDimension viewDimension = new ViewDimension(placeMainTextCardView);
        viewDimension.execute();
        m_PlaceMainUI.removeEmptySpace();
    }

    private void launchVisitePlace(ActivityOptionsCompat options, String title) {
        Intent intent = new Intent(m_Activity, BasicGallery.class);
        intent.putExtra("directory", m_PlacesObject.getM_Path() + "/" + title);
        if (title.equals("Interieur")) {
            intent.putExtra("interieur", true);
        }
        intent.putExtra("actionBarTitle", title);
        intent.putExtra("actionBarColor", getIntent().getStringExtra("RGB"));
        intent.putExtra("actionBarTitleColor", getIntent().getStringExtra("TextColor"));
        ActivityCompat.startActivity(m_Activity, intent, options.toBundle());
    }

}
