package cr.ac.ucr.ecci.proyecto_arce_mall.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class Common {

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                                       context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo[] info =  manager.getAllNetworkInfo();

            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}