package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.repositories.CashUpRepository;
import com.example.zapimini.views.CashUpsReportActivityView;

import java.util.List;

public class CashUpsReportActivityPresenter {
    final static String mCashUpReportActivityPresenter = "CashUpReportActivity";

    CashUpsReportActivityView view;
    CashUpLocalDb repository;

    public CashUpsReportActivityPresenter(CashUpLocalDb repository, CashUpsReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void getCashUpsByUserId(int userId){
        repository.getCashUpsByUserId(userId, new CashUpRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<CashUp> cashUpList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mCashUpReportActivityPresenter, "cashuplist: " + cashUpList);
                            view.displayCashUplist("All", cashUpList);
                        }catch(Exception e){
                            Log.d(mCashUpReportActivityPresenter, "Error: "+e.getMessage());

                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }

    public void getCashUpsByUserIdByDate(int userId, String date){
        repository.getCashUpsByUserIdByDate(userId, date, new CashUpRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<CashUp> cashUpList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mCashUpReportActivityPresenter, "cashuplist: "+cashUpList);
                            view.displayCashUplist(date, cashUpList);
                        }catch(Exception e){
                            Log.d(mCashUpReportActivityPresenter, "Error: "+e.getMessage());

                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }
}
