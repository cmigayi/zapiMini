package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.Income;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.CashUpActivityView;

import java.util.List;

public class CashUpActivityPresenter {
    final static String mCashUpActivityPresenter = "CashUpActivityP";
    CashUpActivityView view;
    IncomeLocalDb repository;

    public CashUpActivityPresenter(IncomeLocalDb repository, CashUpActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void cashUp(Income incomeFromUser){
        String date = new DateTimeUtils().removeTimeInDateTime(incomeFromUser.getDateTime());
        repository.getIncomeByUserIdByDate(incomeFromUser.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                double grossAmount = 0.0;
                if(incomelist.size() > 0){
                    // Update
                    Income incomeFromDb = incomelist.get(0);
                    grossAmount = incomeFromDb.getGrossAmount() + incomeFromUser.getGrossAmount();
                    double netAmount = grossAmount - incomeFromDb.getTotalExpense();
                    incomeFromDb.setNetAmount(netAmount);
                    incomeFromDb.setGrossAmount(grossAmount);
                    updateIncome(incomeFromDb);
                }else {
                    // create
                    Income income = new Income(
                            0,
                            incomeFromUser.getUserId(),
                            incomeFromUser.getBusinessId(),
                            incomeFromUser.getGrossAmount(),
                            0.0,
                            incomeFromUser.getGrossAmount(),
                            new DateTimeUtils().getTodayDateTime()
                    );
                    createIncome(income);
                }
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }

    private void updateIncome(Income income){
        repository.updateIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mCashUpActivityPresenter, "Response: "+response);
                            view.createdIncome(income);
                        }catch(Exception e){
                            Log.d(mCashUpActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }

    public void createIncome(Income income){
        repository.createIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                Log.d(mCashUpActivityPresenter, "Response: "+incomelist.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Log.d(mCashUpActivityPresenter, "Response: "+incomelist);
                                    view.createdIncome(incomelist.get(0));
                                }catch(Exception e){
                                    Log.d(mCashUpActivityPresenter, "Error: "+e.getMessage());
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }
}
