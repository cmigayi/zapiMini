package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.zapimini.commons.DatePickerFragment;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityMoreFilterBinding;
import com.example.zapimini.localStorage.UserLocalStorage;

public class MoreFilterActivity extends AppCompatActivity implements View.OnClickListener {
    final static String mMoreFilterActivity = "MoreFilterActivity";

    ActivityMoreFilterBinding activityMoreFilterBinding;

    Intent intent;

    UserLocalStorage userLocalStorage;
    User user;
    String selectedDate;

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

        selectedDate = "";
        if(getIntent().getStringExtra("date") != null){
            selectedDate = getIntent().getStringExtra("date");
            activityMoreFilterBinding.selectedDateTv.setText(selectedDate);
        }

        activityMoreFilterBinding.submitBtn.setOnClickListener(this);
        activityMoreFilterBinding.cancelBtn.setOnClickListener(this);
        activityMoreFilterBinding.datePickerBtn.setOnClickListener(this);
        activityMoreFilterBinding.dateRangePickerBtn.setOnClickListener(this);
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
                    intent.putExtra("date", selectedDate);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.date_picker_btn:
                DialogFragment newFragment = new DatePickerFragment(MoreFilterActivity.class);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.date_range_picker_btn:
                setupRangePickerDialog();
                break;
            case R.id.cancel_btn:
                intent = new Intent(
                        MoreFilterActivity.this, CashUpsReportActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setupRangePickerDialog() {

    }

}