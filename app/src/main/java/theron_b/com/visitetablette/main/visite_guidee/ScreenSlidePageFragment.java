//package theron_b.com.visitetablette.visite_guidee;
//
//import android.app.Fragment;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Environment;
//import android.speech.tts.TextToSpeech;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import theron_b.com.visitetablette.R;
//import theron_b.com.visitetablette.main.MainActivity;
//
//
//public class ScreenSlidePageFragment extends Fragment {
//    public static final String ARG_PAGE = "page";
//
//    private int mPageNumber;
//
//    public static ScreenSlidePageFragment create(int pageNumber) {
//        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, pageNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public ScreenSlidePageFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPageNumber = getArguments().getInt(ARG_PAGE);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//        ViewGroup rootView = (ViewGroup) inflater
//                .inflate(R.layout.fragment_screen_slide_page, container, false);
//
//        TextView textView = (TextView) rootView.findViewById(R.id.guideContent);
//        VideoView videoView = (VideoView) rootView.findViewById(R.id.guideVideo);
//
//        File sdcard = Environment.getExternalStorageDirectory();
//        File checkMounted = new File(sdcard + "/VisiteTablette");
//        if (!checkMounted.isDirectory())
//            sdcard = new File("/mnt/sdcard2/");
//        String path = "/VisiteTablette/demo/";
//        StringBuilder Content = new StringBuilder();
//        boolean succed = true;
//        try {
//            BufferedReader bufferedReader;
//            if (MainActivity.french)
//                bufferedReader = new BufferedReader(new FileReader(new File(sdcard + path,  + getPageNumber() + "/" + getPageNumber() + "_fr.txt")));
//            else
//                bufferedReader = new BufferedReader(new FileReader(new File(sdcard + path,  + getPageNumber() + "/" + getPageNumber() + "_en.txt")));
//            String line;
//
//            while ((line = bufferedReader.readLine()) != null) {
//                Content.append(line);
//                Content.append('\n');
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            succed = false;
//        }
//
//        if (succed) {
//            textView.setText(Content);
//
//            Intent checkIntent = new Intent();
//            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//            startActivityForResult(checkIntent, 0x01);
//        }
//
//        videoView.setVideoPath("/storage/emulated/0/VisiteTablette/demo/" + getPageNumber() + "/" + getPageNumber() + ".mp4");
//        videoView.start();
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//
//        videoView.setLayoutParams(params);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//
//        textView.setShadowLayer(5, 3, 3, Color.WHITE);
//        textView.setTextColor(Color.BLACK);
//        return rootView;
//    }
//
//    public int getPageNumber() {
//        return mPageNumber;
//    }
//}
