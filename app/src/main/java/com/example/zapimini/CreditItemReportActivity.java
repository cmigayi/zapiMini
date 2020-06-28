package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.data.Credit;
import com.example.zapimini.databinding.ActivityCreditItemBinding;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.presenters.CreditItemReportActivityPresenter;
import com.example.zapimini.views.CreditItemReportActivityView;

import java.util.List;

public class CreditItemReportActivity extends AppCompatActivity
        implements View.OnClickListener, CreditItemReportActivityView {

    final static String mCreditItemActivity = "CreditItemActivity";
    ActivityCreditItemBinding activityCreditItemBinding;

    CreditItemReportActivityPresenter presenter;

    Credit importedCredit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreditItemBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_credit_item);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Modify Credit");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        CreditLocalDb expenseLocalDb = new CreditLocalDb(this);
        presenter = new CreditItemReportActivityPresenter(expenseLocalDb,this);

        List<String> creditDataList = (List<String>) getIntent().getSerializableExtra("creditDataList");
        Log.d(mCreditItemActivity, ""+creditDataList);

        importedCredit = new Credit(
                Integer.parseInt(creditDataList.get(0)),
                Integer.parseInt(creditDataList.get(1)),
                Integer.parseInt(creditDataList.get(2)),
                creditDataList.get(3),
                creditDataList.get(4),
                creditDataList.get(5),
                Double.parseDouble(creditDataList.get(6)),
                creditDataList.get(7),
                creditDataList.get(8)
        );
        loadCreditToFields(importedCredit);

        activityCreditItemBinding.updateBtn.setOnClickListener(this);
        activityCreditItemBinding.deleteBtn.setOnClickListener(this);
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
        activityCreditItemBinding.contraint1.setVisibility(View.GONE);
        Credit credit = new Credit(
                importedCredit.getId(),
                importedCredit.getBusinessId(),
                importedCredit.getUserId(),
                activityCreditItemBinding.name.getText().toString(),
                "",
                "",
                Double.parseDouble(activityCreditItemBinding.amount.getText().toString()),
                "",
                importedCredit.getDateTime()
        );

        switch(v.getId()){
            case R.id.update_btn:
                presenter.updateCredit(credit);
                break;
            case R.id.delete_btn:
                presenter.deleteCredit(credit);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityCreditItemBinding.progressBar;
    }

    @Override
    public void loadCreditToFields(Credit credit) {
        activityCreditItemBinding.pageTitle.setText("Update '"+credit.getName()+"'");
        activityCreditItemBinding.name.setText(credit.getName());
        activityCreditItemBinding.amount.setText(""+credit.getAmount());
    }

    @Override
    public void updatedCredit(Credit credit) {
        try{
            Log.d(mCreditItemActivity, "Done");
            intent = new Intent(CreditItemReportActivity.this,
                    CreditItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have updated this credit successfully!");
            intent.putExtra("message_2", "Credit: "+credit.getName()+
                    ", Fare: ksh."+credit.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mCreditItemActivity, "Error: "+e.getMessage());
            displayError("Sorry an error occured.");
        }
    }

    @Override
    public void deletedCredit(Credit credit) {
        try{
            Log.d(mCreditItemActivity, "Done");
            intent = new Intent(CreditItemReportActivity.this, CreditItemReportConfirmationActivity.class);
            intent.putExtra("message", "You have updated this credit successfully!");
            intent.putExtra("message_2", "Credit: "+credit.getName()+
                    ", Fare: ksh."+credit.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mCreditItemActivity, "Error: "+e.getMessage());
            displayError("Sorry an error occured.");
        }
    }

    @Override
    public void displayError(String message) {
    }
}
