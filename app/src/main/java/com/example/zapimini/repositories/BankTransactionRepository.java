package com.example.zapimini.repositories;

import android.widget.ProgressBar;

import com.example.zapimini.data.BankTransaction;

import java.util.List;

public interface BankTransactionRepository {
    public interface OnFinishedListerner{
        void onFinished(List<BankTransaction> bankTransactionList);
        void onFinished(Object response);
        void onFailuire(Throwable t);
    }

    public void createBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar,
                                      OnFinishedListerner onFinishedListerner);

    public void getBankTransactions(ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void getBankTransactionsByUserId(int userId, ProgressBar progressBar,
                                              OnFinishedListerner onFinishedListerner);

    public void getBankTransactionsByDate(String date, ProgressBar progressBar,
                                          OnFinishedListerner onFinishedListerner);

    public void getBankTransactionsByUserIdByDate(int userId, String date, ProgressBar progressBar,
                                                    OnFinishedListerner onFinishedListerner);

    public void getPreviousBankTransaction(ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void updateBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar,
                                      OnFinishedListerner onFinishedListerner);

    public void deleteBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar,
                                      OnFinishedListerner onFinishedListerner);
}
