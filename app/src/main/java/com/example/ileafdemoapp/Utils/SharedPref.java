package com.example.ileafdemoapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Clint on 8/2/18.
 */

public class SharedPref {

    private static SharedPreferences demoapp;

    public static final String SH_REMEMBERME_STATUS = "remembermestatus";
    public static final String SH_REM_USERNAME = "remembermusername";
    public static final String SH_REME_PASS = "remembermepass";
    public static final String SH_PREFS_ALREADY_LOGGED = "already_logged_in";

    private static  String SH_PREFS_NAME = "demapp";

    public static SharedPreferences getAppSharedPref(Context context) {
        if (demoapp == null) {
            demoapp = context.getSharedPreferences(SH_PREFS_NAME, Context.MODE_PRIVATE);
        }
        return demoapp;
    }

    public static Boolean getRememberMeLoggedIn_Status(Context context) {
        demoapp = getAppSharedPref(context);
        return demoapp.getBoolean(SH_REMEMBERME_STATUS, false);
    }

    public static void setRememberMeLoggedIn_Status(Context context, Boolean status) {
        demoapp = getAppSharedPref(context);
        demoapp.edit().putBoolean(SH_REMEMBERME_STATUS, status).apply();
    }

    public static void setRememberMeUserNameandPassword(Context context, String username,String passsword) {
        demoapp = getAppSharedPref(context);
        demoapp.edit().putString(SH_REM_USERNAME, username).apply();
        demoapp.edit().putString(SH_REME_PASS, passsword).apply();
    }

    public static void setRememberMeUserName(Context context, String username){


        demoapp = getAppSharedPref(context);
        demoapp.edit().putString(SH_REM_USERNAME, username).apply();

    }

    public static String getRememberMeUserName(Context context) {
        demoapp = getAppSharedPref(context);
        return  demoapp.getString(SH_REM_USERNAME, "");

    }


    public static void setRememberMePassword(Context context, String password){

        demoapp = getAppSharedPref(context);
        demoapp.edit().putString(SH_REME_PASS, password).apply();
    }

    public static String getRememberMePassword(Context context) {

        demoapp = getAppSharedPref(context);
        return  demoapp.getString(SH_REME_PASS, "");
    }



    public static void removeRememberMeUserNameandPassword(Context context) {
        demoapp = getAppSharedPref(context);
        demoapp.edit().remove(SH_REM_USERNAME).apply();
        demoapp.edit().remove(SH_REME_PASS).apply();
    }

    public static Boolean getLoggedIn_Status(Context context) {
        demoapp = getAppSharedPref(context);
        return demoapp.getBoolean(SH_PREFS_ALREADY_LOGGED, false);
    }

    public static void setLoggedIn_Status(Context context, Boolean status) {
        demoapp = getAppSharedPref(context);
        demoapp.edit().putBoolean(SH_PREFS_ALREADY_LOGGED, status).apply();
    }

    public static void clear(Context context) {
        demoapp.edit().clear().apply();


    }
}
