package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityIncomeConfirmationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class IncomeConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener {
    ActivityIncomeConfirmationBinding activityIncomeConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIncomeConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_income_confirmation);
        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityIncomeConfirmationBinding.successTv.setText(success);
        activityIncomeConfirmationBinding.transactionTv.setText(transaction);
        activityIncomeConfirmationBinding.backToMenu.setOnClickListener(this);
        activityIncomeConfirmationBinding.expenseReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.expense_report:
                intent = new Intent(IncomeConfirmationActivity.this,
                        IncomeReportActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back_to_menu:
                intent = new Intent(IncomeConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
