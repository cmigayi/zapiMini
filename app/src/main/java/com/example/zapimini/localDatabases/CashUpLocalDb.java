package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.CashUpDaoDatabase;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.repositories.CashUpRepository;

import java.util.List;

public class CashUpLocalDb implements CashUpRepository {

    final static String mCashUpLocalDb = "CashUpLocalDb";
    CashUpDaoDatabase cashUpDaoDatabase;

    public CashUpLocalDb(Context context) {
        cashUpDaoDatabase = CashUpDaoDatabase.getInstance(context);
    }

    @Override
    public void createCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Long status = cashUpDaoDatabase.cashUpDao().insertCashUp(cashUp);
                    if(status > 0){
                        List<CashUp> cashUpList = cashUpDaoDatabase.cashUpDao().getLastInsertedCashUp();
                        Log.d(mCashUpLocalDb, "Success01: " + cashUpList.get(0).getId());
                        onFinishedListerner.onFinished(cashUpList);
                    }
                }catch(Exception e){
                    Log.d(mCashUpLocalDb, "Error: "+e.getMessage());
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getCashUp(OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<CashUp> cashUpList = cashUpDaoDatabase.cashUpDao().getCashUps();
                    onFinishedListerner.onFinished(cashUpList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getCashUpsByUserId(int userId, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<CashUp> cashUpList = cashUpDaoDatabase.cashUpDao()
                            .getCashUpsByUserId(userId);
                    onFinishedListerner.onFinished(cashUpList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getCashUpsByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<CashUp> cashUpList = cashUpDaoDatabase.cashUpDao()
                            .getCashUpsByUserIdByDate(userId, date, dateTo);
                    onFinishedListerner.onFinished(cashUpList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void updateCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    cashUpDaoDatabase.cashUpDao().updateCashUp(cashUp);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void deleteCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    cashUpDaoDatabase.cashUpDao().deleteCashUp(cashUp);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }
}
