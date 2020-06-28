package com.example.zapimini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.adapters.CustomBankReportAdapter;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityBankReportBinding;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.BankReportActivityPresenter;
import com.example.zapimini.views.BankReportActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BankReportActivity extends AppCompatActivity implements
        View.OnClickListener, BankReportActivityView,
        CustomBankReportAdapter.OnBankReportListener {
    final static String mBankReportActivity = "BankReportActivity";
    static Activity aBankReportActivity;
    ActivityBankReportBinding activityBankReportBinding;
    BankReportActivityPresenter presenter;
    Intent intent;

    String selectedDate;
    DateTimeUtils dateTimeUtils;

    UserLocalStorage userLocalStorage;
    User user;

    List<BankTransaction> bankTransactionlist2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBankReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_bank_report);

        aBankReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Bank report");
        setSupportActionBar(toolbar);

        dateTimeUtils = new DateTimeUtils();

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        BankTransactionLocalDb bankTransactionLocalDb = new BankTransactionLocalDb(this);
        presenter = new BankReportActivityPresenter(bankTransactionLocalDb, this);

        selectedDate = "";
        if(getIntent().getStringExtra("date") == null){
            reportByFilter("all", "");
        }else{
            selectedDate = getIntent().getStringExtra("date");
            Log.d(mBankReportActivity, "Date: "+selectedDate);
            reportByFilter("date", selectedDate);
        }
        activityBankReportBinding.addBankTransactionBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.all:
                intent = new Intent(
                        BankReportActivity.this, BankReportActivity.class);
                intent.putExtra("all", "");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        BankReportActivity.this, BankReportActivity.class);
                intent.putExtra("date", dateTimeUtils.getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        BankReportActivity.this, BankReportActivity.class);
                intent.putExtra("date", dateTimeUtils.getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.pick_date:
                DialogFragment newFragment = new DatePickerFragment(BankReportActivity.class);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.export:
                ExportDocumentsUtils exportDocumentsUtils =
                        new ExportDocumentsUtils(getApplicationContext());
                exportDocumentsUtils.bankTransactionCSVFile(bankTransactionlist2);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_bank_transaction_btn:
                intent = new Intent(
                        BankReportActivity.this, AddBankTransactionActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityBankReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(BankReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayBankTransactionlist(String filter,
                                           List<BankTransaction> bankTransactionlist) {
        if(bankTransactionlist.size() > 0) {
            bankTransactionlist2 = bankTransactionlist;
            String amount = new MoneyUtils().AddMoneyFormat(bankTransactionlist.get(0).getBalance());
            toolbar.setTitle("Bank: ("+bankTransactionlist.size()+ " entries)");
            activityBankReportBinding.pageTitle.setText(filter+" | Amount: ksh."+amount);
            activityBankReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityBankReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityBankReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomBankReportAdapter customBankReportAdapter =
                    new CustomBankReportAdapter(this, bankTransactionlist, this);

            activityBankReportBinding.recyclerView.setAdapter(customBankReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date) {
        switch(filter){
            case "date":
                presenter.getBankTransactionsByUserIdByDate(user.getId(), date);
                break;
            case "all":
                presenter.getBankTransactionsByUserId(user.getId());
                break;
        }
    }

    @Override
    public void displayError(String message) {
        activityBankReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityBankReportBinding.recyclerView.setVisibility(View.GONE);
        activityBankReportBinding.progressBar.setVisibility(View.GONE);
        activityBankReportBinding.errorTv.setText(message);
    }

    @Override
    public void onBankReportClick(int position) {
        Log.d(mBankReportActivity, "Bank report: "+position);
        intent = new Intent(BankReportActivity.this, BankItemReportActivity.class);
        Log.d(mBankReportActivity, "cilo: "+bankTransactionlist2.get(position));
        BankTransaction bankTransaction = bankTransactionlist2.get(position);
        List<String> bankTransactionDataList = new ArrayList<>();
        bankTransactionDataList.add(""+bankTransaction.getId());
        bankTransactionDataList.add(""+bankTransaction.getBusinessId());
        bankTransactionDataList.add(""+bankTransaction.getUserId());
        bankTransactionDataList.add(bankTransaction.getType());
        bankTransactionDataList.add(""+bankTransaction.getAmount());
        bankTransactionDataList.add(""+bankTransaction.getBalance());
        bankTransactionDataList.add(""+bankTransaction.getDateTime());
        intent.putExtra("bankTransactionDataList", (Serializable) bankTransactionDataList);
        startActivity(intent);
    }
}
