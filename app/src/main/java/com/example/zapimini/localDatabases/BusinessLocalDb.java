package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.BusinessDaoDatabase;
import com.example.zapimini.data.Business;
import com.example.zapimini.data.User;
import com.example.zapimini.repositories.BusinessRepository;

import java.util.List;

public class BusinessLocalDb implements BusinessRepository {
    final static String mBusinessLocalDb = "BusinessLocalDb";
    BusinessDaoDatabase businessDaoDatabase;

    public BusinessLocalDb(Context context) {
        businessDaoDatabase = BusinessDaoDatabase.getInstance(context);
    }

    @Override
    public void createBusiness(Business business, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Long status = businessDaoDatabase.businessDao().insertBusiness(business);
                    if(status > 0){
                        List<Business> businessist = businessDaoDatabase.businessDao().getLastInsertedBusiness();
                        Log.d(mBusinessLocalDb, "Success01: " + businessist.get(0).getId());
                        onFinishedListerner.onFinished(businessist);
                    }
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getBusiness(int businessId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Business> businessList = businessDaoDatabase.businessDao().getBusiness();
                    onFinishedListerner.onFinished(businessList);
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
    public void getBusinessByUserId(int userId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Business> businessList = businessDaoDatabase.businessDao().getBusinessByUserId(userId);
                    onFinishedListerner.onFinished(businessList);
                }catch(Exception e){
                    onFinishedListerner.onFailuire(e);
                }
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
