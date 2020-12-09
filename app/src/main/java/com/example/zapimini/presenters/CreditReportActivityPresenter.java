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

    public void getReceivableCreditByUserIdByTypeByDate(int userId, String date){
        repository.getCreditByUserIdByTypeByDate(userId,
                "This business or person owes me or my business money (Receivable).",
                date,
                new CreditRepository.OnFinishedListerner() {
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
                            view.displayError("There was a problem. Please try again!");
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
                view.displayError("There was a problem. Please try again!");
            }
        });
    }

    public void getReceivableCreditByUserIdByType(int userId){
        repository.getCreditByUserIdByType(userId,
                "This business or person owes me or my business money (Receivable).",
                new CreditRepository.OnFinishedListerner() {
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
                            view.displayError("There was a problem. Please try again!");
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
                view.displayError("There was a problem. Please try again!");
            }
        });
    }

    public void getReceivableCreditByUserIdByTypeByCreditPaidStatus(int userId, String paidCreditStatus){
        repository.getCreditByUserIdByTypeByCreditPaidStatus(userId,
                "This business or person owes me or my business money (Receivable).",
                paidCreditStatus,
                new CreditRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Credit> creditlist) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(mCreditReportActivityPresenter, "creditlist: "+creditlist);
                                    view.displayCreditlist(paidCreditStatus, creditlist);
                                }catch(Exception e){
                                    Log.d(mCreditReportActivityPresenter, "Error: "+e.getMessage());
                                    view.displayError("There was a problem. Please try again!");
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
                        view.displayError("There was a problem. Please try again!");
                    }
                });
    }

}
