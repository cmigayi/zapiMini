package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.example.zapimini.data.Credit;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface AddCreditActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public boolean validateInputs();
    public void createdCredit(Credit credit);
    public String selectedCreditOption(RadioGroup radioGroup);
    public void displayError(String message);
}
