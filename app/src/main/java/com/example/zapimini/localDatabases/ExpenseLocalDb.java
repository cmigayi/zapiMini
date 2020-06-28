package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.ExpenseDaoDatabase;
import com.example.zapimini.data.Expense;
import com.example.zapimini.repositories.ExpenseRepository;

import java.util.List;

public class ExpenseLocalDb implements ExpenseRepository {
    final static String mExpenseLocalDb = "ExpenseLocalDb";
    ExpenseDaoDatabase expenseDaoDatabase;

    public ExpenseLocalDb(Context context) {
        expenseDaoDatabase = ExpenseDaoDatabase.getInstance(context);
    }

    @Override
    public void createExpense(Expense expense, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Long status =  expenseDaoDatabase.expenseDao().insertExpense(expense);
                    if(status > 0){
                        List<Expense> expenseList = expenseDaoDatabase.expenseDao().getLastInsertedExpense();
                        Log.d(mExpenseLocalDb, "Success01: " + expenseList.get(0).getId());
                        onFinishedListerner.onFinished(expenseList);
                    }
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getExpensesByUserId(int userId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Expense> expenseList = expenseDaoDatabase.expenseDao().getExpenses();
                    onFinishedListerner.onFinished(expenseList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void getExpensesByUserIdByDate(int userId, String date, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<Expense> expenseList = expenseDaoDatabase.expenseDao().getExpensesByDate(date, dateTo);
                    onFinishedListerner.onFinished(expenseList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void updateExpense(Expense expense, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    expenseDaoDatabase.expenseDao().updateExpense(expense);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void deleteExpense(Expense expense, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    expenseDaoDatabase.expenseDao().deleteExpense(expense);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
