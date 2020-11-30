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
import com.example.zapimini.repositories.CashUpRepository;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.repositories.ExpenseRepository;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;

import java.util.List;

public class CreditItemPaidConfirmationActivityPresenter {
    final static String mCreditItemPaidConfirmationActivityPresenter= "CreditPaidConfirmationP";
    CreditItemPaidConfirmationActivityView view;
    CreditLocalDb repository;
    CashUpLocalDb cashUpRepository;
    IncomeLocalDb incomeRepository;
    ExpenseLocalDb expenseRepository;

    public CreditItemPaidConfirmationActivityPresenter(
            CreditLocalDb repository,
            CashUpLocalDb cashUpRepository,
            IncomeLocalDb incomeRepository,
            CreditItemPaidConfirmationActivityView view) {
        this.view = view;
        this.repository = repository;
        this.cashUpRepository = cashUpRepository;
        this.incomeRepository = incomeRepository;
    }

    public CreditItemPaidConfirmationActivityPresenter(
            CreditLocalDb repository,
            ExpenseLocalDb expenseRepository,
            IncomeLocalDb incomeRepository,
            CreditItemPaidConfirmationActivityView view) {
        this.view = view;
        this.repository = repository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public void clearReceivable(Credit credit, CashUp cashUp, Income income){
        // 1. Add to cashup, should indicate "settled credit" in cashup report
        // 2. Add to income
        // 3. update credit to paid
        repository.updateCredit(credit, new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {

            }

            @Override
            public void onFinished(Object response) {
                createCashUp(cashUp, income, credit);
            }

            @Override
            public void onFailuire(Throwable t) {

            }
        });
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
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Response: "+response);

                        }catch(Exception e){
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
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
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Response: "+expenselist);

                        }catch(Exception e){
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {

            }
        });
    }

    private void createCashUp(CashUp cashUp, Income income, Credit credit) {
        cashUpRepository.createCashUp(cashUp, new CashUpRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<CashUp> cashUpList) {
                getUserIncome(income, cashUp, credit);
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {

            }
        });
    }

    private void getUserIncome(Income incomeFromUser, CashUp cashUp, Credit credit){
        String date = new DateTimeUtils().removeTimeInDateTime(incomeFromUser.getDateTime());
        incomeRepository.getIncomeByUserIdByDate(incomeFromUser.getUserId(), date, new IncomeRepository.OnFinishedListerner() {
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
                    updateIncome(incomeFromDb, cashUp, credit);
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
                    createIncome(income, cashUp, credit);
                }
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }

    private void updateIncome(Income income, CashUp cashUp, Credit credit){
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
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Response: "+response);
                            view.onCreditPaidConfirmed(credit);
                        }catch(Exception e){
                            Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }

    public void createIncome(Income income, CashUp cashUp, Credit credit){
        incomeRepository.createIncome(income, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                Log.d(mCreditItemPaidConfirmationActivityPresenter, "Response: "+incomelist.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Log.d(mCreditItemPaidConfirmationActivityPresenter, "Response: "+incomelist);
                                    view.onCreditPaidConfirmed(credit);
                                }catch(Exception e){
                                    Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+e.getMessage());
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
                Log.d(mCreditItemPaidConfirmationActivityPresenter, "Error: "+t.getMessage());
            }
        });
    }
}
