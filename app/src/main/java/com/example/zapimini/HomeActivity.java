package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.zapimini.commons.AddItemDialog;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityHomeBinding;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.HomeActivityPresenter;
import com.example.zapimini.views.HomeActivityView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements HomeActivityView, View.OnClickListener{
    ActivityHomeBinding activityHomeBinding;
    static Activity aHome;

    Intent intent;
    UserLocalStorage userLocalStorage;
    User user;

    HomeActivityPresenter presenter;
    Thread thread;

    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("ZapiMini");
        setSupportActionBar(toolbar);

        aHome = this;

        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        presenter = new HomeActivityPresenter(new IncomeLocalDb(this),
                new CreditLocalDb(this), this);

        activityHomeBinding.addItemBtn.setOnClickListener(this);
        activityHomeBinding.cashUpBtn.setOnClickListener(this);
        activityHomeBinding.cardview3.setOnClickListener(this);
        activityHomeBinding.cardview4.setOnClickListener(this);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(!thread.isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                presenter.loadIncome(user.getId(), new DateTimeUtils().getTodayDate());
                                presenter.loadOverallNetIncome(user.getId());
                                presenter.loadCredit(
                                        user.getId(),
                                        "I owe this business, supplier or person money (Payable).");
                            }
                        });
                    }
                }catch(InterruptedException e){}
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.cash_up_report:
                intent = new Intent(HomeActivity.this, CashUpsReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case R.id.expense_report:
                intent = new Intent(HomeActivity.this, ExpensesReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case R.id.receivable_credit_report:
                intent = new Intent(HomeActivity.this, CreditsReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case R.id.payable_credit_report:
                intent = new Intent(HomeActivity.this, PayableCreditsReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;
            case R.id.income_report:
                intent = new Intent(HomeActivity.this, IncomeReportActivity.class);
                intent.putExtra("activity", "Reports");
                startActivity(intent);
                break;

//            case R.id.reports:
//                intent = new Intent(HomeActivity.this, ReportsActivity.class);
//                intent.putExtra("activity", "Home");
//                startActivity(intent);
//                break;
//            case R.id.profile:
//                intent = new Intent(HomeActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.business:
//                intent = new Intent(HomeActivity.this, BusinessProfileActivity.class);
//                startActivity(intent);
//                break;
            case R.id.settings:
                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_item_btn:
                String[] addItemOptions = {
                        "Expense",
                        "Credit"
                };
                AddItemDialog addItemDialog = new AddItemDialog(addItemOptions);
                addItemDialog.show(getSupportFragmentManager(), "dialog");
                break;
//            case R.id.cardview3:
//                intent = new Intent(HomeActivity.this, IncomeReportActivity.class);
//                intent.putExtra("date", new DateTimeUtils().getTodayDate());
//                intent.putExtra("activity", "Home");
//                startActivity(intent);
//                break;
//            case R.id.cardview4:
//                intent = new Intent(HomeActivity.this, IncomeReportActivity.class);
//                //intent.putExtra("all", new DateTimeUtils().getTodayDate());
//                intent.putExtra("activity", "Home");
//                startActivity(intent);
//                break;
            case R.id.cash_up_btn:
                intent = new Intent(HomeActivity.this, CashUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void updateExpenseMade(double amount) {
        activityHomeBinding.expenseMade.setText(new MoneyUtils().AddMoneyFormat(amount));
    }

    @Override
    public void updateOverallNetIncome(double amount) {
        if(amount < 1){
            activityHomeBinding.netIncomeMade.setTextColor(Color.parseColor("#ff0000"));
        }
        activityHomeBinding.netIncomeMade.setText(new MoneyUtils().AddMoneyFormat(amount));
    }

    @Override
    public void updateNetAmount(double amount) {
        activityHomeBinding.netAmount.setText(new MoneyUtils().AddMoneyFormat(amount));
    }

    @Override
    public void updateGrossAmount(double amount) {
        activityHomeBinding.grossAmount.setText(new MoneyUtils().AddMoneyFormat(amount));
    }

    @Override
    public void updateCreditAmount(double amount) {
        activityHomeBinding.creditAmount.setText(new MoneyUtils().AddMoneyFormat(amount));
    }

    @Override
    public void displayError(String message) {

    }
}
