package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.Credit;
import com.example.zapimini.repositories.CreditRepository;
import com.example.zapimini.views.AddCreditActivityView;

import java.util.List;

public class AddCreditActivityPresenter {
    final static String mAddCreditActivityPresenter = "AddCreditActivityP";
    AddCreditActivityView view;
    CreditRepository repository;

    public AddCreditActivityPresenter(CreditRepository repository, AddCreditActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void createCredit(Credit credit){
        repository.createCredit(credit, new CreditRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Credit> creditlist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mAddCreditActivityPresenter, "Response: "+creditlist.size());
                            view.createdCredit(creditlist.get(0));
                        }catch(Exception e){
                            Log.d(mAddCreditActivityPresenter, "Error: "+e.getMessage());
                            view.displayError("There was an error. Please try again!");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mAddCreditActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("There was an error. Please try again!");
                    }
                });
            }
        });
    }
}
