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
import com.example.zapimini.localDatabases.CashUpLocalDb;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localDatabases.IncomeLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreditItemPaidConfirmationActivityPresenter;
import com.example.zapimini.views.CreditItemPaidConfirmationActivityView;
import com.example.zapimini.views.CreditItemReportActivityView;

import java.util.ArrayList;
import java.util.List;

public class CreditItemPaidConfirmationActivity extends AppCompatActivity
        implements View.OnClickListener, CreditItemPaidConfirmationActivityView {
    final static String mCreditItemPaidConfirmationActivity = "CreditPaidConfirmation";
    ActivityCreditItemPaidConfirmationBinding activityCreditItemPaidConfirmationBinding;
    Intent intent;

    UserLocalStorage userLocalStorage;

    static Activity aCreditItemPaidConfirmationActivity;
    List<String> creditDataList = new ArrayList<>();
    String type;

    double newAmount;

    CreditItemPaidConfirmationActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditItemPaidConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credit_item_paid_confirmation);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Credit Paid");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        aCreditItemPaidConfirmationActivity = this;

        creditDataList = (List<String>) getIntent().getSerializableExtra("creditDataList");

        userLocalStorage = new UserLocalStorage(this);

        String amount =
                new MoneyUtils().AddMoneyFormat(Double.parseDouble(creditDataList.get(5)));
        String balance =
                new MoneyUtils().AddMoneyFormat(Double.parseDouble(creditDataList.get(7)));

        activityCreditItemPaidConfirmationBinding.creditInfoTv.setText(
                "Name: "+
                creditDataList.get(3)+", Phone: "+
                creditDataList.get(4)+", Amount: "+
                amount +", Bal: "+
                balance +", Date: "+
                creditDataList.get(10)
        );

        activityCreditItemPaidConfirmationBinding.cancelBtn.setOnClickListener(this);
        activityCreditItemPaidConfirmationBinding.creditPaidBtn.setOnClickListener(this);
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
                Log.d(mCreditItemPaidConfirmationActivity, creditDataList.get(5));

                if(activityCreditItemPaidConfirmationBinding.amountPaidEt
                        .getText().toString().equals("")){
                    displayError("Amount field cannot be empty!");
                }else{
                    newAmount = Double.parseDouble(activityCreditItemPaidConfirmationBinding
                            .amountPaidEt.getText().toString());

                    int radioButtonID = activityCreditItemPaidConfirmationBinding
                            .paymentModeGb.getCheckedRadioButtonId();

                    Log.d(mCreditItemPaidConfirmationActivity, "selected: "+radioButtonID);
                    if (radioButtonID == -1) {
                        displayError("Payment mode must be provided!");
                    }else {
                        RadioButton radioButton = (RadioButton) activityCreditItemPaidConfirmationBinding
                                .paymentModeGb.findViewById(radioButtonID);
                        String selectedPaymentMode = (String) radioButton.getText();

                        receivable(newAmount, selectedPaymentMode);
                    }
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
            intent = new Intent(CreditItemPaidConfirmationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreditPaidConfirmed(Credit credit) {
        Log.d(mCreditItemPaidConfirmationActivity, "Create cash: done");

        try{
            Log.d(mCreditItemPaidConfirmationActivity, "Done");
            String amountFormatted = new MoneyUtils().AddMoneyFormat(newAmount);
            intent = new Intent(CreditItemPaidConfirmationActivity.this,
                    CreditsReportActivity.class);
//            intent.putExtra("message", "You have added the following to credit reports successfully!");
//            intent.putExtra("message_2", "Credit amount paid: "+amountFormatted);
            Toast.makeText(getApplicationContext(),
                    amountFormatted+" paid to credit and added to credit report",
                    Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mCreditItemPaidConfirmationActivity, "Error: "+e.getMessage());
        }
    }

    @Override
    public void displayError(String message) {
        activityCreditItemPaidConfirmationBinding.errorTv.setVisibility(View.VISIBLE);
        activityCreditItemPaidConfirmationBinding.errorTv.setText(message);
    }

    public void receivable(double newAmount, String paymentMode){
        double creditAmount = Double.parseDouble(creditDataList.get(5));
        double oldPaidAmount = Double.parseDouble(creditDataList.get(6));
        double newPaidAmount = oldPaidAmount + newAmount;

        // Balance is what is where the paid amount should be deducted from.
        double oldBalance = Double.parseDouble(creditDataList.get(7));
        double newBalance = new CreditCalculation().getCreditBalance(oldBalance, newAmount);

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
            if(newBalance == 0){
                credit.setCreditStatus("Fully paid");
            }else{
                credit.setCreditStatus("Partially paid");
            }
            credit.setDateTime(creditDataList.get(10));

            // CashUp
            CashUp cashUp = new CashUp();
            cashUp.setUserId(user.getId());
            cashUp.setCreditId(Integer.parseInt(creditDataList.get(0)));
            cashUp.setAmount(newAmount);
            cashUp.setPaymentMode(paymentMode);
            cashUp.setDateTime(new DateTimeUtils().getTodayDateTime());

            // Income
            Income income = new Income();
            income.setUserId(user.getId());
            income.setGrossAmount(newAmount);
            income.setNetAmount(new IncomeCalculation().getNetIncome(newAmount, 0.0));
            income.setDateTime(new DateTimeUtils().getTodayDateTime());

            // Expense
            Expense expense = new Expense();
            expense.setUserId(user.getId());
            expense.setItem("Payable credit");
            expense.setAmount(newAmount);
            expense.setDateTime(new DateTimeUtils().getTodayDateTime());

            presenter = new CreditItemPaidConfirmationActivityPresenter(
                    new CreditLocalDb(this),
                    new CashUpLocalDb(this),
                    new IncomeLocalDb(this),
                    this
            );
            presenter.clearReceivable(credit, cashUp, income);
        }
    }
}