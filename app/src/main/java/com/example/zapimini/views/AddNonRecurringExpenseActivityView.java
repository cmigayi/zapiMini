package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.Expense;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface AddNonRecurringExpenseActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public boolean validateInputs();
    public void createdExpense(Expense expense);
    public void displayError(String message);
}
