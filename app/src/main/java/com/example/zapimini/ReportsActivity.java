package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zapimini.adapters.CustomReportAdapter;
import com.example.zapimini.databinding.ActivityReportsBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportsActivity extends AppCompatActivity implements CustomReportAdapter.OnReportListener {
    final static String mReportsActivity = "com.example.zapimini.ReportsActivity";
    String[] reportList = {
            "Cash ups", "Expenses", "Credits", "Income summary",
//            "Profit and loss","Balance sheet", "Cashflow",
            "Bank report"
    };

    String[] reportDescriptionList = {
            "Cash amount added. CVS export, daily and date-range reports.",
            "Report of every expense made. CVS export, daily and date-range reports.",
            "Report of every credit. CVS export, daily and date-range reports.",
            "Report of money made, total expenses and net amount. CVS export, daily and date-range reports",
//            "Profit and loss","Balance sheet", "Cashflow",
            "Report of money set aside. CVS export, daily and date-range reports. Add new withdrawal or deposit."
    };

    int[] reportIconList = {
            R.drawable.icon_report_sale,
            R.drawable.icon_report_expense,
            R.drawable.icon_report_credit,
            R.drawable.icon_report_income,
            R.drawable.icon_report_bank
    };

    Intent intent;
    ActivityReportsBinding activityReportsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportsBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        displayReportList(reportList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void displayReportList(String[] reportList){
        activityReportsBinding.recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        activityReportsBinding.recyclerView.setLayoutManager(layoutManager);
        activityReportsBinding.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        CustomReportAdapter customReportAdapter =
                new CustomReportAdapter(this, reportList, reportIconList,
                        reportDescriptionList, this);
        activityReportsBinding.recyclerView.setAdapter(customReportAdapter);
    }

    @Override
    public void onReportClick(int position) {
        reportActivities(position);
    }

    private void reportActivities(int position){
        switch(position){
            case 0:
                intent = new Intent(ReportsActivity.this, CashUpsReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(ReportsActivity.this, ExpensesReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case 2:
//                intent = new Intent(ReportsActivity.this, CreditsReportActivity.class);
//                intent.putExtra("activity", "Reports");
//                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Coming soon!",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                intent = new Intent(ReportsActivity.this, IncomeReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(ReportsActivity.this, BankReportActivity.class);
                startActivity(intent);
                break;
        }
    }
}
