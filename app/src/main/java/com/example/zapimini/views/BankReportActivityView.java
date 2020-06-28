package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public interface BankReportActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void displayBankTransactionlist(String filter, List<BankTransaction> bankTransactionlist);
    public void reportByFilter(String filter, String date);
    public void displayError(String message);
}
