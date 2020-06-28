package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.data.Business;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityBusinessProfileBinding;
import com.example.zapimini.localDatabases.BusinessLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.BusinessProfileActivityPresenter;
import com.example.zapimini.views.BusinessProfileActivityView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class BusinessProfileActivity extends AppCompatActivity implements BusinessProfileActivityView {
    final static String mBusinessActivity = "BusinessActivity";

    ActivityBusinessProfileBinding activityBusinessBinding;

    Intent intent;
    BusinessProfileActivityPresenter presenter;

    UserLocalStorage userLocalStorage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBusinessBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_business_profile);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Business profile");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        BusinessLocalDb businessLocalDb = new BusinessLocalDb(this);
        presenter = new BusinessProfileActivityPresenter(businessLocalDb, this);
        presenter.getBusinessByUserId(user.getId());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityBusinessBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(BusinessProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean validateInputs() {
        return false;
    }

    @Override
    public void populateBusinessForm(Business business) {
        activityBusinessBinding.contraint1.setVisibility(View.VISIBLE);
        activityBusinessBinding.setBusiness(business);
    }

    @Override
    public void displayError(String message) {

    }
}
