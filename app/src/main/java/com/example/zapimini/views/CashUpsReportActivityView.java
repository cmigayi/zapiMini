package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Expense;
import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public interface CashUpsReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void displayCashUplist(String filter, List<CashUp> cashUpList);
    public void reportByFilter(String filter, String date, String paymentMode);
    public void displayError(String message);
}
