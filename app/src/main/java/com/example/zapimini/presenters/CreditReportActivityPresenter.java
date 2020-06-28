package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Credit;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.views.CreditReportActivityView;

import java.util.List;

public class CreditReportActivityPresenter {
    final static String mCreditReportActivityPresenter = "CreditReportActivity";

    CreditRepository repository;
    CreditReportActivityView view;

    public CreditReportActivityPresenter(CreditRepository repository, CreditReportActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void getCreditByUserIdByDate(int userId, String date){
        repository.getCreditByUserIdByDate(userId, date, view.getProgressbar(), new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mCreditReportActivityPresenter, "creditlist: " + creditlist);
                            view.displayCreditlist(date, creditlist);
                        }catch(Exception e){
                            Log.d(mCreditReportActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCreditReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }

    public void getCreditByUserId(int userId){
        repository.getCreditByUserId(userId, view.getProgressbar(), new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mCreditReportActivityPresenter, "creditlist: "+creditlist);
                            view.displayCreditlist("All", creditlist);
                        }catch(Exception e){
                            Log.d(mCreditReportActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCreditReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }

}
