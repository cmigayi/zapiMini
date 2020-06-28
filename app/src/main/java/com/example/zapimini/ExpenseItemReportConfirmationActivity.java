package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityExpenseItemReportConfirmationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ExpenseItemReportConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener {
    ActivityExpenseItemReportConfirmationBinding activityExpenseItemReportConfirmationBinding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityExpenseItemReportConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_expense_item_report_confirmation);
        String success, transaction;

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        activityExpenseItemReportConfirmationBinding.successTv.setText(success);
        activityExpenseItemReportConfirmationBinding.transactionTv.setText(transaction);
        activityExpenseItemReportConfirmationBinding.backToMenu.setOnClickListener(this);
        activityExpenseItemReportConfirmationBinding.expenseReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.expense_report:
                intent = new Intent(ExpenseItemReportConfirmationActivity.this,
                        ExpensesReportActivity.class);
                startActivity(intent);
                finish();
                ExpensesReportActivity.aExpensesReportActivity.finish();
                break;
            case R.id.back_to_menu:
                intent = new Intent(ExpenseItemReportConfirmationActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
                ExpensesReportActivity.aExpensesReportActivity.finish();
                break;
        }
    }
}
