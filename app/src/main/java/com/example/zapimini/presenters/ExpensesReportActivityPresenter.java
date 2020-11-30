package com.example.zapimini.presenters;

import android.util.Log;
import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Expense;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.repositories.ExpenseRepository;
import com.example.zapimini.views.ExpensesReportActivityView;
import java.util.List;

public class ExpensesReportActivityPresenter {
    final static String mExpenseReportActivityPresenter = "ExpenseReportActivity";

    ExpensesReportActivityView view;
    ExpenseLocalDb repository;

    public ExpensesReportActivityPresenter(ExpenseLocalDb repository, ExpensesReportActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void getExpensesByUserId(int userId){
        repository.getExpensesByUserId(userId, new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mExpenseReportActivityPresenter, "expenselist: " + expenselist);
                            view.displayExpenselist("All", expenselist);
                        }catch(Exception e){
                            Log.d(mExpenseReportActivityPresenter, "Error: "+e.getMessage());
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
                Log.d(mExpenseReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("There was a problem. Please try again!");
            }
        });
    }

    public void getExpensesByUserIdByDate(int userId, String date){
        repository.getExpensesByUserIdByDate(userId, date, new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mExpenseReportActivityPresenter, "expenselist: "+expenselist);
                            view.displayExpenselist(date, expenselist);
                        }catch(Exception e){
                            Log.d(mExpenseReportActivityPresenter, "Error: "+e.getMessage());
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
                Log.d(mExpenseReportActivityPresenter, "error: "+t.getMessage());
                view.displayError("There was a problem. Please try again!");
            }
        });
    }
}
