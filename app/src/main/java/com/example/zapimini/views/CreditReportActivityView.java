package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Credit;
import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public interface CreditReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void displayCreditlist(String filter, List<Credit> creditlist);
    public void reportByFilter(String filter, String date);
    public void displayError(String message);
}
