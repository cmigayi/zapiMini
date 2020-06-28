package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Credit;

public interface CreditItemReportActivityView {
    public ProgressBar getProgressbar();
    public void loadCreditToFields(Credit credit);
    public void updatedCredit(Credit credit);
    public void deletedCredit(Credit credit);
    public void displayError(String message);
}
