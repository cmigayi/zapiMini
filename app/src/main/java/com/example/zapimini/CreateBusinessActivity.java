package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.example.zapimini.commons.CurrencyUtils;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.Business;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCreateAccountBinding;
import com.example.zapimini.databinding.ActivityCreateBusinessBinding;
import com.example.zapimini.localDatabases.BusinessLocalDb;
import com.example.zapimini.localStorage.BusinessLocalStorage;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreateBusinessActivityPresenter;
import com.example.zapimini.views.CreateBusinessActivityView;

public class CreateBusinessActivity extends AppCompatActivity
        implements CreateBusinessActivityView, View.OnClickListener {
    final static String mCreateBusinessActivity = "CreateBusinessActivity";
    ActivityCreateBusinessBinding activityCreateBusinessBinding;

    String businessName, selectedCurrency;
    Intent intent;

    CreateBusinessActivityPresenter presenter;
    UserLocalStorage userLocalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateBusinessBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_create_business);

        userLocalStorage = new UserLocalStorage(this);
        setLoggedInUsername(userLocalStorage.getLoggedInUser());
        loadCurrencies();

        BusinessLocalDb businessLocalDb = new BusinessLocalDb(this);
        presenter = new CreateBusinessActivityPresenter(businessLocalDb, this);

        activityCreateBusinessBinding.createBusinessBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_business_btn:
                Business business = new Business();
                User user = userLocalStorage.getLoggedInUser();
                
                business.setUserId(user.getId());
                business.setName(businessName);
                business.setCurrency(selectedCurrency);
                business.setDateTime(new DateTimeUtils().getTodayDateTime());
                presenter.createBusiness(business);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityCreateBusinessBinding.progressBar;
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;
        if(activityCreateBusinessBinding.business.getText().toString().equals("")){
            displayError("Business or currency is invalid!");
        }else{
            businessName = activityCreateBusinessBinding.business.getText().toString();
            //selectedCurrency = activityCreateBusinessBinding.currency.getText().toString();
            selectedCurrency = "Ksh";
            validated = true;
        }
        return validated;
    }

    @Override
    public void setLoggedInUsername(User user) {
        activityCreateBusinessBinding.username.setText(user.getUsername());
    }

    @Override
    public void loadCurrencies() {
        String [] currencies = new CurrencyUtils().getCurrencies();
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, currencies);
        activityCreateBusinessBinding.currency.setAdapter(currencyAdapter);
    }

    @Override
    public void createdBusiness(Business business) {
        Log.d(mCreateBusinessActivity, "Create business: done");
        try {
            if (userLocalStorage.isUserLogged()) {
                User userFromDb = userLocalStorage.getLoggedInUser();
                Log.d(mCreateBusinessActivity, "User from db: " + userFromDb.getUsername() +
                        " id: " + userFromDb.getId());
                userLocalStorage.setUserBusiness(true);
                BusinessLocalStorage businessLocalStorage = new BusinessLocalStorage(this);
                businessLocalStorage.storeBusiness(business);
                intent = new Intent(CreateBusinessActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }catch(Exception e){
            Log.d(mCreateBusinessActivity, "Error: "+e.getMessage());
            displayError("Sorry, an error occured.");
        }
    }

    @Override
    public void displayError(String message) {
        activityCreateBusinessBinding.progressBar.setVisibility(View.GONE);
        activityCreateBusinessBinding.errorTv.setText(message);
        activityCreateBusinessBinding.errorTv.setVisibility(View.VISIBLE);
    }
}
