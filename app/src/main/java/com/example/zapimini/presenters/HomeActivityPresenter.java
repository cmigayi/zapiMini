package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.commons.IncomeCalculation;
import com.example.zapimini.data.Income;
import com.example.zapimini.repositories.IncomeRepository;
import com.example.zapimini.views.HomeActivityView;

import java.util.List;

public class HomeActivityPresenter {
    final static String mHomeActivityPresenter = "HomeActivityPresenter";
    HomeActivityView view;
    IncomeRepository incomeRepository;

    public HomeActivityPresenter(IncomeRepository incomeRepository, HomeActivityView view) {
        this.view = view;
        this.incomeRepository = incomeRepository;
    }

    public void loadIncome(int userId, String date){
        incomeRepository.getIncomeByUserIdByDate(userId, date, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mHomeActivityPresenter, "Response: "+incomelist.size());
                            Income income = incomelist.get(0);
                            view.updateGrossAmount(income.getGrossAmount());
                            view.updateNetAmount(income.getNetAmount());
                            view.updateExpenseMade(income.getTotalExpense());
                        }catch(Exception e){
                            Log.d(mHomeActivityPresenter, "Error: "+e.getMessage());
                            view.updateGrossAmount(0.0);
                            view.updateNetAmount(0.0);
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mHomeActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }

    public void loadOverallNetIncome(int userId){
        incomeRepository.getIncomeByUserId(userId, new IncomeRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<Income> incomelist) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mHomeActivityPresenter, "Response: "+incomelist.size());
                            view.updateOverallNetIncome(
                                    new IncomeCalculation().getTotalNetAmount(incomelist));
                        }catch(Exception e){
                            Log.d(mHomeActivityPresenter, "Error: "+e.getMessage());
                            view.updateOverallNetIncome(0.0);
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object object) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mHomeActivityPresenter, "error: "+t.getMessage());
                view.displayError("");
            }
        });
    }
}
