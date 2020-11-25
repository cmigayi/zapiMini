package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Credit;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface CreditItemPaidConfirmationActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void onCreditPaidConfirmed(Credit credit);
    public void displayError(String message);
}
