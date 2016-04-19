package theron_b.com.visitetablette.main;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.kimo.konamicode.KonamiCode;
import io.kimo.konamicode.KonamiCodeLayout;
import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.main.google_maps.GetLocation;
import theron_b.com.visitetablette.main.google_maps.Maps;
import theron_b.com.visitetablette.place.PlaceMain;
import theron_b.com.visitetablette.tool.FileManager;
import theron_b.com.visitetablette.tool.Param;
import theron_b.com.visitetablette.tool.TakePicture;
import theron_b.com.visitetablette.tool.clock.Clock;
import theron_b.com.visitetablette.tool.clock.OnClockTickListner;

import static theron_b.com.visitetablette.tool.ConnectionManager.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

    private int updateActivityNb = 0;
    private Maps m_Maps;
    private GetLocation m_GetLocation;
    private VisiteData m_VisiteData;

    private Context m_Context;
    private static MainActivity instance;
    private Activity m_Activity;
    private FragmentManager m_FragmentManager;

    private Clock m_Clock;

    private FloatingActionButton m_FABButton;
    private int m_IDFABButton;

    TextView m_Time;

    // Constructor
    public MainActivity() {
        instance = this;
    }

    // Getter
    public static Context getContext() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Bind(R.id.main_content_main)
    CoordinatorLayout m_CoordinatorLayout;

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout m_DrawerLayout;

    @Bind(R.id.navigationView)
    NavigationView m_NavigationView;

    @Bind(R.id.fab_user_position)
    FloatingActionButton m_FABUserPosition;

    @Bind(R.id.fab_visit_info)
    FloatingActionButton m_FABVisitInfo;

    private ActionBarDrawerToggle   m_DrawerToggle;
    private MenuItem                m_MenuItemSatellite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.main_main_activity);
        ButterKnife.bind(this);
        Param param = new Param();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
        // auto pin the app
        startLockTask();
        // empty visitors pictures folder
        TakePicture.updateVisitorPhoto();
        // if wifi -> try to updata media
        if(isNetworkAvailable()) {
            FileManager.UpdateMedia();
        }
        // create visits dynamically and the menu
        FileManager.ListVisits(m_NavigationView, CurrentState.getInstance().getM_french());
        setObjectParam();
        setDrawer();
        setTime();
        setKonamiCode();
        alertDialog();
        updateValuesFromBundle(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.visitSharedPreference),
                Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getResources().getString(R.string.is_first_launch), true)) {
            startActivity(new Intent(this, FirstLaunch.class));
            finish();
        }
    }

    private void setKonamiCode() {
        new KonamiCode.Installer(this)
                .callback(new KonamiCodeLayout.Callback() {
                    @Override
                    public void onFinish() {
                        if (!CurrentState.getInstance().isM_DemoMode()) {
                            CurrentState.getInstance().setM_DemoMode(true);
                            Toast.makeText(m_Context, "Mode demo ON", Toast.LENGTH_LONG).show();
                        } else {
                            CurrentState.getInstance().setM_DemoMode(false);
                            Toast.makeText(m_Context, "Mode demo OFF", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .on(this)
                .install();
    }

    private void setObjectParam() {
        m_Context = getApplicationContext();
        m_Activity = MainActivity.this;
        m_FragmentManager = getFragmentManager();
        CurrentState.getInstance().setM_FragmentManager(m_FragmentManager);
        m_Maps = new Maps();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            m_GetLocation = new GetLocation(m_Context, true, m_Maps);
        else
            Toast.makeText(m_Context, "Je ne peux pas accèder au GPS", Toast.LENGTH_LONG).show();
        m_Maps.setLocation(m_GetLocation);
        m_IDFABButton = View.generateViewId();
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);
        m_MenuItemSatellite = null;
    }

    private void alertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("The application is currently in french");
        alertDialogBuilder
                .setMessage("Pass the application in english?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CurrentState.getInstance().setM_french(false);
                        m_NavigationView.getMenu().clear();
                        FileManager.ListVisits(m_NavigationView, CurrentState.getInstance().getM_french());
                        presentTheDrawer();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CurrentState.getInstance().setM_french(true);
                        presentTheDrawer();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void presentTheDrawer() {
        m_DrawerLayout.openDrawer(GravityCompat.START);
        m_Time = (TextView) m_NavigationView.findViewById(R.id.time);
        setClock();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                m_DrawerLayout.closeDrawers();
            }
        }.start();

    }

    public void setDrawer() {
        m_DrawerLayout.setDrawerListener(m_DrawerToggle);
        m_DrawerLayout.isDrawerOpen(m_NavigationView);
        m_NavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    MenuItem m_menuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        m_DrawerLayout.closeDrawers();
                        m_menuItem = menuItem;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Resources resources = getResources();
                                if ((m_menuItem.getTitle().equals(resources.getString(R.string.action_section_3)) ||
                                        m_menuItem.getTitle().equals(resources.getString(R.string.action_section_english_3))) && m_MenuItemSatellite == null) {
                                    m_MenuItemSatellite = m_menuItem;
                                }
                                if (m_MenuItemSatellite != null) {
                                    String MenuTitle = "";
                                    if (CurrentState.getInstance().getM_Satellite()) {
                                        if (CurrentState.getInstance().getM_french())
                                            MenuTitle = resources.getString(R.string.action_section_4);
                                        else
                                            MenuTitle = resources.getString(R.string.action_section_english_4);
                                    } else {
                                        if (CurrentState.getInstance().getM_french())
                                            MenuTitle = resources.getString(R.string.action_section_3);
                                        else
                                            MenuTitle = resources.getString(R.string.action_section_english_3);
                                    }
                                    m_MenuItemSatellite.setTitle(MenuTitle);
                                }
                                m_menuItem.setChecked(true);
                                navigationDrawerItemSelected(m_menuItem.getItemId(), m_menuItem.getTitle().toString());
                            }
                        }, 250);
                        return false;
                    }
                });
    }

    public void navigationDrawerItemSelected(int position, String title) {
        if (position > 0) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                CurrentState.getInstance().removeFragment();
                prepareVisit(title);
                CurrentState.getInstance().setM_fragment(true);
            } else
                Toast.makeText(m_Context, "Je ne peux accèder à la mémoire", Toast.LENGTH_LONG).show();
        } else {
            Resources resources = getResources();
            if (title.equals(resources.getString(R.string.action_section_1)) || title.equals(resources.getString(R.string.action_section_english_1))) {
                TakePicture takePicture = new TakePicture(m_Activity);
                takePicture.photo();
            } else if (title.equals(resources.getString(R.string.action_section_2)) || title.equals(resources.getString(R.string.action_section_english_2))) {
                CurrentState.getInstance().removeFragment();
                if (m_FABButton != null)
                    m_FABButton.performClick();
                m_Maps.cinqMinutes(m_Context, m_Activity, m_GetLocation.getM_CurrentLatLng());

            } else {
                if (!CurrentState.getInstance().getM_Satellite()) {
                    m_Maps.changeView(true);
                    CurrentState.getInstance().setM_Satellite(true);
                } else {
                    m_Maps.changeView(false);
                    CurrentState.getInstance().setM_Satellite(false);
                }
                m_NavigationView.refreshDrawableState();
            }
        }
    }

    private void prepareVisit(String title) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
        m_Maps.loadVisite(title);
        if (title.equals("Endroits Eloignés") || title.equals("Distant Places")) {
            m_Maps.centerCamera(m_GetLocation.getM_CurrentLatLng(), 15);
        }
        m_VisiteData = new VisiteData(m_Maps.getM_ListPlacesObjects());
        m_VisiteData.displayVisitPresentation(m_FragmentManager, m_FABUserPosition, m_FABVisitInfo, CurrentState.getInstance().getM_french(), title);
        if (m_FABButton == null)
            addFABButton();
    }

    private void addFABButton() {
        CurrentState.getInstance().setM_FABButton(true);
        m_FABButton = new FloatingActionButton(this);
        m_FABButton.setRippleColor(Color.parseColor("#0099cc"));
        m_FABButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_done, null));
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.bottomMargin = 35;
        m_FABButton.setLayoutParams(layoutParams);
        m_FABButton.setAnimation(m_FABUserPosition.getAnimation());
        m_CoordinatorLayout.addView(m_FABButton);
        m_FABButton.setId(m_IDFABButton);
        m_FABButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewManager) m_FABButton.getParent()).removeView(m_FABButton);
                if (m_VisiteData.visitHasInfo()) {
                    m_FABVisitInfo.setVisibility(View.VISIBLE);
                    VisitInfo visitInfo = new VisitInfo(m_Maps.getM_ListPlacesObjects());
                    visitInfo.prepareImage(m_Activity);

                } else {
                    m_FABVisitInfo.setVisibility(View.INVISIBLE);
                }
                m_FABUserPosition.setVisibility(View.VISIBLE);
                CurrentState.getInstance().removeFragment();
                m_FABButton = null;
                CurrentState.getInstance().setM_FABButton(false);
            }
        });
    }

    public void setTime() {
        setClock();
        m_Clock = new Clock(this);
        m_Clock.AddClockTickListner(new OnClockTickListner() {
            @Override
            public void OnMinuteTick(Time currentTime) {
                setClock();
            }
        });
    }

    private void setClock() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        String hour = (h < 10) ? "0" + h + "" : h + "";
        int m = calendar.get(Calendar.MINUTE);
        String minute = (m < 10) ? "0" + m + "" : m + "";
        if (m_Time != null)
            m_Time.setText(hour + "h" + minute);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (m_GetLocation != null)
            m_GetLocation.stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        // reload menu if update medias - it's activity nb < 3
        if(updateActivityNb < 3) {
            m_NavigationView.getMenu().clear();
            FileManager.ListVisits(m_NavigationView, CurrentState.getInstance().getM_french());
            updateActivityNb++;
        }
        if (m_GetLocation != null)
           m_GetLocation.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (m_GetLocation != null) {
            savedInstanceState.putBoolean("RequestingLocationUpdates",
                    m_GetLocation.getM_RequestingLocationUpdates());
            savedInstanceState.putParcelable("CurrentLocation", m_GetLocation.getM_CurrentLocation());
            savedInstanceState.putString("LastUpdateTime", m_GetLocation.getM_LastUpdateTime());
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains("RequestingLocationUpdate")) {
                m_GetLocation.setM_RequestingLocationUpdates(savedInstanceState.getBoolean(
                        "RequestingLocationUpdate"));
            }
            if (savedInstanceState.keySet().contains("CurrentLocation")) {
                m_GetLocation.setM_CurrentLocation((Location) savedInstanceState.getParcelable("CurrentLocation"));
            }
            if (savedInstanceState.keySet().contains("LastUpdateTime")) {
                m_GetLocation.setM_LastUpdateTime(savedInstanceState.getString(
                        "LastUpdateTime"));
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        m_DrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return m_DrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_user_position)
    public void onFabUserPositionClick() {
        if (CurrentState.getInstance().isM_DemoMode())
            m_Maps.centerCamera(new LatLng(43.763660, 5.362316));
        else {
            if (m_GetLocation != null)
                m_Maps.centerCamera(m_GetLocation.getM_CurrentLatLng());
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_visit_info)
    public void onFabVisitInfoClick() {
        //todo change this animation and fix
        SharedPreferences sharedPreferences = m_Activity.getPreferences(Context.MODE_PRIVATE);
        CurrentState.getInstance().setM_TextColor(sharedPreferences.getString(getResources().getString(R.string.saved_info_visite_title_text_color), "#ffffff"));
        CurrentState.getInstance().setM_RGB(sharedPreferences.getString(getResources().getString(R.string.saved_info_visite_RGB), "#a6da57"));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                m_Activity,
                Pair.create((View)m_FABVisitInfo, getResources().getString(R.string.ImageTransition))
        );
        launchVisitPlace(options);
    }

    public void onClickCardview(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                m_Activity,
                Pair.create(view.findViewById(R.id.image), getResources().getString(R.string.ImageTransition))
        );
        launchVisitPlace(options);
    }

    private void launchVisitPlace(ActivityOptionsCompat options) {
        Intent intent = new Intent(m_Activity, PlaceMain.class);
        intent.putExtra(PlaceMain.MY_OBJECT, CurrentState.getInstance().getM_placeObject());
        intent.putExtra("RGB", CurrentState.getInstance().getM_RGB());
        intent.putExtra("TextColor", CurrentState.getInstance().getM_TextColor());
        intent.putExtra("french", CurrentState.getInstance().getM_french());
        ActivityCompat.startActivity(m_Activity, intent, options.toBundle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_Clock.StopTick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String title = CurrentState.getInstance().getM_french() ? "Voulez vous vraiment quitter l'application ?" : "Do you really wan't to quit this app?";
        String message = CurrentState.getInstance().getM_french() ? "Tous vos données seront remises à zéro" : "All your data will be reset";
        String positive = CurrentState.getInstance().getM_french() ? "Oui" : "Yes";
        String negative = CurrentState.getInstance().getM_french() ? "Non" : "No";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
