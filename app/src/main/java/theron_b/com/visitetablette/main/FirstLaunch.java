package theron_b.com.visitetablette.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import theron_b.com.visitetablette.R;

public class FirstLaunch extends AppCompatActivity {
    @Bind(R.id.visitTabletDirectoryTextView)
    TextView mTextViewDirectory;

//    @Bind(R.id.visitTabletPhotosphereTextView)
//    TextView mTextViewPhotosphere;

    @Bind(R.id.visitTabletGPSTextView)
    TextView mTextViewGPS;

//    @Bind(R.id.visitTabletDirectoryButton)
//    Button mButtonDirectory;

//    @Bind(R.id.visitTabletPhotosphereButton)
//    Button mButtonPhotosphere;

    @Bind(R.id.visitTabletOkay)
    Button mButtonOkay;

    @Bind(R.id.visitTabletGPSButton)
    Button mButtonGPS;

    private boolean mOkay = true;

    private boolean mGPS = true;
    private boolean mStorage = true;
    private boolean mGoogle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_launch);
        ButterKnife.bind(this);
        checkDirectory();
        checkPhotosphere();
        checkPermission();
        checkAll();
        save();
    }

    private void checkAll() {
        if (mOkay)
            mButtonOkay.setVisibility(View.VISIBLE);
    }

    private void checkDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        boolean directoryExist = new File(sdcard + "/VisiteTablette").exists();
        String message =  directoryExist ? getResources().getString(R.string.visitTabletDirectoryIsHere) :
                getResources().getString(R.string.visitTabletDirectoryIsNotHere);
        //if (!directoryExist)
        //    mOkay = false;
        mTextViewDirectory.setText(message);
    }

    private void checkPhotosphere() {
    //todo
//        String message =  directoryExist ? getResources().getString(R.string.visitTabletDirectoryIsHere) :
//                getResources().getString(R.string.visitTabletDirectoryIsNotHere);
//        if (directoryExist)
//            mButtonDirectory.setVisibility(View.GONE);
//        mTextViewDirectory.setText(message);
    }

    private void checkPermission() {
        String message = "Je n'ai pas accès :";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            message += getResources().getString(R.string.visitTabletNoAccessToGPS);
            mGPS = false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            message += getResources().getString(R.string.visitTabletNoAccessToStockage);
            mStorage = false;
        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
//            message += getResources().getString(R.string.visitTabletNoAccessToServiceGoogle) + " (j'ai besoin d'accéder au contact pour ça, oui, cette permision est étrange)";
//            mGoogle = false;
//        }
        if (message.equals("Je n'ai pas accès :")) {
            mButtonGPS.setVisibility(View.GONE);
            message = "J'ai accès à tout :-)";
        } else {
            mOkay = false;
        }
        mTextViewGPS.setText(message);

    }

    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.visitSharedPreference),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences .edit();
        editor.putBoolean(getResources().getString(R.string.is_first_launch), false);
        editor.apply();
    }

    @OnClick(R.id.visitTabletGPSButton)
    public void onClickGPSButton(View view) {
        if (!mStorage) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
            mStorage = true;
        }
        if (!mGPS) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
            mGPS = true;
        }
//        if (!mGoogle) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    0);
//             mGoogle = true;
//        }
        mTextViewGPS.setText("J'ai accès à tout :-)");
        mButtonOkay.setVisibility(View.VISIBLE);
        mButtonGPS.setVisibility(View.GONE);
    }

    @OnClick(R.id.visitTabletOkay)
    public void onClickOkayButton(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

}
