package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.User;

public interface LoginActivityView {
    public ProgressBar getProgressbar();
    public boolean validateInputs();
    public boolean noAccountUser();
    public void authorizeUser(User user);
    public void displayError(String message);
}
