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
import com.example.zapimini.views.AddNonRecurringExpenseActivityView;

import java.util.List;

public class AddNonRecurringExpenseActivityPresenter {
    final static String mAddNonRecurringActivityPresenter = "AddNonRecurringExpense";
    AddNonRecurringExpenseActivityView view;
    ExpenseLocalDb repository;

    public AddNonRecurringExpenseActivityPresenter(ExpenseLocalDb repository,
                                                   AddNonRecurringExpenseActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void createNonRecurringExpenseInLocalDb(Expense expense, Context context){
        repository.createExpense(expense, new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {
                IncomeLocalDb incomeLocalDb = new IncomeLocalDb(context);
                String date = new DateTimeUtils().removeTimeInDateTime(expense.getDateTime());
                incomeLocalDb.getIncomeByUserIdByDate(expense.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
                    @Override
                    public void onFinished(List<Income> incomelist) {
                        double totalExpense = 0.0;
                        if(incomelist.size() > 0){
                            // Update
                            Income income = incomelist.get(0);
                            totalExpense = income.getTotalExpense() + expense.getAmount();
                            double netAmount = income.getGrossAmount() - totalExpense;
                            income.setNetAmount(netAmount);
                            income.setTotalExpense(totalExpense);
                            updateIncome(incomeLocalDb, income);
                        }else{
                            // create
                            Income income =  new Income(
                                    0,
                                    expense.getUserId(),
                                    expense.getBusinessId(),
                                    0.0,
                                    expense.getAmount(),
                                    expense.getAmount(),
                                    new DateTimeUtils().getTodayDateTime()
                            );
                            createIncome(incomeLocalDb, income);
                        }
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(mAddNonRecurringActivityPresenter, "Response: "+expenselist.size());
                                    view.createdExpense(expenselist.get(0));

                                }catch(Exception e){
                                    Log.d(mAddNonRecurringActivityPresenter, "Error: "+e.getMessage());
                                    view.displayError("Sorry, user was not created.");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFinished(Object object) {

                    }

                    @Override
                    public void onFailuire(Throwable t) {

                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mAddNonRecurringActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }

    private void updateIncome(IncomeLocalDb incomeLocalDb, Income income){
        incomeLocalDb.updateIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {

            }

            @Override
            public void onFinished(Object response) {
                try {
                    Log.d(mAddNonRecurringActivityPresenter, "Response: "+response);
                }catch(Exception e){
                    Log.d(mAddNonRecurringActivityPresenter, "Error: "+e.getMessage());
                }
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mAddNonRecurringActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }

    private void createIncome(IncomeLocalDb incomeLocalDb, Income income){
        incomeLocalDb.createIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                try {
                    Log.d(mAddNonRecurringActivityPresenter, "Response: "+incomelist);
                }catch(Exception e){
                    Log.d(mAddNonRecurringActivityPresenter, "Error: "+e.getMessage());
                }
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mAddNonRecurringActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }
}
