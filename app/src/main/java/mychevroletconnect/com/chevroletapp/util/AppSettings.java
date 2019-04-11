package mychevroletconnect.com.chevroletapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import mychevroletconnect.com.chevroletapp.app.Constants;


public class AppSettings {

    public static final String TAG = AppSettings.class.getSimpleName();


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    private String profile;

    


    public static AppSettings getAppSettingsFromSharedPreference(Context context) {
        AppSettings appSettings = new AppSettings();

        SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);



        appSettings.profile = settings.getString(Constants.PROFILE_SIGNATURE, "123");


        return appSettings;
    }



    public void save(Context context) {

        SharedPreferences settings = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();


        editor.putString(Constants.PROFILE_SIGNATURE, profile);

        editor.commit();
        //editor.apply();
    }


}
