package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.Income;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.repositories.ExpenseRepository;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;

import java.util.List;

public class PayableCreditItemPaidConfirmationActivityPresenter {
    final static String mPayableCreditItemPaidConfirmationActivityPresenter = "PayableCreditItemPaidP";
    CreditItemPaidConfirmationActivityView view;
    CreditLocalDb repository;
    IncomeLocalDb incomeRepository;
    ExpenseLocalDb expenseRepository;

    public PayableCreditItemPaidConfirmationActivityPresenter(
            CreditLocalDb repository,
            ExpenseLocalDb expenseRepository,
            IncomeLocalDb incomeRepository,
            CreditItemPaidConfirmationActivityView view) {
        this.view = view;
        this.repository = repository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    public void clearPayable(Credit credit, Expense expense, Income income){
        // 1. Add to expense
        // 2. Subtract from income
        // 3. update credit to paid
        repository.updateCredit(credit, new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {

            }

            @Override
            public void onFinished(Object response) {
                createExpense(credit, expense, income);
            }

            @Override
            public void onFailuire(Throwable t) {

            }
        });
    }

    private void createExpense(Credit credit, Expense expense, Income income){
        expenseRepository.createExpense(expense, new ExpenseRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Expense> expenselist) {
                getUserIncome(income, expense, credit);
//                AppExecutors.getInstance().mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Response: "+expenselist);
//                            view.onCreditPaidConfirmed(credit);
//                        }catch(Exception e){
//                            Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
//                        }
//                    }
//                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {

            }
        });
    }

    private void getUserIncome(Income incomeFromUser, Expense expense, Credit credit){
        String date = new DateTimeUtils().removeTimeInDateTime(incomeFromUser.getDateTime());
        incomeRepository.getIncomeByUserIdByDate(incomeFromUser.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
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
                    updateIncome(income, expense, credit);
                }else{
                    double netAmount = 0.0 - totalExpense;
                    // create
                    Income income =  new Income(
                            0,
                            expense.getUserId(),
                            expense.getBusinessId(),
                            0.0,
                            expense.getAmount(),
                            netAmount,
                            new DateTimeUtils().getTodayDateTime()
                    );
                    createIncome(income, expense, credit);
                }
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }

    private void updateIncome(Income income, Expense expense, Credit credit){
        incomeRepository.updateIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {

            }

            @Override
            public void onFinished(Object response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mPayableCreditItemPaidConfirmationActivityPresenter,
                                    "Response: "+response);
                            view.onCreditPaidConfirmed(credit);
                        }catch(Exception e){
                            Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }

    public void createIncome(Income income, Expense expense, Credit credit){
        incomeRepository.createIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Response: "+incomelist.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Response: "+incomelist);
                                    view.onCreditPaidConfirmed(credit);
                                }catch(Exception e){
                                    Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
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
                Log.d(mPayableCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }
}
