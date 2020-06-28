package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.databinding.ActivityBankItemReportBinding;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.BankItemReportActivityPresenter;
import com.example.zapimini.views.BankItemReportActivityView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class BankItemReportActivity extends AppCompatActivity
        implements View.OnClickListener, BankItemReportActivityView {

    final static String mBankItemReportActivity = "BankItemReportActivity";
    ActivityBankItemReportBinding activityBankItemReportBinding;

    BankItemReportActivityPresenter presenter;

    BankTransaction importedBankTransaction;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBankItemReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_bank_item_report);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Modify bank transaction");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        BankTransactionLocalDb bankTransactionLocalDb = new BankTransactionLocalDb(this);
        presenter = new BankItemReportActivityPresenter(bankTransactionLocalDb, this);

        List<String> bankTransactioDataList =
                (List<String>) getIntent().getSerializableExtra("bankTransactionDataList");
        Log.d(mBankItemReportActivity, ""+bankTransactioDataList);

        importedBankTransaction = new BankTransaction(
                Integer.parseInt(bankTransactioDataList.get(0)),
                Integer.parseInt(bankTransactioDataList.get(1)),
                Integer.parseInt(bankTransactioDataList.get(2)),
                bankTransactioDataList.get(3),
                Double.parseDouble(bankTransactioDataList.get(4)),
                Double.parseDouble(bankTransactioDataList.get(5)),
                bankTransactioDataList.get(6)
        );
        loadBankTransactionToFields(importedBankTransaction);

        activityBankItemReportBinding.updateBtn.setOnClickListener(this);
        activityBankItemReportBinding.deleteBtn.setOnClickListener(this);
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
        return activityBankItemReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {

    }

    @Override
    public void loadBankTransactionToFields(BankTransaction bankTransaction) {
        activityBankItemReportBinding.pageTitle.setText("Update bank transaction");
        activityBankItemReportBinding.type.setText(bankTransaction.getType());
        activityBankItemReportBinding.amount.setText(""+bankTransaction.getAmount());
    }

    @Override
    public void updatedBankTransaction(BankTransaction bankTransaction) {
        if(bankTransaction == null){

        }else {
            Log.d(mBankItemReportActivity, "Done");
            intent = new Intent(BankItemReportActivity.this,
                    BankItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have updated this bank transaction successfully!");
            intent.putExtra("message_2", "Type: "+bankTransaction.getType()+
                    ", Amount: ksh."+bankTransaction.getAmount());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void deletedBankTransaction(BankTransaction bankTransaction) {
        if(bankTransaction == null){

        }else {
            Log.d(mBankItemReportActivity, "Done");
            intent = new Intent(BankItemReportActivity.this,
                    BankItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have deleted this bank transaction successfully!");
            intent.putExtra("message_2", "Type: "+bankTransaction.getType()+
                    ", Amount: ksh."+bankTransaction.getAmount());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void displayError(String message) {

    }

    @Override
    public void onClick(View v) {
        activityBankItemReportBinding.contraint1.setVisibility(View.GONE);
        BankTransaction bankTransaction = new BankTransaction(
                importedBankTransaction.getId(),
                importedBankTransaction.getBusinessId(),
                importedBankTransaction.getUserId(),
                activityBankItemReportBinding.type.getText().toString(),
                Double.parseDouble(activityBankItemReportBinding.amount.getText().toString()),
                importedBankTransaction.getBalance(),
                importedBankTransaction.getDateTime()
        );

        switch(v.getId()){
            case R.id.update_btn:
                presenter.updateBankTransactionInLocalDb(bankTransaction, getApplicationContext());
                break;
            case R.id.delete_btn:
                presenter.deleteBankTransactionInLocalDb(bankTransaction, getApplicationContext());
                break;
        }
    }
}
