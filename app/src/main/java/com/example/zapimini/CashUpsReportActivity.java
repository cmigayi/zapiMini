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
import com.example.zapimini.adapters.CustomCashUpReportAdapter;
import com.example.zapimini.commons.CashUpCalculation;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.CashUp;
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

    String selectedDate;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_ups_report);

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
        if(getIntent().getStringExtra("date") == null){
            reportByFilter("all", "");
        }else{
            selectedDate = getIntent().getStringExtra("date");
            Log.d(mCashUpsReportActivity, "Date: "+selectedDate);
            reportByFilter("date", selectedDate);
        }
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
    public void reportByFilter(String filter, String date) {
        switch(filter){
            case "date":
                presenter.getCashUpsByUserIdByDate(user.getId(), date);
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
        intent = new Intent(CashUpsReportActivity.this, CashUpItemReportActivity.class);
        Log.d(mCashUpsReportActivity, "cilo: "+cashUpList2.get(position));
        CashUp cashUp = cashUpList2.get(position);
        List<String> cashUpDataList = new ArrayList<>();
        cashUpDataList.add(""+cashUp.getId());
        cashUpDataList.add(""+cashUp.getBusinessId());
        cashUpDataList.add(""+cashUp.getUserId());
        cashUpDataList.add(""+cashUp.getAmount());
        cashUpDataList.add(""+cashUp.getDateTime());
        intent.putExtra("cashUpDataList", (Serializable) cashUpDataList);
        startActivity(intent);
    }
}