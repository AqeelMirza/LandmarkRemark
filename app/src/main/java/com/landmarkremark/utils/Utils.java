package com.landmarkremark.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class Utils {

    private static SharedPreferences appPrefs;
    private static final String landmarkPref = "LandmarkPref";
    private static final String isLoggedIn = "IsLoggedIn";
    private static final String loggedId = "LoggedId";
    //Constants
    public static final String MarkedNote = "markedNote";
    public static final String home_label = "Home";
    public static final String myLandmarks_label = "My Landmarks";
    public static final String users = "users";
    public static String username;

    private static SharedPreferences getPreferences(Context ctxt) {
        if (appPrefs == null) {
            appPrefs = ctxt.getSharedPreferences(landmarkPref, Context.MODE_PRIVATE);
        }
        return appPrefs;
    }

    public static Boolean getIsLoggedIn() {
        return getPreferences(LandmarkRemark.getContext()).getBoolean(Utils.isLoggedIn, false);
    }

    public static void setIsLoggedIn(Boolean val) {
        getPreferences(LandmarkRemark.getContext()).edit().putBoolean(Utils.isLoggedIn, val).apply();
    }

    public static String getLoggedInUserId() {
        return getPreferences(LandmarkRemark.getContext()).getString(Utils.loggedId, "");
    }

    public static void setLoggedId(String val) {
        getPreferences(LandmarkRemark.getContext()).edit().putString(Utils.loggedId, val).apply();
    }

    public static void clearPref() {
        getPreferences(LandmarkRemark.getContext()).edit().clear().commit();
    }

    public static int createRandomInt() {
        return ((int) (Math.random() * 9000) + 1000);
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    //getting the current address of the user
    public static String convertLatLngToAddress(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No Address Found", e);
        }
        if (!addresses.isEmpty()) {
            Address returnAddress = addresses.get(0);
            Log.i("Return Address: ", returnAddress.getAddressLine(0));
            return returnAddress.getAddressLine(0);
        }
        return "";
    }
}
