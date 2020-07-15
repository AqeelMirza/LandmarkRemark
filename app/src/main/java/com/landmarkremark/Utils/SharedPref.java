package com.landmarkremark.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

public class SharedPref {

    private static SharedPreferences mPrefs;
    private static String landmarkPref = "LandmarkPref";
    private static String isLoggedIn = "IsLoggedIn";
    private static String loggedId = "LoggedId";
    //Constants
    public static String MarkedNote = "markedNote";
    public static String User = "user";

    private static SharedPreferences getPreferences(Context ctxt) {
        if (mPrefs == null) {
            mPrefs = ctxt.getSharedPreferences(landmarkPref, Context.MODE_PRIVATE);
        }

        return mPrefs;
    }

    public static Boolean getIsLoggedIn() {
        return getPreferences(LandmarkRemark.getContext()).getBoolean(SharedPref.isLoggedIn, false);
    }

    public static void setIsLoggedIn(Boolean val) {
        getPreferences(LandmarkRemark.getContext()).edit().putBoolean(SharedPref.isLoggedIn, val).apply();
    }

    public static String getloggedId() {
        return getPreferences(LandmarkRemark.getContext()).getString(SharedPref.loggedId, "");
    }

    public static void setLoggedId(String val) {
        getPreferences(LandmarkRemark.getContext()).edit().putString(SharedPref.loggedId, val).apply();
    }

    public static void clearPref() {
        getPreferences(LandmarkRemark.getContext()).edit().clear().commit();
    }

    public static String getID() {

        return "" + ((int) (Math.random() * 9000) + 1000);
    }

    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }
}
