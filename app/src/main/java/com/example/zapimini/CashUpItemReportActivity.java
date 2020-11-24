package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Expense;
import com.example.zapimini.databinding.ActivityCashUpItemReportBinding;
import com.example.zapimini.databinding.ActivityExpenseItemReportBinding;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.presenters.CashUpItemReportActivityPresenter;
import com.example.zapimini.presenters.ExpenseItemReportActivityPresenter;
import com.example.zapimini.views.CashUpItemReportActivityView;
import com.example.zapimini.views.ExpenseItemReportActivityView;

import java.util.List;

public class CashUpItemReportActivity extends AppCompatActivity
        implements View.OnClickListener, CashUpItemReportActivityView {

    final static String mCashUpItemReportActivity = "CashUpItemReport";
    ActivityCashUpItemReportBinding activityCashUpItemReportBinding;

    CashUpItemReportActivityPresenter presenter;

    CashUp importedCashUp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCashUpItemReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_cash_up_item_report);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Modify Cash up");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        CashUpLocalDb cashUpLocalDb = new CashUpLocalDb(this);
        presenter = new CashUpItemReportActivityPresenter(cashUpLocalDb,this);

        List<String> cashUpDataList = (List<String>) getIntent().getSerializableExtra("cashUpDataList");
        Log.d(mCashUpItemReportActivity, ""+cashUpDataList);

        importedCashUp = new CashUp(
                Integer.parseInt(cashUpDataList.get(0)),
                Integer.parseInt(cashUpDataList.get(1)),
                Integer.parseInt(cashUpDataList.get(2)),
                Double.parseDouble(cashUpDataList.get(3)),
                cashUpDataList.get(4)
        );
        loadCashUpToFields(importedCashUp);

        activityCashUpItemReportBinding.updateBtn.setOnClickListener(this);
        activityCashUpItemReportBinding.deleteBtn.setOnClickListener(this);
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
    public void onClick(View v) {
        activityCashUpItemReportBinding.contraint1.setVisibility(View.GONE);
        CashUp cashUp = new CashUp(
                importedCashUp.getId(),
                importedCashUp.getBusinessId(),
                importedCashUp.getUserId(),
                Double.parseDouble(activityCashUpItemReportBinding.amount.getText().toString()),
                importedCashUp.getDateTime()
        );

        switch(v.getId()){
            case R.id.update_btn:
                presenter.updateCashUpInLocalDb(cashUp, importedCashUp, this);
                break;
            case R.id.delete_btn:
                presenter.deleteCashUpInLocalDb(cashUp, this);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityCashUpItemReportBinding.progressBar;
    }

    @Override
    public void loadCashUpToFields(CashUp cashup) {
        activityCashUpItemReportBinding.pageTitle.setText("Update '"+cashup.getAmount()+"'");
        activityCashUpItemReportBinding.amount.setText(""+cashup.getAmount());
    }

    @Override
    public void updatedCashUp(CashUp cashUp) {
        try{
            Log.d(mCashUpItemReportActivity, "Done");
            clearTextFields();
            intent = new Intent(CashUpItemReportActivity.this,
                    CashUpItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have updated this cashup successfully!");
            intent.putExtra("message_2", "Cashup: Amount: ksh."+cashUp.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mCashUpItemReportActivity, "Error: "+e.getMessage());
            displayError("Sorry an error occured.");
        }
    }

    @Override
    public void deletedCashUp(CashUp cashUp) {
        if(cashUp == null){

        }else {
            Log.d(mCashUpItemReportActivity, "Done");
            clearTextFields();
            intent = new Intent(CashUpItemReportActivity.this,
                    CashUpItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have deleted this cash up successfully!");
            intent.putExtra("message_2", "Cashup: Amount ksh."+cashUp.getAmount());
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
}