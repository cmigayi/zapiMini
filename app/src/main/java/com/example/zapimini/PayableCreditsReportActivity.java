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
import com.example.zapimini.databinding.ActivityPayableCreditsReportBinding;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreditReportActivityPresenter;
import com.example.zapimini.presenters.PayableCreditReportActivityPresenter;
import com.example.zapimini.views.CreditReportActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayableCreditsReportActivity extends AppCompatActivity
        implements View.OnClickListener, CreditReportActivityView,
        CustomCreditReportAdapter.OnCreditReportListener{
    final static String mCreditReportActivity = "PayableCreditActivity";
    ActivityPayableCreditsReportBinding activityPayableCreditsReportBinding;

    static Activity aPayableCreditsReportActivity;
    List<Credit> creditlist2 = new ArrayList<>();

    PayableCreditReportActivityPresenter presenter;

    String selectedDate;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    Toolbar toolbar;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPayableCreditsReportBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_payable_credits_report);

        aPayableCreditsReportActivity = this;

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Payable Credit: ");
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
        presenter = new PayableCreditReportActivityPresenter(creditLocalDb,this);

        type = getIntent().getStringExtra("type");
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
                        PayableCreditsReportActivity.this, PayableCreditsReportActivity.class);
                intent.putExtra("all", "");
                startActivity(intent);
                finish();
                break;
            case R.id.today:
                intent = new Intent(
                        PayableCreditsReportActivity.this, PayableCreditsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getTodayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.yesterday:
                intent = new Intent(
                        PayableCreditsReportActivity.this, PayableCreditsReportActivity.class);
                intent.putExtra("date", new DateTimeUtils().getYesterdayDate());
                startActivity(intent);
                finish();
                break;
            case R.id.pick_date:
                DialogFragment newFragment = new DatePickerFragment(PayableCreditsReportActivity.class);
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
        return activityPayableCreditsReportBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(PayableCreditsReportActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void displayCreditlist(String filter, List<Credit> creditlist) {
        creditlist2 = creditlist;
        activityPayableCreditsReportBinding.pageTitle.setText(filter);

        if(creditlist.size() > 0){
            String amount = new MoneyUtils().AddMoneyFormat(
                    new CreditCalculation().getTotalCreditAmount(creditlist));
            toolbar.setTitle("Payables: ("+creditlist.size()+ ")");
            activityPayableCreditsReportBinding.amountTv.setText(""+amount);
            activityPayableCreditsReportBinding.recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            activityPayableCreditsReportBinding.recyclerView.setLayoutManager(layoutManager);
            activityPayableCreditsReportBinding.recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            CustomCreditReportAdapter customCreditReportAdapter =
                    new CustomCreditReportAdapter(this, creditlist, this);

            activityPayableCreditsReportBinding.recyclerView.setAdapter(customCreditReportAdapter);
        }else{
            displayError("Nothing was posted");
        }
    }

    @Override
    public void reportByFilter(String filter, String date) {
        switch(filter){
            case "date":
                presenter.getPayableCreditByUserIdByTypeByDate(user.getId(), date);
                break;
            case "all":
                presenter.getPayableCreditByUserIdByType(user.getId());
                break;
        }
    }

    @Override
    public void displayError(String message) {
        activityPayableCreditsReportBinding.errorTv.setVisibility(View.VISIBLE);
        activityPayableCreditsReportBinding.recyclerView.setVisibility(View.GONE);
        activityPayableCreditsReportBinding.progressBar.setVisibility(View.GONE);
        activityPayableCreditsReportBinding.errorTv.setText(message);
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
                creditDataList.add(""+credit.getPaidAmount());
                creditDataList.add(""+credit.getBalance());
                creditDataList.add(credit.getType());
                creditDataList.add(""+credit.getDateTime());

                switch (which){
                    case 0:
                        Log.d(mCreditReportActivity, "credit clicked: "+position);
                        intent = new Intent(PayableCreditsReportActivity.this,
                                CreditItemReportActivity.class);

                        intent.putExtra("creditDataList", (Serializable) creditDataList);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(PayableCreditsReportActivity.this,
                                CreditItemPaidConfirmationActivity.class);
                        intent.putExtra("type", "payable");
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