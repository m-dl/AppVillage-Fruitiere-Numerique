package theron_b.com.visitetablette.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import theron_b.com.visitetablette.main.MainActivity;

/**
 * Created by Maxime
 */
public class ConnectionManager {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
