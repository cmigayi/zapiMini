package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityConfirmationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityConfirmationBinding activityConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_confirmation);

        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityConfirmationBinding.successTv.setText(success);
        activityConfirmationBinding.transactionTv.setText(transaction);
        activityConfirmationBinding.addAnother.setOnClickListener(this);
        activityConfirmationBinding.bankReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.add_another:
                intent = new Intent(ConfirmationActivity.this, AddBankTransactionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bank_report:
                intent = new Intent(ConfirmationActivity.this, BankReportActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
