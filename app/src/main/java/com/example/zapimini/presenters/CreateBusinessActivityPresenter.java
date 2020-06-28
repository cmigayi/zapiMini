package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Business;
import com.example.zapimini.repositories.BusinessRepository;
import com.example.zapimini.views.CreateBusinessActivityView;

import java.util.List;

public class CreateBusinessActivityPresenter {
    final static String mBusinessAccountActivityPresenter = "BusinessAccountActivity";
    CreateBusinessActivityView view;
    BusinessRepository repository;

    public CreateBusinessActivityPresenter(BusinessRepository repository, CreateBusinessActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void createBusiness(Business business){
        repository.createBusiness(business, view.getProgressbar(), new BusinessRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Business> businessList) {
                Log.d(mBusinessAccountActivityPresenter, "Response: "+businessList.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.createdBusiness(businessList.get(0));
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mBusinessAccountActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, business was not created.");
                    }
                });
            }
        });
    }
}
