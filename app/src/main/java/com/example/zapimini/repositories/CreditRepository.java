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

    public void createCredit(Credit credit, OnFinishedListerner onFinishedListerner);

    public void getCreditByUserIdByType(int userId, String type, OnFinishedListerner onFinishedListerner);

    public void getCreditByUserIdByTypeByDate(int userId, String type, String date,
                                              OnFinishedListerner onFinishedListerner);

    public void getCreditByUserIdByTypeByCreditPaidStatus(int userId, String type,
                                                          String paidCreditStatus,
                                              OnFinishedListerner onFinishedListerner);

    public void updateCredit(Credit credit, OnFinishedListerner onFinishedListerner);

    public void deleteCredit(Credit credit, OnFinishedListerner onFinishedListerner);
}
