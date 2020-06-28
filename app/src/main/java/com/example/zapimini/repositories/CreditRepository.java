package com.example.zapimini.repositories;

import android.widget.ProgressBar;

import com.example.zapimini.data.Credit;

import java.util.List;

public interface CreditRepository {

    public interface OnFinishedListerner{
        void onFinished(List<Credit> creditlist);
        void onFinished(Object response);
        void onFailuire(Throwable t);
    }

    public void createCredit(Credit credit, ProgressBar progressBar,
                             OnFinishedListerner onFinishedListerner);

    public void getCreditByUserId(int userId, ProgressBar progressBar,
                             OnFinishedListerner onFinishedListerner);

    public void getCreditByUserIdByDate(int userId, String date, ProgressBar progressBar,
                                  OnFinishedListerner onFinishedListerner);

    public void updateCredit(Credit credit, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void deleteCredit(Credit credit, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);
}
