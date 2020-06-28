package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.repositories.BankTransactionRepository;
import com.example.zapimini.views.AddBankTransactionActivityView;

import java.util.List;

public class AddBankTransactionActivityPresenter {
    private final static String mAddBankTransactionActivityPresenter = "AddBankTransaction";
    AddBankTransactionActivityView view;
    BankTransactionLocalDb repository;

    public AddBankTransactionActivityPresenter(AddBankTransactionActivityView view,
                                               BankTransactionLocalDb repository) {
        this.view = view;
        this.repository = repository;
    }

    public void createBankTransactionInLocalDb(BankTransaction bankTransaction, Context context){
        repository.createBankTransaction(bankTransaction, view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<BankTransaction> bankTransactionList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mAddBankTransactionActivityPresenter,
                                    "Response: "+bankTransactionList);
                            view.createdBankTransaction(bankTransactionList.get(0));
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
                        Log.d(mAddBankTransactionActivityPresenter, "Error: "+t.getMessage());
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }

    public void getPreviousBankTransactionInLocalDb(Context context){
        repository.getPreviousBankTransaction(view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<BankTransaction> bankTransactionList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mAddBankTransactionActivityPresenter,
                                    "Response: "+bankTransactionList);
                            view.previousBankTransaction(bankTransactionList.get(0));
                            view.displayPreviousBankTransaction(bankTransactionList.get(0));
                        }catch(Exception e){
                            Log.d(mAddBankTransactionActivityPresenter,
                                    "Null transaction: "+e.getMessage());
                            view.displayPreviousBankTransaction(null);
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
                        Log.d(mAddBankTransactionActivityPresenter, "Error: "+t.getMessage());
                        view.displayError("Sorry, could not get previous transaction.");
                    }
                });
            }
        });
    }

    public void getCurrentGrossBusinessIncome(){

    }
}
