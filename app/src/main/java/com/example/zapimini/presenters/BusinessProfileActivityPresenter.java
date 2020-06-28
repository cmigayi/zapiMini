package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Business;
import com.example.zapimini.localDatabases.BusinessLocalDb;
import com.example.zapimini.repositories.BusinessRepository;
import com.example.zapimini.views.BusinessProfileActivityView;

import java.util.List;

public class BusinessProfileActivityPresenter {
    final static String mBusinessActivityPresenter = "BusinessActivity";
    BusinessLocalDb repository;
    BusinessProfileActivityView view;

    public BusinessProfileActivityPresenter(BusinessLocalDb repository, BusinessProfileActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void getBusinessByUserId(int userId){
        repository.getBusiness(userId, view.getProgressbar(), new BusinessRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Business> businessList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mBusinessActivityPresenter, "Businesslist: "+businessList);
                            view.populateBusinessForm(businessList.get(0));
                        }catch(Exception e){
                            Log.d(mBusinessActivityPresenter, "Error: "+e.getMessage());
                            view.displayError("There was a problem / no data found.");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mBusinessActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }
}
