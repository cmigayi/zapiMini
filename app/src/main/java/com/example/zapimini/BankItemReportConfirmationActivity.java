package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityBankItemReportConfirmationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class BankItemReportConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityBankItemReportConfirmationBinding activityBankItemReportConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBankItemReportConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_bank_item_report_confirmation);

        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityBankItemReportConfirmationBinding.successTv.setText(success);
        activityBankItemReportConfirmationBinding.transactionTv.setText(transaction);
        activityBankItemReportConfirmationBinding.backToMenu.setOnClickListener(this);
        activityBankItemReportConfirmationBinding.bankReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.bank_report:
                intent = new Intent(BankItemReportConfirmationActivity.this,
                        BankReportActivity.class);
                startActivity(intent);
                finish();
                BankReportActivity.aBankReportActivity.finish();
                break;
            case R.id.back_to_menu:
                intent = new Intent(BankItemReportConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                BankReportActivity.aBankReportActivity.finish();
                break;
        }
    }
}
