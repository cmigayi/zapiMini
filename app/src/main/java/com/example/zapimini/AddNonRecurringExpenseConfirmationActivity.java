package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityAddNonRecurringExpenseConfirmationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AddNonRecurringExpenseConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddNonRecurringExpenseConfirmationBinding
            activityAddNonRecurringExpenseConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNonRecurringExpenseConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_add_non_recurring_expense_confirmation);
        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityAddNonRecurringExpenseConfirmationBinding.successTv.setText(success);
        activityAddNonRecurringExpenseConfirmationBinding.transactionTv.setText(transaction);
        activityAddNonRecurringExpenseConfirmationBinding.addAnother.setOnClickListener(this);
        activityAddNonRecurringExpenseConfirmationBinding.bankReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.add_another:
                intent = new Intent(AddNonRecurringExpenseConfirmationActivity.this,
                        AddNonRecurringExpenseActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bank_report:
                intent = new Intent(AddNonRecurringExpenseConfirmationActivity.this,
                        ExpensesReportActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
