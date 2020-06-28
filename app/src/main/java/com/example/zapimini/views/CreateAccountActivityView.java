package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.User;

public interface CreateAccountActivityView {
    public ProgressBar getProgressbar();
    public boolean validateInputs();
    public void createdUser(User user);
    public void displayError(String message);
}
