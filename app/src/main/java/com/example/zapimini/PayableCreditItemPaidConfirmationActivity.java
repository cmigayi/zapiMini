package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.zapimini.commons.CreditCalculation;
import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.commons.ExportDocumentsUtils;
import com.example.zapimini.commons.IncomeCalculation;
import com.example.zapimini.commons.MoneyUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.Income;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCreditItemPaidConfirmationBinding;
import com.example.zapimini.databinding.ActivityPayableCreditItemPaidConfirmationBinding;
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreditItemPaidConfirmationActivityPresenter;
import com.example.zapimini.presenters.PayableCreditItemPaidConfirmationActivityPresenter;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;
import com.example.zapimini.views.CreditItemReportActivityView;

import java.util.ArrayList;
import java.util.List;

public class PayableCreditItemPaidConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener, CreditItemPaidConfirmationActivityView {
    final static String mPayableCreditItemPainConfirmationActivity = "CreditPaidConfirmation";
    ActivityPayableCreditItemPaidConfirmationBinding activityPayableCreditItemPaidConfirmationBinding;
    Intent intent;

    UserLocalStorage userLocalStorage;

    static Activity aPayableCreditItemPainConfirmationActivity;
    List<String> creditDataList = new ArrayList<>();

    double newAmount;

    PayableCreditItemPaidConfirmationActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPayableCreditItemPaidConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_payable_credit_item_paid_confirmation);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Payable Credit Paid");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        aPayableCreditItemPainConfirmationActivity= this;

        creditDataList = (List<String>) getIntent().getSerializableExtra("creditDataList");

        userLocalStorage = new UserLocalStorage(this);

        String amount =
                new MoneyUtils().AddMoneyFormat(Double.parseDouble(creditDataList.get(5)));
        String balance =
                new MoneyUtils().AddMoneyFormat(Double.parseDouble(creditDataList.get(7)));

        activityPayableCreditItemPaidConfirmationBinding.creditInfoTv.setText(
                "Name: "+
                        creditDataList.get(3)+", Phone: "+
                        creditDataList.get(4)+", Amount: "+
                        amount +", Bal: "+
                        balance +", Date: "+
                        creditDataList.get(9)
        );

        activityPayableCreditItemPaidConfirmationBinding.cancelBtn.setOnClickListener(this);
        activityPayableCreditItemPaidConfirmationBinding.creditPaidBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.credit_paid_btn:
                Log.d(mPayableCreditItemPainConfirmationActivity, creditDataList.get(5));

                if(activityPayableCreditItemPaidConfirmationBinding.amountPaidEt
                        .getText().toString().equals("")){
                    displayError("Amount field cannot be empty!");
                }else{
                    newAmount = Double.parseDouble(activityPayableCreditItemPaidConfirmationBinding
                            .amountPaidEt.getText().toString());

                    payable(newAmount);
                }
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return null;
    }

    @Override
    public void authUser() {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(PayableCreditItemPaidConfirmationActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreditPaidConfirmed(Credit credit) {
        Log.d(mPayableCreditItemPainConfirmationActivity, "Create cash: done");

        try{
            Log.d(mPayableCreditItemPainConfirmationActivity, "Done");
            String amountFormatted = new MoneyUtils().AddMoneyFormat(newAmount);
            intent = new Intent(PayableCreditItemPaidConfirmationActivity.this,
                    PayableCreditsReportActivity.class);
//            intent.putExtra("message", "You have added the following to credit reports successfully!");
//            intent.putExtra("message_2", "Credit amount paid: "+amountFormatted);
            Toast.makeText(getApplicationContext(),
                    amountFormatted+" paid to credit and added to credit report",
                    Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mPayableCreditItemPainConfirmationActivity, "Error: "+e.getMessage());
        }
    }

    @Override
    public void displayError(String message) {
        activityPayableCreditItemPaidConfirmationBinding.errorTv.setVisibility(View.VISIBLE);
        activityPayableCreditItemPaidConfirmationBinding.errorTv.setText(message);
    }

    public void payable(double newAmount){
        double creditAmount = Double.parseDouble(creditDataList.get(5));
        double oldPaidAmount = Double.parseDouble(creditDataList.get(6));
        double newPaidAmount = oldPaidAmount + newAmount;

        // Balance is what is where the paid amount should be deducted from.
        double oldBalance = Double.parseDouble(creditDataList.get(7));
        double newBalance = new CreditCalculation().getCreditBalance(oldBalance, newAmount);

        Log.d(mPayableCreditItemPainConfirmationActivity,
                "CreditAmount:"+creditAmount+", OldPaidAmount:"+oldPaidAmount
                        +", newPaidAmount:"+newPaidAmount+", oldBalance:"+oldBalance
                        +", newBalance:"+newBalance);

        // Paid amount should not be greater than credit amount
        if(newAmount > creditAmount){
            displayError("The amount paid should not be more than the credit being settled!");
        }else{
            User user = userLocalStorage.getLoggedInUser();

            // Credit
            Credit credit = new Credit();
            credit.setId(Integer.parseInt(creditDataList.get(0)));
            credit.setUserId(Integer.parseInt(creditDataList.get(1)));
            credit.setBusinessId(Integer.parseInt(creditDataList.get(2)));
            credit.setName(creditDataList.get(3));
            credit.setPhone(creditDataList.get(4));
            credit.setAmount(creditAmount);
            credit.setPaidAmount(newPaidAmount);
            credit.setBalance(newBalance);
            credit.setType(creditDataList.get(8));
            credit.setDateTime(creditDataList.get(9));

            // CashUp
            CashUp cashUp = new CashUp();
            cashUp.setUserId(user.getId());
            cashUp.setCreditId(Integer.parseInt(creditDataList.get(0)));
            cashUp.setAmount(newAmount);
            cashUp.setDateTime(new DateTimeUtils().getTodayDateTime());

            // Income
            Income income = new Income();
            income.setUserId(user.getId());
            income.setDateTime(new DateTimeUtils().getTodayDateTime());

            // Expense
            Expense expense = new Expense();
            expense.setUserId(user.getId());
            expense.setCreditId(credit.getId());
            expense.setItem(credit.getName());
            expense.setAmount(newAmount);
            expense.setDateTime(new DateTimeUtils().getTodayDateTime());

            presenter = new PayableCreditItemPaidConfirmationActivityPresenter(
                    new CreditLocalDb(this),
                    new ExpenseLocalDb(this),
                    new IncomeLocalDb(this),
                    this
            );
            presenter.clearPayable(credit, expense, income);
        }
    }
}