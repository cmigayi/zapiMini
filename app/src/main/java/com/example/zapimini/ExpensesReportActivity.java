package com.example.zapimini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.adapters.CustomExpenseReportAdapter;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExpenseCalculation;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityExpensesReportBinding;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.ExpensesReportActivityPresenter;
import com.example.zapimini.views.ExpensesReportActivityView;
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

public class ExpensesReportActivity extends AppCompatActivity implements
        View.OnClickListener, ExpensesReportActivityView,
        CustomExpenseReportAdapter.OnExpenseReportListener {

    final static String mExpensesReportActivity = "ExpensesReportActivity";
    static Activity aExpensesReportActivity;

    List<Expense> expenselist2 = new ArrayList<>();

    ActivityExpensesReportBinding activityExpensesReportBinding;
    ExpensesReportActivityPresenter presenter;

    String selectedDate;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityExpensesReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_expenses_report);

        aExpensesReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Expenses: ");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        ExpenseLocalDb expenseLocalDb = new ExpenseLocalDb(this);
        presenter = new ExpensesReportActivityPresenter(expenseLocalDb,this);

        selectedDate = "";
        if(getIntent().getStringExtra("date") == null){
            reportByFilter("all", "");
        }else{
            selectedDate = getIntent().getStringExtra("date");
            Log.d(mExpensesReportActivity, "Date: "+selectedDate);
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
                        ExpensesReportActivity.this, ExpensesReportActivity.class);
                intent.putExtra("all", "");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        ExpensesReportActivity.this, ExpensesReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        ExpensesReportActivity.this, ExpensesReportActivity.class);
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
                exportDocumentsUtils.expenseCSVFile(expenselist2);
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
    public void displayExpenselist(String filter, List<Expense> expenselist) {
        expenselist2 = expenselist;
        activityExpensesReportBinding.pageTitle.setText(filter);

        if(expenselist.size() > 0){
            String amount = new MoneyUtils().AddMoneyFormat(
                    new ExpenseCalculation().getTotalExpenseAmount(expenselist));
            toolbar.setTitle("Expenses: ("+expenselist.size()+ " entries)");
            activityExpensesReportBinding.amountTv.setText(amount);
            activityExpensesReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityExpensesReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityExpensesReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomExpenseReportAdapter customExpenseReportAdapter =
                    new CustomExpenseReportAdapter(this, expenselist, this);

            activityExpensesReportBinding.recyclerView.setAdapter(customExpenseReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date) {
        switch(filter){
            case "date":
                presenter.getExpensesByUserIdByDate(user.getId(), date);
                break;
            case "all":
                presenter.getExpensesByUserId(user.getId());
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityExpensesReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(ExpensesReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayError(String message) {
        activityExpensesReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityExpensesReportBinding.recyclerView.setVisibility(View.GONE);
        activityExpensesReportBinding.progressBar.setVisibility(View.GONE);
        activityExpensesReportBinding.errorTv.setText(message);
    }

    @Override
    public void onExpenseReportClick(int position) {
        Log.d(mExpensesReportActivity, "expense clicked: "+position);
        intent = new Intent(ExpensesReportActivity.this, ExpenseItemReportActivity.class);
        Log.d(mExpensesReportActivity, "cilo: "+expenselist2.get(position));
        Expense expense = expenselist2.get(position);
        List<String> expenseDataList = new ArrayList<>();
        expenseDataList.add(""+expense.getId());
        expenseDataList.add(""+expense.getBusinessId());
        expenseDataList.add(""+expense.getUserId());
        expenseDataList.add(expense.getItem());
        expenseDataList.add(""+expense.getAmount());
        expenseDataList.add(""+expense.getDateTime());
        intent.putExtra("expenseDataList", (Serializable) expenseDataList);
        startActivity(intent);
    }
}
