package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCashUpsReportBinding;
import com.example.zapimini.databinding.ActivityMoreFilterBinding;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CashUpsReportActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class MoreFilterActivity extends AppCompatActivity implements View.OnClickListener {
    final static String mMoreFilterActivity = "MoreFilterActivity";

    ActivityMoreFilterBinding activityMoreFilterBinding;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoreFilterBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_more_filter);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("More filter");
        setSupportActionBar(toolbar);

        userLocalStorage = new UserLocalStorage(this);
        user = userLocalStorage.getLoggedInUser();

        activityMoreFilterBinding.submitBtn.setOnClickListener(this);
        activityMoreFilterBinding.cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:
                String selectedPaymentMode = "";
                int radioButtonID = activityMoreFilterBinding.paymentModeGb.getCheckedRadioButtonId();
                Log.d(mMoreFilterActivity, "selected: "+radioButtonID);
                if (radioButtonID != -1) {
                    RadioButton radioButton = (RadioButton) activityMoreFilterBinding.paymentModeGb.findViewById(radioButtonID);
                    selectedPaymentMode = (String) radioButton.getText();

                    intent = new Intent(
                            MoreFilterActivity.this, CashUpsReportActivity.class);
                    intent.putExtra("paymentMode", selectedPaymentMode);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.cancel_btn:
                intent = new Intent(
                        MoreFilterActivity.this, CashUpsReportActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}