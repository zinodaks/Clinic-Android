package com.example.infinitycenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "keyusername";
    private static final String F_NAME = "keyfirstname";
    private static final String L_NAME = "keylastname";
    private static final String DATE_OF_BIRTH = "keydob";
    private static final String MANAGER = "keymanager";
    private static SharedPreferencesManager sharedPreferencesManager;
    private static Context context;

    private SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return sharedPreferencesManager;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(user.getfName() != null ) {
            editor.putString(USERNAME, user.getUsername());
            editor.putString(F_NAME, user.getfName());
            editor.putString(L_NAME, user.getlName());
            editor.putString(DATE_OF_BIRTH, user.getDateOfBirth());
        } else {
            editor.putString(USERNAME , user.getUsername());
            editor.putString(MANAGER , "yes");
        }
        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME , null) != null;
    }

    public boolean isManager() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        return sharedPreferences.getString(MANAGER , null) != null ;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(USERNAME , null) ,
                        sharedPreferences.getString(F_NAME , null) ,
                        sharedPreferences.getString(L_NAME , null) ,
                        sharedPreferences.getString(DATE_OF_BIRTH , null));
    }

    public void logout(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context , Login.class));
    }

}
