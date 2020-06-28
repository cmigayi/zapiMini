package com.example.zapimini.presenters;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.Income;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.repositories.ExpenseRepository;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.ExpenseItemReportActivityView;

import java.util.List;

public class ExpenseItemReportActivityPresenter {
    final static String mExpenseItemReportActivityPresenter = "ExpenseItemReport";
    ExpenseItemReportActivityView view;
    ExpenseLocalDb repository;

    public ExpenseItemReportActivityPresenter(ExpenseLocalDb repository, ExpenseItemReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void updateExpenseInLocalDb(Expense newExpense, Expense oldExpense, Context context){
        repository.updateExpense(newExpense, view.getProgressbar(), new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {

            }

            @Override
            public void onFinished(Object response) {
                Log.d(mExpenseItemReportActivityPresenter, "response: "+response);
                IncomeLocalDb incomeLocalDb = new IncomeLocalDb(context);
                String date = new DateTimeUtils().removeTimeInDateTime(newExpense.getDateTime());
                Log.d(mExpenseItemReportActivityPresenter, "date: "+date+" ,"+newExpense.getDateTime());
                incomeLocalDb.getIncomeByUserIdByDate(newExpense.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Income> incomelist) {
                        try{
                            Log.d(mExpenseItemReportActivityPresenter,"Response: "+incomelist.size());
                            if(incomelist.size() > 0){
                                // update income
                                Income income = incomelist.get(0);
                                double newTotalExpense = newExpense.getAmount() +
                                        (income.getTotalExpense()-oldExpense.getAmount());
                                double netAmount = income.getGrossAmount() - newTotalExpense;
                                income.setTotalExpense(newTotalExpense);
                                income.setNetAmount(netAmount);
                                income.setDateTime(date);
                                updateIncome(incomeLocalDb, income);

                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.updatedExpense(newExpense);
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
                            Log.d(mExpenseItemReportActivityPresenter,"Error: "+e.getMessage());
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
                        Log.d(mExpenseItemReportActivityPresenter, "error: "+t.getMessage());
                        view.displayError("Sorry there was an error. Kindly try again!");
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mExpenseItemReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("Sorry there was an error. Kindly try again!");
            }
        });
    }

    public void deleteExpenseInLocalDb(Expense expense, Context context){
        repository.deleteExpense(expense, view.getProgressbar(), new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {

            }

            @Override
            public void onFinished(Object response) {
                Log.d(mExpenseItemReportActivityPresenter, "response: "+response);
                IncomeLocalDb incomeLocalDb = new IncomeLocalDb(context);
                String date = new DateTimeUtils().removeTimeInDateTime(expense.getDateTime());
                Log.d(mExpenseItemReportActivityPresenter, "date: "+date+" ,"+expense.getDateTime());
                incomeLocalDb.getIncomeByUserIdByDate(expense.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Income> incomelist) {
                        try{
                            if(incomelist.size() > 0){
                                // update income
                                Income income = incomelist.get(0);
                                double newTotalExpense = income.getTotalExpense() - expense.getAmount();
                                double netAmount = income.getGrossAmount() - newTotalExpense;
                                income.setTotalExpense(newTotalExpense);
                                income.setNetAmount(netAmount);
                                income.setDateTime(date);
                                updateIncome(incomeLocalDb, income);

                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.updatedExpense(expense);
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
                            Log.d(mExpenseItemReportActivityPresenter,"Error: "+e.getMessage());
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
                        Log.d(mExpenseItemReportActivityPresenter, "error: "+t.getMessage());
                        view.displayError("Sorry there was an error. Kindly try again!");
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mExpenseItemReportActivityPresenter, "error: "+t.getMessage());
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
                Log.d(mExpenseItemReportActivityPresenter, "Response: "+object);
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mExpenseItemReportActivityPresenter, "Error: "+t.getMessage());
                view.displayError("");
            }
        });
    }
}
