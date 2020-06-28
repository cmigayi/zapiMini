package com.example.zapimini.localStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zapimini.data.User;

public class UserLocalStorage {
    final static String mUserLocalStorage = "UserLocalStorage";
    SharedPreferences localStorage;
    SharedPreferences.Editor editor;

    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USERLOGGEDIN = "userLoggedIn";
    public static final String USERBUSINESSADDED = "userBusinessAdded";
    public static final String DATETIME= "date_time";

    public UserLocalStorage(Context context) {
        localStorage = context.getSharedPreferences("user", 0);
        editor = localStorage.edit();
    }

    public void storeUser(User user) {
            editor.putInt(USER_ID, user.getId());
            editor.putString(USERNAME, user.getUsername());
            editor.putString(PASSWORD, user.getPassword());
            editor.putString(DATETIME, user.getDateTime());
            editor.commit();
    }

    public User getLoggedInUser() {
        User user = new User(
            localStorage.getInt(USER_ID, -1),
            localStorage.getString(USERNAME,""),
            localStorage.getString(PASSWORD,""),
            localStorage.getString(DATETIME,"")
        );
        return user;
    }

    public void setUserLogged(boolean logged){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putBoolean(USERLOGGEDIN,logged);
        editor.commit();
    }

    public boolean isUserLogged(){
        if(localStorage.getBoolean(USERLOGGEDIN,false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void setUserBusiness(boolean businessAdded){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putBoolean(USERBUSINESSADDED,businessAdded);
        editor.commit();
    }

    public boolean isUserBusinessAdded(){
        if(localStorage.getBoolean(USERBUSINESSADDED,false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.clear();
        editor.commit();
    }

}
