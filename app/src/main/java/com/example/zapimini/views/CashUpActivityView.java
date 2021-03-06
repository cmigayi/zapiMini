package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Income;

public interface CashUpActivityView {
    public void authUser();
    public ProgressBar getProgressbar();
    public boolean validateInputs();
    public void createdCashUp(CashUp cashUp);
    public void displayError(String message);
}
