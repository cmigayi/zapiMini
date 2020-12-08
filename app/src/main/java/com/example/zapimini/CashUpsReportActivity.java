package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zapimini.adapters.CustomCashUpReportAdapter;
import com.example.zapimini.commons.CashUpCalculation;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCashUpsReportBinding;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CashUpsReportActivityPresenter;
import com.example.zapimini.views.CashUpsReportActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CashUpsReportActivity extends AppCompatActivity implements
        View.OnClickListener, CashUpsReportActivityView,
        CustomCashUpReportAdapter.OnCashUpReportListener {

    final static String mCashUpsReportActivity = "CashUpsReportActivity";
    static Activity aCashUpsReportActivity;

    List<CashUp> cashUpList2 = new ArrayList<>();

    ActivityCashUpsReportBinding activityCashUpsReportBinding;
    CashUpsReportActivityPresenter presenter;

    String selectedDate, paymentMode;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCashUpsReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_cash_ups_report);

        aCashUpsReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Cash ups: ");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        CashUpLocalDb cashUpLocalDb = new CashUpLocalDb(this);
        presenter = new CashUpsReportActivityPresenter(cashUpLocalDb,this);

        selectedDate = "";
        filterReport(getIntent().getStringExtra("date"), getIntent().getStringExtra("paymentMode"));
//        if(getIntent().getStringExtra("date") == null){
//            reportByFilter("all", "", null);
//        }
//        if(getIntent().getStringExtra("paymentMode") != null){
//            paymentMode = getIntent().getStringExtra("paymentMode");
//            Log.d(mCashUpsReportActivity, "PaymentMode: "+paymentMode);
//            reportByFilter("paymentMode", "", paymentMode);
//        }
//        if(getIntent().getStringExtra("date") != null){
//            selectedDate = getIntent().getStringExtra("date");
//            Log.d(mCashUpsReportActivity, "Date: "+selectedDate);
//            reportByFilter("date", selectedDate, null);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expenses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.all:
                intent = new Intent(
                        CashUpsReportActivity.this, CashUpsReportActivity.class);
                intent.putExtra("all", "");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        CashUpsReportActivity.this, CashUpsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        CashUpsReportActivity.this, CashUpsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.pick_date:
                DialogFragment newFragment = new DatePickerFragment(ExpensesReportActivity.class);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.more_filter:
                intent = new Intent(
                        CashUpsReportActivity.this, MoreFilterActivity.class);
                intent.putExtra("date", new DateTimeUtils().getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.export:
                ExportDocumentsUtils exportDocumentsUtils = new ExportDocumentsUtils(getApplicationContext());
                exportDocumentsUtils.cashUpCSVFile(cashUpList2);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public ProgressBar getProgressbar() {
        return null;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(CashUpsReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayCashUplist(String filter, List<CashUp> cashUpList) {
        cashUpList2 = cashUpList;
        activityCashUpsReportBinding.pageTitle.setText(filter);

        if(cashUpList.size() > 0){
            String amountFormatted = new MoneyUtils().AddMoneyFormat(
                    new CashUpCalculation().getTotalCashUpAmount(cashUpList));
            toolbar.setTitle("CashUps: ("+cashUpList.size()+ " entries)");
            activityCashUpsReportBinding.amountTv.setText(amountFormatted);
            activityCashUpsReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityCashUpsReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityCashUpsReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomCashUpReportAdapter customCashUpReportAdapter =
                    new CustomCashUpReportAdapter(this, cashUpList, this);

            activityCashUpsReportBinding.recyclerView.setAdapter(customCashUpReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date, String paymentMode) {
        switch(filter){
            case "date":
                presenter.getCashUpsByUserIdByDate(user.getId(), date);
                break;
            case "paymentMode":
                presenter.getCashUpsByUserIdByPaymentMode(user.getId(), paymentMode);
                break;
            case "date + paymentMode":
                presenter.getCashUpsByUserIdByDateByPaymentMode(user.getId(), date, paymentMode);
                break;
            case "all":
                presenter.getCashUpsByUserId(user.getId());
                break;
        }
    }

    @Override
    public void displayError(String message) {
        activityCashUpsReportBinding.recyclerView.setVisibility(View.GONE);
        activityCashUpsReportBinding.progressBar.setVisibility(View.GONE);

        activityCashUpsReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityCashUpsReportBinding.errorTv.setText(message);
    }

    @Override
    public void onCashUpReportClick(int position) {
        Log.d(mCashUpsReportActivity, "cashup clicked: "+position);

        CashUp cashUp = cashUpList2.get(position);

        if(cashUp.getCreditId() != -1){
            Toast.makeText(getApplicationContext(),
                    "All Credit Payment posts can only be accessed from the credit report page!",
                    Toast.LENGTH_LONG).show();
        }else {
            intent = new Intent(CashUpsReportActivity.this, CashUpItemReportActivity.class);
            Log.d(mCashUpsReportActivity, "cilo: " + cashUpList2.get(position));
            List<String> cashUpDataList = new ArrayList<>();
            cashUpDataList.add("" + cashUp.getId());
            cashUpDataList.add("" + cashUp.getBusinessId());
            cashUpDataList.add("" + cashUp.getUserId());
            cashUpDataList.add("" + cashUp.getCreditId());
            cashUpDataList.add("" + cashUp.getAmount());
            cashUpDataList.add("" + cashUp.getDescription());
            cashUpDataList.add("" + cashUp.getPaymentMode());
            cashUpDataList.add("" + cashUp.getDateTime());

            intent.putExtra("cashUpDataList", (Serializable) cashUpDataList);
            startActivity(intent);
        }
    }

    private void filterReport(String date, String paymentMode){
        if(date != null && paymentMode != null){
            reportByFilter("date + paymentMode", date, paymentMode);
        }else{
            if(date == null){
                reportByFilter("all", "", null);
            }
            if(paymentMode != null){
                paymentMode = getIntent().getStringExtra("paymentMode");
                Log.d(mCashUpsReportActivity, "PaymentMode: "+paymentMode);
                reportByFilter("paymentMode", "", paymentMode);
            }
            if(date != null){
                selectedDate = getIntent().getStringExtra("date");
                Log.d(mCashUpsReportActivity, "Date: "+selectedDate);
                reportByFilter("date", selectedDate, null);
            }
        }
    }
}