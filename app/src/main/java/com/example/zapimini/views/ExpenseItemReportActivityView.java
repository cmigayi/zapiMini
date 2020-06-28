package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.Expense;

public interface ExpenseItemReportActivityView {
    public ProgressBar getProgressbar();
    public void loadExpenseToFields(Expense expense);
    public void updatedExpense(Expense expense);
    public void deletedExpense(Expense expense);
    public void clearTextFields();
    public void showMessageNotification(TextView textView, String message);
    public void displayError(String message);
}
