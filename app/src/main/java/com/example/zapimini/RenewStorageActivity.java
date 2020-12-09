package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityRenewStorageBinding;
import com.example.zapimini.databinding.ActivitySettingsBinding;
import com.example.zapimini.localStorage.UserLocalStorage;

public class RenewStorageActivity extends AppCompatActivity {
    final static String mRenewStorageActivity = "RenewStorage";
    ActivityRenewStorageBinding activityRenewStorageBinding;
    static Activity aRenewStorageActivity;

    Intent intent;
    UserLocalStorage userLocalStorage;
    User user;

    String [] businesslist = {"Business A","Business B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_storage);

        aRenewStorageActivity = this;

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Renew Storage");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
}