package com.example.zapimini.repositories;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Income;

import java.util.List;

public interface CashUpRepository {
    public interface OnFinishedListerner{
        void onFinished(List<CashUp> cashUpList);
        void onFinished(Object object);
        void onFailuire(Throwable t);
    }

    public void createCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner);

    public void getCashUp(OnFinishedListerner onFinishedListerner);

    public void getCashUpsByUserId(int userId, OnFinishedListerner onFinishedListerner);

    public void getCashUpsByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner);

    public void getCashUpsByUserIdByPaymentMode(int userId, String paymentMode,
                                                OnFinishedListerner onFinishedListerner);

    public void getCashUpsByUserIdByDateByPaymentMode(int userId, String date, String paymentMode,
                                                      OnFinishedListerner onFinishedListerner);

    public void updateCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner);

    public void deleteCashUp(CashUp cashUp, OnFinishedListerner onFinishedListerner);
}
