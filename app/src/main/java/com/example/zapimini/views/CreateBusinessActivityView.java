package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.Business;
import com.example.zapimini.data.User;

public interface CreateBusinessActivityView {
    public ProgressBar getProgressbar();
    public boolean validateInputs();
    public void setLoggedInUsername(User user);
    public void loadCurrencies();
    public void createdBusiness(Business business);
    public void displayError(String message);
}
