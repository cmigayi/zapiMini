package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityCashUpConfirmationBinding;
import com.example.zapimini.databinding.ActivityIncomeConfirmationBinding;

public class CashUpConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCashUpConfirmationBinding activityCashUpConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_up_confirmation);

        activityCashUpConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_cash_up_confirmation);
        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityCashUpConfirmationBinding.successTv.setText(success);
        activityCashUpConfirmationBinding.transactionTv.setText(transaction);
        activityCashUpConfirmationBinding.backToMenu.setOnClickListener(this);
        activityCashUpConfirmationBinding.cashUpReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.cash_up_report:
                intent = new Intent(CashUpConfirmationActivity.this,
                        CashUpsReportActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back_to_menu:
                intent = new Intent(CashUpConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}