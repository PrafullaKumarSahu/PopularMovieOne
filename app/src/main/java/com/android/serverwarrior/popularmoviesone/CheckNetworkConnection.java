package com.android.serverwarrior.popularmoviesone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Server Warrior on 5/15/2016.
 */
public class CheckNetworkConnection {
    private static ConnectivityManager cm;

    public static boolean isNetwork(Context context)
    {
        CheckNetworkConnection.cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = CheckNetworkConnection.cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
