package com.example.zapimini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivitySettingsBinding;
import com.example.zapimini.localStorage.UserLocalStorage;

public class SettingsActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    final static String mSettingsActivity = "SettingsActivity";
    ActivitySettingsBinding activitySettingsBinding;
    static Activity aSettingsActivity;

    Intent intent;
    UserLocalStorage userLocalStorage;
    User user;

    String [] businesslist = {"Choose business","Business A","Business B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        aSettingsActivity = this;

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userLocalStorage = new UserLocalStorage(this);
        user = userLocalStorage.getLoggedInUser();

        ArrayAdapter<String> businessAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, businesslist);
        activitySettingsBinding.businessesSp.setAdapter(businessAdapter);
        activitySettingsBinding.businessesSp.setOnItemSelectedListener(this);

        activitySettingsBinding.backUpBtn.setOnClickListener(this);
        activitySettingsBinding.restoreBtn.setOnClickListener(this);
        activitySettingsBinding.renewBtn.setOnClickListener(this);
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
            case R.id.back_up_btn:

                break;
            case R.id.restore_btn:

                break;
            case R.id.renew_btn:
                intent = new Intent(SettingsActivity.this, RenewStorageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0){
            activitySettingsBinding.currentBusinessName.setText(businesslist[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}