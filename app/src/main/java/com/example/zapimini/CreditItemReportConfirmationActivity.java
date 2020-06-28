package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityCreditItemReportConfirmationBinding;

public class CreditItemReportConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener{
    final static String mCreditItemReportConfirmationActivity = "CreditItemReport";

    ActivityCreditItemReportConfirmationBinding activityCreditItemReportConfirmationBinding;
    Intent intent;

    String success, transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditItemReportConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credit_item_report_confirmation);

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityCreditItemReportConfirmationBinding.successTv.setText(success);
        activityCreditItemReportConfirmationBinding.transactionTv.setText(transaction);
        activityCreditItemReportConfirmationBinding.backToMenu.setOnClickListener(this);
        activityCreditItemReportConfirmationBinding.creditReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.back_to_menu:
                intent = new Intent(CreditItemReportConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.credit_report:
                intent = new Intent(CreditItemReportConfirmationActivity.this,
                        CreditsReportActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
