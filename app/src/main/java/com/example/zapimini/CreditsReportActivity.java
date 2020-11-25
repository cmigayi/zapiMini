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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.adapters.CustomCreditReportAdapter;
import com.example.zapimini.commons.CreditCalculation;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCreditsReportBinding;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreditReportActivityPresenter;
import com.example.zapimini.views.CreditReportActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreditsReportActivity extends AppCompatActivity
        implements View.OnClickListener, CreditReportActivityView,
        CustomCreditReportAdapter.OnCreditReportListener {
    final static String mCreditReportActivity = "CreditReportActivity";
    ActivityCreditsReportBinding activityCreditReportBinding;

    static Activity aExpensesReportActivity;
    List<Credit> creditlist2 = new ArrayList<>();

    CreditReportActivityPresenter presenter;

    String selectedDate;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credits_report);

        aExpensesReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Credit: ");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        CreditLocalDb creditLocalDb = new CreditLocalDb(this);
        presenter = new CreditReportActivityPresenter(creditLocalDb,this);

        selectedDate = "";
        if(getIntent().getStringExtra("date") == null){
            reportByFilter("all", "");
        }else{
            selectedDate = getIntent().getStringExtra("date");
            Log.d(mCreditReportActivity, "Date: "+selectedDate);
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
        getMenuInflater().inflate(R.menu.menu_credit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.all:
                intent = new Intent(
                        CreditsReportActivity.this, CreditsReportActivity.class);
                intent.putExtra("all", "");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        CreditsReportActivity.this, CreditsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        CreditsReportActivity.this, CreditsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.pick_date:
                DialogFragment newFragment = new DatePickerFragment(CreditsReportActivity.class);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.export:
                ExportDocumentsUtils exportDocumentsUtils = new ExportDocumentsUtils(getApplicationContext());
                exportDocumentsUtils.creditCSVFile(creditlist2);
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
        return activityCreditReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(CreditsReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayCreditlist(String filter, List<Credit> creditlist) {
        creditlist2 = creditlist;
        if(creditlist.size() > 0){
            String amount = new MoneyUtils().AddMoneyFormat(
                    new CreditCalculation().getTotalCreditAmount(creditlist));
            toolbar.setTitle("Credits: ("+creditlist.size()+ " entries)");
            activityCreditReportBinding.pageTitle.setText(filter);
            activityCreditReportBinding.amountTv.setText(""+amount);
            activityCreditReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityCreditReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityCreditReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomCreditReportAdapter customCreditReportAdapter =
                    new CustomCreditReportAdapter(this, creditlist, this);

            activityCreditReportBinding.recyclerView.setAdapter(customCreditReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date) {
        switch(filter){
            case "date":
                presenter.getCreditByUserIdByDate(user.getId(), date);
                break;
            case "all":
                presenter.getCreditByUserId(user.getId());
                break;
        }
    }

    @Override
    public void displayError(String message) {
        activityCreditReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityCreditReportBinding.recyclerView.setVisibility(View.GONE);
        activityCreditReportBinding.progressBar.setVisibility(View.GONE);
        activityCreditReportBinding.errorTv.setText(message);
    }

    @Override
    public void onCreditReportClick(int position) {
        final CharSequence[] items = {"Modify Credit","Credit Paid","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Credit Actions");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(mCreditReportActivity, "cilo: "+creditlist2.get(position));
                Credit credit = creditlist2.get(position);
                List<String> creditDataList = new ArrayList<>();
                creditDataList.add(""+credit.getId());
                creditDataList.add(""+credit.getBusinessId());
                creditDataList.add(""+credit.getUserId());
                creditDataList.add(credit.getName());
                creditDataList.add(credit.getPhone());
                creditDataList.add(""+credit.getAmount());
                creditDataList.add(credit.getType());
                creditDataList.add(""+credit.getDateTime());

                switch (which){
                    case 0:
                        Log.d(mCreditReportActivity, "credit clicked: "+position);
                        intent = new Intent(CreditsReportActivity.this, CreditItemReportActivity.class);

                        intent.putExtra("creditDataList", (Serializable) creditDataList);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(CreditsReportActivity.this,
                                CreditItemPaidConfirmationActivity.class);

                        intent.putExtra("creditDataList", (Serializable) creditDataList);
                        startActivity(intent);
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.show();
    }
}
