package com.example.zapimini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivitySettingsBinding;
import com.example.zapimini.localStorage.UserLocalStorage;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    final static String mSettingsActivity = "SettingsActivity";
    ActivitySettingsBinding activitySettingsBinding;
    static Activity aSettingsActivity;

    Intent intent;
    UserLocalStorage userLocalStorage;
    User user;

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

        activitySettingsBinding.backUpBtn.setOnClickListener(this);
        activitySettingsBinding.restoreBtn.setOnClickListener(this);
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
        }
    }
}