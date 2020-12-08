package com.example.zapimini.views;

import com.example.zapimini.localStorage.UserLocalStorage;

public interface HomeActivityView {
    public void authUser(UserLocalStorage userLocalStorage);
    public void updateExpenseMade(double amount);
    public void updateOverallNetIncome(double amount);
    public void updateNetAmount(double amount);
    public void updateGrossAmount(double amount);
    public void updatePayableCreditAmount(double amount);
    public void updateReceivableCreditAmount(double amount);
    public void displayError(String message);
}
