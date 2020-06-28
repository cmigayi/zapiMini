package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.repositories.BankTransactionRepository;
import com.example.zapimini.views.BankReportActivityView;

import java.util.List;

public class BankReportActivityPresenter {
    final static String mBankReportActivityPresenter = "BankReportActivity";
    BankReportActivityView view;
    BankTransactionLocalDb repository;

    public BankReportActivityPresenter(BankTransactionLocalDb repository, BankReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void getBankTransactionsByUserId(int userId){
       repository.getBankTransactionsByUserId(userId, view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
           @Override
           public void onFinished(List<BankTransaction> bankTransactionList) {
               AppExecutors.getInstance().mainThread().execute(new Runnable() {
                   @Override
                   public void run() {
                       try{
                           Log.d(mBankReportActivityPresenter,
                                   "Response: "+bankTransactionList);
                           view.displayBankTransactionlist("All", bankTransactionList);
                       }catch(Exception e){
                           view.displayError("There was an error / Nothing was posted");
                       }
                   }
               });
           }

           @Override
           public void onFinished(Object response) {

           }

           @Override
           public void onFailuire(Throwable t) {
               AppExecutors.getInstance().mainThread().execute(new Runnable() {
                   @Override
                   public void run() {
                       Log.d(mBankReportActivityPresenter, "Error: "+t.getMessage());
                       view.displayError("Sorry, user was not created.");
                   }
               });
           }
       });
    }

    public void getBankTransactionsByUserIdByDate(int userId, String date){
        repository.getBankTransactionsByUserIdByDate(userId, date, view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<BankTransaction> bankTransactionList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mBankReportActivityPresenter,
                                    "Response: "+bankTransactionList);
                            view.displayBankTransactionlist(date, bankTransactionList);
                        }catch(Exception e){
                            view.displayError("There was an error / Nothing was posted");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(mBankReportActivityPresenter, "Error: "+t.getMessage());
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }
}
