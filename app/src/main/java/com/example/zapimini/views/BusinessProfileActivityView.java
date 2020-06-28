package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.Business;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface BusinessProfileActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public boolean validateInputs();
    public void populateBusinessForm(Business business);
    public void displayError(String message);
}
