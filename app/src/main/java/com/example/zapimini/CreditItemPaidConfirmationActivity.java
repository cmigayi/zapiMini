package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Credit;
import com.example.zapimini.databinding.ActivityCreditItemPaidConfirmationBinding;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;
import com.example.zapimini.views.CreditItemReportActivityView;

import java.util.ArrayList;
import java.util.List;

public class CreditItemPaidConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener, CreditItemPaidConfirmationActivityView {
    final static String mCreditItemPaidConfirmationActivity = "CreditPaidConfirmation";
    ActivityCreditItemPaidConfirmationBinding activityCreditItemPaidConfirmationBinding;
    Intent intent;

    static Activity aCreditItemPaidConfirmationActivity;
    List<String> creditDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditItemPaidConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credit_item_paid_confirmation);

        aCreditItemPaidConfirmationActivity = this;

        creditDataList = (List<String>) getIntent().getSerializableExtra("creditDataList");
        String amount =
                new MoneyUtils().AddMoneyFormat(Double.parseDouble(creditDataList.get(5)));

        activityCreditItemPaidConfirmationBinding.creditInfoTv.setText(
                "Person: "+
                creditDataList.get(3)+", Phone: "+
                creditDataList.get(4)+", Amount: "+
                amount+", Date: "+
                creditDataList.get(7)
        );

        activityCreditItemPaidConfirmationBinding.cancelBtn.setOnClickListener(this);
        activityCreditItemPaidConfirmationBinding.confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.confirm_btn:
                Log.d(mCreditItemPaidConfirmationActivity, creditDataList.get(6));
//                if(creditDataList.get(6).equals("")){
//
//                }
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return null;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(CreditItemPaidConfirmationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreditPaidConfirmed(Credit credit) {

    }

    @Override
    public void displayError(String message) {

    }
}