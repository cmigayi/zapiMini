package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.databinding.ActivityCashUpItemReportConfirmationBinding;
import com.example.zapimini.databinding.ActivityExpenseItemReportConfirmationBinding;

public class CashUpItemReportConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCashUpItemReportConfirmationBinding activityCashUpItemReportConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCashUpItemReportConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_cash_up_item_report_confirmation);
        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityCashUpItemReportConfirmationBinding.successTv.setText(success);
        activityCashUpItemReportConfirmationBinding.transactionTv.setText(transaction);
        activityCashUpItemReportConfirmationBinding.backToMenu.setOnClickListener(this);
        activityCashUpItemReportConfirmationBinding.cashUpReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.cash_up_report:
                intent = new Intent(CashUpItemReportConfirmationActivity.this,
                        CashUpsReportActivity.class);
                startActivity(intent);
                finish();
                CashUpsReportActivity.aCashUpsReportActivity.finish();
                break;
            case R.id.back_to_menu:
                intent = new Intent(CashUpItemReportConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                CashUpsReportActivity.aCashUpsReportActivity.finish();
                break;
        }
    }
}