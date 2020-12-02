package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.IncomeCalculation;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Income;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCashUpBinding;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CashUpActivityPresenter;
import com.example.zapimini.views.CashUpActivityView;

public class CashUpActivity extends AppCompatActivity
        implements CashUpActivityView, View.OnClickListener {
    final static String mCashUpActivity = "CashUpActivity";
    ActivityCashUpBinding activityCashUpBinding;
    double expense, amountEntered;
    String selectedPaymentMode;
    UserLocalStorage userLocalStorage;

    Intent intent, broadcastIntent;
    CashUpActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCashUpBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_cash_up);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Cash up");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userLocalStorage = new UserLocalStorage(this);

        IncomeLocalDb incomeLocalDb = new IncomeLocalDb(this);
        presenter = new CashUpActivityPresenter(incomeLocalDb, new CashUpLocalDb(this), this);

        activityCashUpBinding.createBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_btn:
                if(validateInputs()){
                    User user = userLocalStorage.getLoggedInUser();
                    Log.d(mCashUpActivity, "loggedIn user: "+user.getId()+
                            " "+user.getUsername());

                    Income income = new Income();
                    income.setUserId(user.getId());
                    income.setGrossAmount(amountEntered);
                    income.setNetAmount(new IncomeCalculation().getNetIncome(amountEntered, expense));
                    income.setDateTime(new DateTimeUtils().getTodayDateTime());

                    CashUp cashUp = new CashUp();
                    cashUp.setUserId(user.getId());
                    cashUp.setCreditId(-1);
                    cashUp.setAmount(amountEntered);
                    cashUp.setPaymentMode(selectedPaymentMode);
                    cashUp.setDateTime(new DateTimeUtils().getTodayDateTime());
                    presenter.cashUp(income, cashUp);
                }else{
                    displayError("Amount and payment mode must be provided!");
                }
                break;
        }
    }

    @Override
    public void authUser() {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(CashUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        activityCashUpBinding.progressBar.setVisibility(View.VISIBLE);
        activityCashUpBinding.contraint1.setVisibility(View.GONE);
        return activityCashUpBinding.progressBar;
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;

        int radioButtonID = activityCashUpBinding.paymentModeGb.getCheckedRadioButtonId();
        Log.d(mCashUpActivity, "selected: "+radioButtonID);
        if (radioButtonID == -1) {
            //displayError("Payment mode must be provided!");
            validated = false;
        }else{
            RadioButton radioButton = (RadioButton) activityCashUpBinding.paymentModeGb.findViewById(radioButtonID);
            selectedPaymentMode = (String) radioButton.getText();

            if(activityCashUpBinding.amountEntered.getText().toString().equals("") ||
                    selectedPaymentMode.equals("")){
                //displayError("Amount and payment mode must be provided!");
                validated = false;
            }else {
                amountEntered = Double.parseDouble(
                        activityCashUpBinding.amountEntered.getText().toString());
                validated = true;
            }
        }
        return validated;
    }

    @Override
    public void createdCashUp(CashUp cashUp) {
        Log.d(mCashUpActivity, "Create cash: done");

        activityCashUpBinding.contraint1.setVisibility(View.VISIBLE);
        activityCashUpBinding.progressBar.setVisibility(View.GONE);

        try{
            Log.d(mCashUpActivity, "Done");
            String amountFormatted = new MoneyUtils().AddMoneyFormat(cashUp.getAmount());
            intent = new Intent(CashUpActivity.this,
                    CashUpConfirmationActivity.class);
            intent.putExtra("message", "You have added the following to cash up reports successfully!");
            intent.putExtra("message_2", "Amount added: "+amountFormatted);
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mCashUpActivity, "Error: "+e.getMessage());
        }
    }

    @Override
    public void displayError(String message) {
        activityCashUpBinding.progressBar.setVisibility(View.GONE);
        activityCashUpBinding.errorTv.setText(message);
        activityCashUpBinding.errorTv.setVisibility(View.VISIBLE);
    }
}
