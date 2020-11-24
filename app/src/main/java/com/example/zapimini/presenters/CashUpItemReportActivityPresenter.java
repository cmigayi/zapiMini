package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Income;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.repositories.CashUpRepository;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.CashUpItemReportActivityView;

import java.util.List;

public class CashUpItemReportActivityPresenter {
    final static String mCashUpItemReportActivityPresenter = "CashUpItemReport";
    CashUpItemReportActivityView view;
    CashUpLocalDb repository;

    public CashUpItemReportActivityPresenter(CashUpLocalDb repository, CashUpItemReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void updateCashUpInLocalDb(CashUp newCashUp, CashUp oldCashUp, Context context){
        repository.updateCashUp(newCashUp, new CashUpRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<CashUp> cashUpList) {

            }

            @Override
            public void onFinished(Object response) {
                Log.d(mCashUpItemReportActivityPresenter, "response: "+response);
                IncomeLocalDb incomeLocalDb = new IncomeLocalDb(context);
                String date = new DateTimeUtils().removeTimeInDateTime(newCashUp.getDateTime());
                Log.d(mCashUpItemReportActivityPresenter, "date: "+date+" ,"+newCashUp.getDateTime());
                incomeLocalDb.getIncomeByUserIdByDate(newCashUp.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Income> incomelist) {
                        try{
                            Log.d(mCashUpItemReportActivityPresenter,"Response: "+incomelist.size());
                            if(incomelist.size() > 0){
                                // update income
                                Income income = incomelist.get(0);
                                double newGrossAmount = newCashUp.getAmount() +
                                        (income.getGrossAmount()-oldCashUp.getAmount());
                                double netAmount = newGrossAmount - income.getTotalExpense();
                                income.setGrossAmount(newGrossAmount);
                                income.setNetAmount(netAmount);
                                income.setDateTime(date);
                                updateIncome(incomeLocalDb, income);

                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.updatedCashUp(newCashUp);
                                    }
                                });
                            }else{
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.displayError("Nothing was posted.");
                                    }
                                });
                            }

                        }catch(Exception e){
                            Log.d(mCashUpItemReportActivityPresenter,"Error: "+e.getMessage());
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.displayError("Sorry, user was not created.");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFinished(Object object) {

                    }

                    @Override
                    public void onFailuire(Throwable t) {
                        Log.d(mCashUpItemReportActivityPresenter, "error: "+t.getMessage());
                        view.displayError("Sorry there was an error. Kindly try again!");
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpItemReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("Sorry there was an error. Kindly try again!");
            }
        });
    }

    public void deleteCashUpInLocalDb(CashUp cashUp, Context context){
        repository.deleteCashUp(cashUp, new CashUpRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<CashUp> cashUpList) {

            }

            @Override
            public void onFinished(Object response) {
                Log.d(mCashUpItemReportActivityPresenter, "response: "+response);
                IncomeLocalDb incomeLocalDb = new IncomeLocalDb(context);
                String date = new DateTimeUtils().removeTimeInDateTime(cashUp.getDateTime());
                Log.d(mCashUpItemReportActivityPresenter, "date: "+date+" ,"+cashUp.getDateTime());
                incomeLocalDb.getIncomeByUserIdByDate(cashUp.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Income> incomelist) {
                        try{
                            if(incomelist.size() > 0){
                                // update income
                                Income income = incomelist.get(0);
                                double newGrossAmount = income.getGrossAmount() - cashUp.getAmount();
                                double netAmount = newGrossAmount - income.getTotalExpense();
                                income.setGrossAmount(newGrossAmount);
                                income.setNetAmount(netAmount);
                                income.setDateTime(date);
                                updateIncome(incomeLocalDb, income);

                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.deletedCashUp(cashUp);
                                    }
                                });
                            }else{
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.displayError("Nothing was posted.");
                                    }
                                });
                            }
                        }catch(Exception e){
                            Log.d(mCashUpItemReportActivityPresenter,"Error: "+e.getMessage());
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.displayError("Sorry, user was not created.");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFinished(Object object) {

                    }

                    @Override
                    public void onFailuire(Throwable t) {
                        Log.d(mCashUpItemReportActivityPresenter, "error: "+t.getMessage());
                        view.displayError("Sorry there was an error. Kindly try again!");
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpItemReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("Sorry there was an error. Kindly try again!");
            }
        });
    }

    private void updateIncome(IncomeLocalDb incomeLocalDb, Income income){
        incomeLocalDb.updateIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {

            }

            @Override
            public void onFinished(Object object) {
                Log.d(mCashUpItemReportActivityPresenter, "Response: "+object);
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCashUpItemReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("");
            }
        });
    }
}
