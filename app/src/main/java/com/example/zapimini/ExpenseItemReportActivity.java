package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.Expense;
import com.example.zapimini.databinding.ActivityExpenseItemReportBinding;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.presenters.ExpenseItemReportActivityPresenter;
import com.example.zapimini.views.ExpenseItemReportActivityView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class ExpenseItemReportActivity extends AppCompatActivity
        implements View.OnClickListener, ExpenseItemReportActivityView {

    final static String mExpenseItemReportActivity = "ExpenseItemReport";
    ActivityExpenseItemReportBinding activityExpenseItemReportBinding;

    ExpenseItemReportActivityPresenter presenter;

    Expense importedExpense;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityExpenseItemReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_expense_item_report);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Modify Expense");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ExpenseLocalDb expenseLocalDb = new ExpenseLocalDb(this);
        presenter = new ExpenseItemReportActivityPresenter(expenseLocalDb,this);

        List<String> expenseDataList = (List<String>) getIntent().getSerializableExtra("expenseDataList");
        Log.d(mExpenseItemReportActivity, ""+expenseDataList);

        importedExpense = new Expense(
                Integer.parseInt(expenseDataList.get(0)),
                Integer.parseInt(expenseDataList.get(1)),
                Integer.parseInt(expenseDataList.get(2)),
                expenseDataList.get(3),
                Double.parseDouble(expenseDataList.get(4)),
                expenseDataList.get(5)
        );
        loadExpenseToFields(importedExpense);

        activityExpenseItemReportBinding.updateBtn.setOnClickListener(this);
        activityExpenseItemReportBinding.deleteBtn.setOnClickListener(this);
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
    public ProgressBar getProgressbar() {
        return activityExpenseItemReportBinding.progressBar;
    }

    @Override
    public void loadExpenseToFields(Expense expense) {
        activityExpenseItemReportBinding.pageTitle.setText("Update '"+expense.getItem()+"'");
        activityExpenseItemReportBinding.item.setText(expense.getItem());
        activityExpenseItemReportBinding.cost.setText(""+expense.getAmount());
    }

    @Override
    public void updatedExpense(Expense expense) {
        try{
            Log.d(mExpenseItemReportActivity, "Done");
            clearTextFields();
            intent = new Intent(ExpenseItemReportActivity.this,
                    ExpenseItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have updated this route successfully!");
            intent.putExtra("message_2", "Expense: "+expense.getItem()+
                    ", Fare: ksh."+expense.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mExpenseItemReportActivity, "Error: "+e.getMessage());
            displayError("Sorry an error occured.");
        }
    }

    @Override
    public void deletedExpense(Expense expense) {
        if(expense == null){

        }else {
            Log.d(mExpenseItemReportActivity, "Done");
            clearTextFields();
            intent = new Intent(ExpenseItemReportActivity.this,
                    ExpenseItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have deleted this route successfully!");
            intent.putExtra("message_2", "Expense: "+expense.getItem()+
                    ", Fare: ksh."+expense.getAmount());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void clearTextFields() {

    }

    @Override
    public void showMessageNotification(TextView textView, String message) {

    }

    @Override
    public void displayError(String message) {

    }

    @Override
    public void onClick(View v) {
        activityExpenseItemReportBinding.contraint1.setVisibility(View.GONE);
        Expense expense = new Expense(
                importedExpense.getId(),
                importedExpense.getBusinessId(),
                importedExpense.getUserId(),
                activityExpenseItemReportBinding.item.getText().toString(),
                Double.parseDouble(activityExpenseItemReportBinding.cost.getText().toString()),
                importedExpense.getDateTime()
        );

        switch(v.getId()){
            case R.id.update_btn:
                presenter.updateExpenseInLocalDb(expense, importedExpense, this);
                break;
            case R.id.delete_btn:
                presenter.deleteExpenseInLocalDb(expense, this);
                break;
        }
    }
}
