package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Credit;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.views.CreditItemReportActivityView;

import java.util.List;

public class CreditItemReportActivityPresenter {
    final static String mCreditItemActivityPresenter = "CreditItemActivityP";
    CreditRepository repository;
    CreditItemReportActivityView view;

    public CreditItemReportActivityPresenter(
            CreditRepository repository, CreditItemReportActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void updateCredit(Credit credit){
        repository.updateCredit(credit, new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mCreditItemActivityPresenter,
                                    "Response: "+response);
                            view.updatedCredit(credit);
                        }catch(Exception e){
                            view.displayError("There was an error / Nothing was posted");
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(mCreditItemActivityPresenter, "Error: "+t.getMessage());
                        view.displayError("There was a problem retrievining the data.");
                    }
                });
            }
        });
    }

    public void deleteCredit(Credit credit){
        repository.deleteCredit(credit, new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mCreditItemActivityPresenter,
                                    "Response: "+response);
                            view.deletedCredit(credit);
                        }catch(Exception e){
                            view.displayError("There was an error / Nothing was posted");
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(mCreditItemActivityPresenter, "Error: "+t.getMessage());
                        view.displayError("There was a problem retrievining the data.");
                    }
                });
            }
        });
    }

}
