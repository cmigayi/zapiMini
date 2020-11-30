package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Credit;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.views.CreditReportActivityView;

import java.util.List;

public class PayableCreditReportActivityPresenter {
    final static String mCreditReportActivityPresenter = "CreditReportActivity";

    CreditRepository repository;
    CreditReportActivityView view;

    public PayableCreditReportActivityPresenter(CreditRepository repository, CreditReportActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void getPayableCreditByUserIdByTypeByDate(int userId, String date){
        repository.getCreditByUserIdByTypeByDate(userId,
                "I owe this business, supplier or person money (Payable).",
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

    public void getPayableCreditByUserIdByType(int userId){
        repository.getCreditByUserIdByType(userId,
                "I owe this business, supplier or person money (Payable).",
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
}
