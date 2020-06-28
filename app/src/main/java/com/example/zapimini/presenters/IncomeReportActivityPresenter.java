package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Income;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.IncomeReportActivityView;

import java.util.List;

public class IncomeReportActivityPresenter {
    final static String mIncomeReportActivityPresenter = "IncomeReportActivity";

    IncomeReportActivityView view;
    IncomeLocalDb incomeRepository;

    public IncomeReportActivityPresenter(
            IncomeReportActivityView view, IncomeLocalDb incomeRepository) {
        this.view = view;
        this.incomeRepository = incomeRepository;
    }

    public void getUserIncomeFromLocalDb(int userId){
        incomeRepository.getIncomeByUserId(userId, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mIncomeReportActivityPresenter, "Incomelist: "+incomelist);
                            view.displayIncomelist("All", incomelist);
                        }catch(Exception e){
                            Log.d(mIncomeReportActivityPresenter, "Error: "+e.getMessage());
                            view.displayError("There was a problem / no data found.");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mIncomeReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }

    public void getUserIncomeByDateFromLocalDb(int userId, String date){
        incomeRepository.getIncomeByUserIdByDate(userId, date, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mIncomeReportActivityPresenter, "Incomelist: "+incomelist);
                            view.displayIncomelist(date, incomelist);
                        }catch(Exception e){
                            Log.d(mIncomeReportActivityPresenter, "Error: "+e.getMessage());
                            view.displayError("There was a problem / no data found.");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mIncomeReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }
}
