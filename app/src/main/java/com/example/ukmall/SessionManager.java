package com.example.ukmall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        }
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, Login.class);

        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public String getTrimmedEmail() {
        String email = sharedPreferences.getString(NAME, "");
        return email.substring(0, email.indexOf("@"));
    }

    public String getUsername() {

        return getTrimmedEmail();
    }
}
