package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.IncomeDaoDatabase;
import com.example.zapimini.data.Income;
import com.example.zapimini.repositories.IncomeRepository;

import java.util.List;

public class IncomeLocalDb implements IncomeRepository {
    final static String mIncomeLocalDb = "IncomeLocalDb";
    IncomeDaoDatabase incomeDaoDatabase;

    public IncomeLocalDb(Context context) {
        incomeDaoDatabase = IncomeDaoDatabase.getInstance(context);
    }

    @Override
    public void createIncome(Income income, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Long status = incomeDaoDatabase.incomeDao().insertIncome(income);
                    if(status > 0){
                        List<Income> incomeList = incomeDaoDatabase.incomeDao().getLastInsertedIncome();
                        Log.d(mIncomeLocalDb, "Success01: " + incomeList .get(0).getId());
                        onFinishedListerner.onFinished(incomeList);
                    }
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getIncome(OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Income> incomeList = incomeDaoDatabase.incomeDao().getIncomes();
                    onFinishedListerner.onFinished(incomeList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getIncomeByUserId(int userId, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Income> incomeList = incomeDaoDatabase.incomeDao()
                            .getIncomesByUserId(userId);
                    onFinishedListerner.onFinished(incomeList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getIncomeByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<Income> incomeList = incomeDaoDatabase.incomeDao()
                            .getIncomesByUserIdByDate(userId, date, dateTo);
                    onFinishedListerner.onFinished(incomeList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void updateIncome(Income income, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    incomeDaoDatabase.incomeDao().updateIncome(income);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void deleteIncome(Income income, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    incomeDaoDatabase.incomeDao().deleteIncome(income);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }
}
