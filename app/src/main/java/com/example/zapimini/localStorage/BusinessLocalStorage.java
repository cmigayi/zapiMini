package com.example.zapimini.localStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zapimini.data.Business;

public class BusinessLocalStorage {
    final static String mBusinessLocalStorage = "BusinessLocalStorage";
    SharedPreferences localStorage;
    SharedPreferences.Editor editor;

    public static final String BUSINESS_ID = "business_id";
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CURRENCY = "currency";
    public static final String DATETIME= "date_time";

    public BusinessLocalStorage(Context context) {
        localStorage = context.getSharedPreferences("business", 0);
        editor = localStorage.edit();
    }

    public void storeBusiness(Business business) {
        editor.putInt(BUSINESS_ID, business.getId());
        editor.putInt(USER_ID, business.getUserId());
        editor.putString(NAME, business.getName());
        editor.putString(DESCRIPTION, business.getDescription());
        editor.putString(CURRENCY, business.getCurrency());
        editor.putString(DATETIME, business.getDateTime());
        editor.commit();
    }

    public Business getBusiness() {
        Business business = new Business(
                localStorage.getInt(BUSINESS_ID, -1),
                localStorage.getInt(USER_ID, -1),
                localStorage.getString(NAME,""),
                localStorage.getString(DESCRIPTION,""),
                localStorage.getString(CURRENCY,""),
                localStorage.getString(DATETIME,"")
        );
        return business;
    }

    public void clearBusinessData(){
        SharedPreferences.Editor editor = localStorage.edit();
        editor.clear();
        editor.commit();
    }

}

