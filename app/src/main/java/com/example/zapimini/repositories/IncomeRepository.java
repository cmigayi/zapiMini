package com.example.zapimini.repositories;

import com.example.zapimini.data.Income;

import java.util.List;

public interface IncomeRepository {

    public interface OnFinishedListerner{
        void onFinished(List<Income> incomelist);
        void onFinished(Object object);
        void onFailuire(Throwable t);
    }

    public void createIncome(Income income, OnFinishedListerner onFinishedListerner);

    public void getIncome(OnFinishedListerner onFinishedListerner);

    public void getIncomeByUserId(int userId, OnFinishedListerner onFinishedListerner);

    public void getIncomeByUserIdByDate(int userId, String date, OnFinishedListerner onFinishedListerner);

    public void updateIncome(Income income, OnFinishedListerner onFinishedListerner);

    public void deleteIncome(Income income, OnFinishedListerner onFinishedListerner);
}
