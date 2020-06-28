package com.example.zapimini.repositories;

import android.widget.ProgressBar;
import com.example.zapimini.data.Business;

import java.util.List;

public interface BusinessRepository {
    public interface OnFinishedListerner{
        void onFinished(List<Business> businessList);
        void onFinished(Object response);
        void onFailuire(Throwable t);
    }

    public void createBusiness(Business business, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void getBusiness(int businessId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void getBusinessByUserId(int userId, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);
}
