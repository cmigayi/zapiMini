package com.example.zapimini.repositories;

import android.widget.ProgressBar;

import com.example.zapimini.data.Expense;

import java.util.List;

public interface ExpenseRepository {

    public interface OnFinishedListerner{
        void onFinished(List<Expense> expenselist);
        void onFinished(Object response);
        void onFailuire(Throwable t);
    }

    public void createExpense(Expense expense, OnFinishedListerner onFinishedListerner);

    public void getExpensesByUserId(int userId, OnFinishedListerner onFinishedListerner);

    public void getExpensesByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner);

    public void updateExpense(Expense expense, OnFinishedListerner onFinishedListerner);

    public void deleteExpense(Expense expense, OnFinishedListerner onFinishedListerner);

}
