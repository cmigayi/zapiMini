package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.CreditDaoDatabase;
import com.example.zapimini.data.Credit;
import com.example.zapimini.repositories.CreditRepository;

import java.util.List;

public class CreditLocalDb implements CreditRepository {
    final static String mCreditLocalDb = "CreditLocalDb";
    CreditDaoDatabase creditDaoDatabase;

    public CreditLocalDb(Context context) {
        creditDaoDatabase = CreditDaoDatabase.getInstance(context);
    }

    @Override
    public void createCredit(Credit credit, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Long status =  creditDaoDatabase.creditDao().insertCredit(credit);
                    if(status > 0){
                        List<Credit> creditList = creditDaoDatabase.creditDao().getLastInsertedCredit();
                        Log.d(mCreditLocalDb, "Success01: " + creditList.get(0).getId());
                        onFinishedListerner.onFinished(creditList);
                    }
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void getCreditByUserId(int userId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Credit> creditList = creditDaoDatabase.creditDao().getCreditsByUserId(userId);
                    onFinishedListerner.onFinished(creditList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getCreditByUserIdByDate(int userId, String date, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String dateTo = date +" 23:59:59";
                    List<Credit> expenseList = creditDaoDatabase.creditDao()
                            .getCreditsByUserIdByDate(userId, date, dateTo);
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
    public void updateCredit(Credit credit, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    creditDaoDatabase.creditDao().updateCredit(credit);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void deleteCredit(Credit credit, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    creditDaoDatabase.creditDao().deleteCredit(credit);
                    onFinishedListerner.onFinished("success");
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }
}
