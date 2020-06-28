package com.example.zapimini.views;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface BankItemReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void loadBankTransactionToFields(BankTransaction bankTransaction);
    public void updatedBankTransaction(BankTransaction bankTransaction);
    public void deletedBankTransaction(BankTransaction bankTransaction);
    public void displayError(String message);
}
