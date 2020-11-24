package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Expense;

public interface CashUpItemReportActivityView {
    public ProgressBar getProgressbar();
    public void loadCashUpToFields(CashUp cashUp);
    public void updatedCashUp(CashUp cashUp);
    public void deletedCashUp(CashUp cashUp);
    public void clearTextFields();
    public void showMessageNotification(TextView textView, String message);
    public void displayError(String message);
}
