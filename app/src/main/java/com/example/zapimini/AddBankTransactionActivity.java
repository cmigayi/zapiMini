package com.example.zapimini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zapimini.commons.BankCalculation;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.BankTransaction;
import com.example.zapimini.data.Business;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityAddBankTransactionBinding;
import com.example.zapimini.localDatabases.BankTransactionLocalDb;
import com.example.zapimini.localStorage.BusinessLocalStorage;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.AddBankTransactionActivityPresenter;
import com.example.zapimini.views.AddBankTransactionActivityView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class AddBankTransactionActivity extends AppCompatActivity implements
        View.OnClickListener, AddBankTransactionActivityView {
    final static String mBankTransactionActivity = "BankTransaction";

    ActivityAddBankTransactionBinding activityAddBankTransactionBinding;
    AddBankTransactionActivityPresenter presenter;

    String selectedType;
    double amount;
    String[] typeList = {"Deposit","Withdraw"};

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;

    BankTransaction previousBankTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBankTransactionBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_add_bank_transaction);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Add bank transaction");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        BankTransactionLocalDb bankTransactionLocalDb = new BankTransactionLocalDb(this);
        presenter = new AddBankTransactionActivityPresenter(this, bankTransactionLocalDb);
        loadTypelist();
        loadMoneyFromList();
        presenter.getPreviousBankTransactionInLocalDb(this);
        onSelectedTransactionType();

        activityAddBankTransactionBinding.submit.setOnClickListener(this);
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
        switch(v.getId()) {
            case R.id.submit:
                if(validateInputs()) {
                    BankCalculation bankCalculation = new BankCalculation();
                    double balance = bankCalculation.getBalanceFromPreviousTransactions(
                            previousBankTransaction, amount, selectedType);
                    BankTransaction bankTransaction = new BankTransaction(0,
                            getUserBusinessId(),
                            user.getId(),
                            selectedType,
                            amount,
                            balance,
                            new DateTimeUtils().getTodayDateTime()
                    );
                    presenter.createBankTransactionInLocalDb(bankTransaction, getApplicationContext());
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityAddBankTransactionBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(AddBankTransactionActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void loadTypelist() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, typeList);
        activityAddBankTransactionBinding.typelist.setAdapter(arrayAdapter);
        activityAddBankTransactionBinding.typelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedType = typeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void previousBankTransaction(BankTransaction bankTransaction) {
        previousBankTransaction = bankTransaction;
    }

    @Override
    public void displayPreviousBankTransaction(BankTransaction bankTransaction) {
        activityAddBankTransactionBinding.successTv.setVisibility(View.VISIBLE);
        String message = null;
        try{
            message = "Previous Transaction: (Amount: " + bankTransaction.getAmount() +
                    ", Type: " + bankTransaction.getType() + ")";
        }catch(Exception e) {
            message = "Previous Transaction: None";
        }
        activityAddBankTransactionBinding.successTv.setText(message);
    }

    @Override
    public int getUserBusinessId() {
        BusinessLocalStorage businessLocalStorage = new BusinessLocalStorage(this);
        Business business = businessLocalStorage.getBusiness();
        return business.getId();
    }

    @Override
    public void createdBankTransaction(BankTransaction bankTransaction) {
        try{
            Log.d(mBankTransactionActivity, "Done");
            intent = new Intent(AddBankTransactionActivity.this, ConfirmationActivity.class);
            intent.putExtra("message", "You have added this transaction to bank reports successfully!");
            intent.putExtra("message_2", "Transaction type: "+bankTransaction.getType()+
                    ", Amount: ksh."+bankTransaction.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mBankTransactionActivity, "Error: "+e.getMessage());
        }
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;
        if(activityAddBankTransactionBinding.amount.getText().toString().equals("") ||
                selectedType.equals("")){
            displayError("Amount or selected type is invalid!");
        }else{
            amount = Double.parseDouble(activityAddBankTransactionBinding.amount.getText().toString());
            validated = true;
        }
        return validated;
    }

    @Override
    public void displayError(String message) {
        activityAddBankTransactionBinding.progressBar.setVisibility(View.GONE);
        activityAddBankTransactionBinding.errorTv.setText(message);
        activityAddBankTransactionBinding.errorTv.setVisibility(View.VISIBLE);
    }

    private void onSelectedTransactionType(){
        activityAddBankTransactionBinding.typelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(mBankTransactionActivity, typeList[position]);

                if(position == 0){
                    activityAddBankTransactionBinding.chooseMoneyFromTv.setVisibility(View.VISIBLE);
                    activityAddBankTransactionBinding.moneyFromList.setVisibility(View.VISIBLE);
                }else{
                    activityAddBankTransactionBinding.chooseMoneyFromTv.setVisibility(View.GONE);
                    activityAddBankTransactionBinding.moneyFromList.setVisibility(View.GONE);
                }
                activityAddBankTransactionBinding.amountTitle.setVisibility(View.VISIBLE);
                activityAddBankTransactionBinding.amount.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadMoneyFromList(){
        String[] moneyFromList = {"Business income", "New investment/ funds"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, moneyFromList);
        activityAddBankTransactionBinding.moneyFromList.setAdapter(arrayAdapter);
        activityAddBankTransactionBinding.moneyFromList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
