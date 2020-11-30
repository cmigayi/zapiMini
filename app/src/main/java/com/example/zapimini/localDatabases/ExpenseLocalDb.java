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
    public void createExpense(Expense expense, OnFinishedListerner onFinishedListerner) {
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
            }
        });
    }

    @Override
    public void getExpensesByUserId(int userId, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Expense> expenseList = expenseDaoDatabase.expenseDao().getExpenses();
                    onFinishedListerner.onFinished(expenseList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getExpensesByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner) {
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
            }
        });
    }

    @Override
    public void updateExpense(Expense expense, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    expenseDaoDatabase.expenseDao().updateExpense(expense);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void deleteExpense(Expense expense, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    expenseDaoDatabase.expenseDao().deleteExpense(expense);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }
}
