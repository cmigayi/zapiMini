package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.BankTransactionDaoDatabase;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.data.User;
import com.example.zapimini.repositories.BankTransactionRepository;

import java.util.List;

public class BankTransactionLocalDb implements BankTransactionRepository {
    final static String mBankTransactionLocalDb = "BankTransactionLocalDb";
    BankTransactionDaoDatabase bankTransactionDaoDatabase;

    public BankTransactionLocalDb(Context context) {
        bankTransactionDaoDatabase = BankTransactionDaoDatabase.getInstance(context);
    }

    @Override
    public void createBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Long status = bankTransactionDaoDatabase.bankTransactionDao().
                            insertBankTransaction(bankTransaction);
                    if(status > 0){
                        List<BankTransaction> bankTransactionList = bankTransactionDaoDatabase
                                .bankTransactionDao().getPreviousBankTransaction();
                        Log.d(mBankTransactionLocalDb, "Success01: " +
                                bankTransactionList.get(0).getId());
                        onFinishedListerner.onFinished(bankTransactionList);
                    }
                }catch(Exception e){
                    Log.d(mBankTransactionLocalDb, "Error: "+e.getMessage());
                    onFinishedListerner.onFailuire(e);
                }
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void getBankTransactions(ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<BankTransaction> bankTransactionList =
                            bankTransactionDaoDatabase.bankTransactionDao().getBankTransactions();
                    onFinishedListerner.onFinished(bankTransactionList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getBankTransactionsByUserId(int userId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<BankTransaction> bankTransactionList =
                            bankTransactionDaoDatabase.bankTransactionDao().
                                    getBankTransactionByUserId(userId);
                    onFinishedListerner.onFinished(bankTransactionList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getBankTransactionsByDate(String date, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<BankTransaction> bankTransactionList = bankTransactionDaoDatabase.
                            bankTransactionDao().getBankTransactionsByDate(date, dateTo);
                    onFinishedListerner.onFinished(bankTransactionList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getBankTransactionsByUserIdByDate(int userId, String date, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<BankTransaction> bankTransactionList =
                            bankTransactionDaoDatabase.bankTransactionDao().
                                    getBankTransactionsByUserIdByDate(userId, date, dateTo);
                    onFinishedListerner.onFinished(bankTransactionList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getPreviousBankTransaction(ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<BankTransaction> bankTransactionList = bankTransactionDaoDatabase.
                            bankTransactionDao().getPreviousBankTransaction();
                    onFinishedListerner.onFinished(bankTransactionList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void updateBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    bankTransactionDaoDatabase.bankTransactionDao().updateBankTransaction(bankTransaction);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void deleteBankTransaction(BankTransaction bankTransaction, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    bankTransactionDaoDatabase.bankTransactionDao().deleteBankTransaction(bankTransaction);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
