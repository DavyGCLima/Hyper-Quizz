package com.example.davy.projetoic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * Created by reida on 21/03/2018.
 */

public class AndroidUtils {
    @SuppressLint("ShowToast")
    public static boolean isNeworkAvailble(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null)
                return false;
            else {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }catch (SecurityException e){
            Toast.makeText(context, "Coneção falha: " + e.getClass().getSimpleName() + e.getMessage(), Toast.LENGTH_SHORT);
        }
        return false;
    }
}
