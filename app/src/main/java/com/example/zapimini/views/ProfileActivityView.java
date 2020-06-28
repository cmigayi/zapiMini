package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.zapimini.data.User;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface ProfileActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public boolean validateInputs();
    public void populateProfileForm(User user);
    public void redirectOnLogout();
    public void displayError(String message);
}
