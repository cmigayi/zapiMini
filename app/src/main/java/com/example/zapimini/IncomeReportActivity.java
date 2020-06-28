package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.adapters.CustomIncomeReportAdapter;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.IncomeCalculation;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Income;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityIncomeReportBinding;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.IncomeReportActivityPresenter;
import com.example.zapimini.views.IncomeReportActivityView;

import java.util.List;

public class IncomeReportActivity extends AppCompatActivity implements
        View.OnClickListener, IncomeReportActivityView {

    final static String mIncomeReportActivity = "IncomeReportActivity";

    ActivityIncomeReportBinding activityIncomeReportBinding;
    IncomeReportActivityPresenter presenter;

    String selectedDate;
    DateTimeUtils dateTimeUtils;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;

    List<Income> incomelist2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIncomeReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_income_report);

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Income report");
        setSupportActionBar(toolbar);

        dateTimeUtils = new DateTimeUtils();

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);

        IncomeLocalDb incomeLocalDb = new IncomeLocalDb(this);
        presenter = new IncomeReportActivityPresenter(this, incomeLocalDb);

        selectedDate = "";
        try{
            if(!getIntent().getStringExtra("date").isEmpty()){
                selectedDate = getIntent().getStringExtra("date");
                Log.d(mIncomeReportActivity, "Date: "+selectedDate);
                reportByFilter("date", selectedDate);
            }else{
                reportByFilter("all", "");
            }
        }catch(Exception e){
            reportByFilter("all", "");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_income, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.all:
                intent = new Intent(
                        IncomeReportActivity.this, IncomeReportActivity.class);
                intent.putExtra("all", "all");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        IncomeReportActivity.this, IncomeReportActivity.class);
                intent.putExtra("date", dateTimeUtils.getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        IncomeReportActivity.this, IncomeReportActivity.class);
                intent.putExtra("date", dateTimeUtils.getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.pick_date:
                DialogFragment newFragment = new DatePickerFragment(IncomeReportActivity.class);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.export:
                ExportDocumentsUtils exportDocumentsUtils = new ExportDocumentsUtils(this);
                exportDocumentsUtils.incomeCSVFile(incomelist2);
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
    public void displayIncomelist(String filter, List<Income> incomelist) {
        if(incomelist.size() > 0) {
            incomelist2 = incomelist;
            String amount = new MoneyUtils().AddMoneyFormat(
                    new IncomeCalculation().getTotalNetAmount(incomelist));
            toolbar.setTitle("Income: ("+incomelist.size()+ " entries)");
            activityIncomeReportBinding.pageTitle.setText(filter+" | Net total: ksh."+amount);
            activityIncomeReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityIncomeReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityIncomeReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomIncomeReportAdapter customIncomeReportAdapter =
                    new CustomIncomeReportAdapter(this, incomelist);

            activityIncomeReportBinding.recyclerView.setAdapter(customIncomeReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date) {
        user = userLocalStorage.getLoggedInUser();
        switch(filter){
            case "date":
                presenter.getUserIncomeByDateFromLocalDb(user.getId(), date);
                break;
            case "all":
                presenter.getUserIncomeFromLocalDb(user.getId());
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityIncomeReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(IncomeReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayError(String message) {
        activityIncomeReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityIncomeReportBinding.recyclerView.setVisibility(View.GONE);
        activityIncomeReportBinding.progressBar.setVisibility(View.GONE);
        activityIncomeReportBinding.errorTv.setText(message);
    }
}
