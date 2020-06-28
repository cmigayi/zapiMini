package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.repositories.BankTransactionRepository;
import com.example.zapimini.views.BankItemReportActivityView;

import java.util.List;

public class BankItemReportActivityPresenter {
    final static String mBankItemReportActivityPresenter = "BankItemReportActivity";

    BankItemReportActivityView view;
    BankTransactionLocalDb repository;

    public BankItemReportActivityPresenter(BankTransactionLocalDb repository, BankItemReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void updateBankTransactionInLocalDb(BankTransaction bankTransaction, Context context){
        repository.updateBankTransaction(bankTransaction, view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<BankTransaction> bankTransactionList) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mBankItemReportActivityPresenter,
                                    "Response: "+response);
                            view.updatedBankTransaction(bankTransaction);
                        }catch(Exception e){
                            view.displayError("There was an error / Nothing was posted");
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mBankItemReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }

    public void deleteBankTransactionInLocalDb(BankTransaction bankTransaction, Context context){
        repository.deleteBankTransaction(bankTransaction, view.getProgressbar(), new BankTransactionRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<BankTransaction> bankTransactionList) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mBankItemReportActivityPresenter,
                                    "Response: "+response);
                            view.updatedBankTransaction(bankTransaction);
                        }catch(Exception e){
                            view.displayError("There was an error / Nothing was posted");
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mBankItemReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }

}
