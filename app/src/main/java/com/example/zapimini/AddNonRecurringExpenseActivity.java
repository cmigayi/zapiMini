package com.example.zapimini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.Business;
import com.example.zapimini.data.Expense;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityAddNonRecurringExpenseBinding;
import com.example.zapimini.localDatabases.ExpenseLocalDb;
import com.example.zapimini.localStorage.BusinessLocalStorage;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.AddNonRecurringExpenseActivityPresenter;
import com.example.zapimini.views.AddNonRecurringExpenseActivityView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class AddNonRecurringExpenseActivity extends AppCompatActivity
        implements View.OnClickListener, AddNonRecurringExpenseActivityView {
    final static String mAddNonRecurringExpenseActivity = "AddNonRecurringExpense";

    ActivityAddNonRecurringExpenseBinding activityAddNonRecurringExpenseBinding;
    static Activity mAddNonRecurringExpense;

    AddNonRecurringExpenseActivityPresenter presenter;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;

    ExpenseLocalDb expenseLocalDb;

    String item;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNonRecurringExpenseBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_add_non_recurring_expense);

        mAddNonRecurringExpense = this;

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Add expense");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        expenseLocalDb = new ExpenseLocalDb(this);
        presenter = new AddNonRecurringExpenseActivityPresenter(expenseLocalDb, this);

        activityAddNonRecurringExpenseBinding.submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        intent = new Intent(AddNonRecurringExpenseActivity.this, HomeActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
//                intent = new Intent(AddNonRecurringExpenseActivity.this, HomeActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if(validateInputs()) {
                    BusinessLocalStorage businessLocalStorage = new BusinessLocalStorage(this);
                    Business business = businessLocalStorage.getBusiness();

                    Expense expense = new Expense(
                            0,
                            business.getId(),
                            user.getId(),
                            -1,
                            activityAddNonRecurringExpenseBinding.itemName.getText().toString(),
                            Double.parseDouble(activityAddNonRecurringExpenseBinding.amountValue.getText().toString()),
                            new DateTimeUtils().getTodayDateTime()
                    );
                    presenter.createNonRecurringExpenseInLocalDb(expense, this);
                }
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityAddNonRecurringExpenseBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(AddNonRecurringExpenseActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;
        if(activityAddNonRecurringExpenseBinding.itemName.getText().toString().equals("") ||
                activityAddNonRecurringExpenseBinding.amountValue.getText().toString().equals("")){
            displayError("Expense item or amount is invalid!");
        }else {
            item = activityAddNonRecurringExpenseBinding.itemName.getText().toString();
            amount = Double.parseDouble(
                    activityAddNonRecurringExpenseBinding.amountValue.getText().toString());
            validated = true;
        }
        return validated;
    }

    @Override
    public void displayError(String message) {
        activityAddNonRecurringExpenseBinding.progressBar.setVisibility(View.GONE);
        activityAddNonRecurringExpenseBinding.errorTv.setText(message);
        activityAddNonRecurringExpenseBinding.errorTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void createdExpense(Expense expense) {
        try{
            Log.d(mAddNonRecurringExpenseActivity, "Done");
            clearTextFields();
            intent = new Intent(AddNonRecurringExpenseActivity.this,
                    AddNonRecurringExpenseConfirmationActivity.class);
            intent.putExtra("message", "You have added this expense to expense reports successfully!");
            intent.putExtra("message_2", "Item: "+expense.getItem()+
                    ", Amount: ksh."+expense.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mAddNonRecurringExpenseActivity, "Error: "+e.getMessage());
            displayError("");
        }
    }

    private void clearTextFields(){
        activityAddNonRecurringExpenseBinding.itemName.setText("");
        activityAddNonRecurringExpenseBinding.amountValue.setText("");
    }
}
