package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zapimini.databinding.ActivityAddCreditConfirmationBinding;

public class AddCreditConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    final static String mAddCreditConfirmationActivity = "AddCreditConfirmationActivity";
    ActivityAddCreditConfirmationBinding activityAddCreditConfirmationBinding;
    Intent intent;

    String success, transaction, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddCreditConfirmationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_add_credit_confirmation);

        success = getIntent().getStringExtra("message");
        transaction = getIntent().getStringExtra("message_2");
        type = getIntent().getStringExtra("type");
        activityAddCreditConfirmationBinding.successTv.setText(success);
        activityAddCreditConfirmationBinding.transactionTv.setText(transaction);
        activityAddCreditConfirmationBinding.addAnother.setOnClickListener(this);
        activityAddCreditConfirmationBinding.creditReport.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.add_another:
                intent = new Intent(AddCreditConfirmationActivity.this,
                        AddCreditActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.credit_report:
                if(type.equals("This business or person owes me or my business money (Receivable).")){
                    intent = new Intent(AddCreditConfirmationActivity.this,
                            CreditsReportActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(AddCreditConfirmationActivity.this,
                            PayableCreditsReportActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }

    }
}
