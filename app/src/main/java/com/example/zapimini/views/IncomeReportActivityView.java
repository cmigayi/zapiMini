package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Income;
import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public interface IncomeReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void displayIncomelist(String filter, List<Income> incomelist);
    public void reportByFilter(String filter, String date);
    public void displayError(String message);
}
