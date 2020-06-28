package com.example.zapimini.views;

import android.widget.ProgressBar;

import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localStorage.UserLocalStorage;

public interface AddBankTransactionActivityView {
    public ProgressBar getProgressbar();
    public void authUser(UserLocalStorage userLocalStorage);
    public void loadTypelist();
    public void previousBankTransaction(BankTransaction bankTransaction);
    public void displayPreviousBankTransaction(BankTransaction bankTransaction);
    public int getUserBusinessId();
    public void createdBankTransaction(BankTransaction bankTransaction);
    public boolean validateInputs();
    public void displayError(String message);
}
