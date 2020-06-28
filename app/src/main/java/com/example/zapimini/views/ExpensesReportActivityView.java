package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Expense;
import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public interface ExpensesReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void displayExpenselist(String filter, List<Expense> expenselist);
    public void reportByFilter(String filter, String date);
    public void displayError(String message);
}
