package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

    static Activity aCreditsReportActivity;
    List<Credit> creditlist2 = new ArrayList<>();

    CreditReportActivityPresenter presenter;

    String selectedDate = "";

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credits_report);

        aCreditsReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Receivable Credit: ");
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

        filterReport(
                getIntent().getStringExtra("date"),
                getIntent().getStringExtra("paidcreditstatus"),
                Boolean.parseBoolean(getIntent().getStringExtra("date"))
        );
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
            case R.id.unpaid:
                filterActivityData("none", "Unpaid", false);
                break;
            case R.id.all:
                filterActivityData("none", "none", true);
                break;
            case R.id.today:
                filterActivityData(new DateTimeUtils().getTodayDate(), "none", false);
                break;
            case R.id.yesterday:
                filterActivityData(new DateTimeUtils().getYesterdayDate(), "none",false);
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
        activityCreditReportBinding.pageTitle.setText(filter);

        if(creditlist.size() > 0){
            String amount = new MoneyUtils().AddMoneyFormat(
                    new CreditCalculation().getTotalCreditAmount(creditlist));
            toolbar.setTitle("Receivables: ("+creditlist.size()+ ")");
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
    public void reportByFilter(String filter, String date, String paidCreditStatus) {
        switch(filter){
            case "date":
                presenter.getReceivableCreditByUserIdByTypeByDate(user.getId(), date);
                break;
            case "all":
                presenter.getReceivableCreditByUserIdByType(user.getId());
                break;
            case "paidcreditstatus":
                presenter.getReceivableCreditByUserIdByTypeByCreditPaidStatus(user.getId(), paidCreditStatus);
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
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                creditDataList.add(""+credit.getPaidAmount());
                creditDataList.add(""+credit.getBalance());
                creditDataList.add(credit.getType());
                creditDataList.add(credit.getCreditStatus());
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

    private void filterReport(String date, String paidCreditStatus, boolean all){
        if(date == null || date.equals("none")){
            if(paidCreditStatus == null || paidCreditStatus.equals("none")){
                reportByFilter("all", date, paidCreditStatus);
            }else{
                String selectedPaidCreditStatus = getIntent().getStringExtra("paidcreditstatus");
                Log.d(mCreditReportActivity, "paidCreditStatus: "+selectedPaidCreditStatus);
                reportByFilter("paidcreditstatus", date, selectedPaidCreditStatus);
            }
        }else{
            selectedDate = getIntent().getStringExtra("date");
            Log.d(mCreditReportActivity, "Date: "+selectedDate);
            reportByFilter("date", selectedDate, paidCreditStatus);
        }

//        if(all == true){
//            reportByFilter("all", date, paidCreditStatus);
//        }
//        if((paidCreditStatus.equals("none") || paidCreditStatus == null) &&
//                (date.equals("none") || date == null) &&
//                all == false
//        ){
//            reportByFilter("all", date, paidCreditStatus);
//        }
    }

    private void filterActivityData(String date, String paidCreditStatus, boolean all){
        intent = new Intent(
                CreditsReportActivity.this, CreditsReportActivity.class);
        intent.putExtra("paidcreditstatus", paidCreditStatus);
        intent.putExtra("date", date);
        intent.putExtra("all", all);
        startActivity(intent);
        finish();
    }
}
