package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityProfileBinding;
import com.example.zapimini.localDatabases.UserLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.ProfileActivityPresenter;
import com.example.zapimini.views.ProfileActivityView;

public class ProfileActivity extends AppCompatActivity implements ProfileActivityView {
    final static String mProfileActivity = "ProfileActivity";

    ActivityProfileBinding activityProfileBinding;

    Intent intent;

    ProfileActivityPresenter presenter;

    UserLocalStorage userLocalStorage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(
                this,R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();
        populateProfileForm(user);

        UserLocalDb userLocalDb = new UserLocalDb(this);
        presenter = new ProfileActivityPresenter(userLocalDb, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.logout:
                userLocalStorage.clearUserData();
                finish();
                HomeActivity.aHome.finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityProfileBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean validateInputs() {
        return false;
    }

    @Override
    public void populateProfileForm(User user) {
        activityProfileBinding.contraint1.setVisibility(View.VISIBLE);
        activityProfileBinding.setUser(user);
    }

    @Override
    public void redirectOnLogout() {
        intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void displayError(String message) {
        activityProfileBinding.errorTv.setVisibility(View.VISIBLE);
        activityProfileBinding.progressBar.setVisibility(View.GONE);
        activityProfileBinding.errorTv.setText(message);
    }
}
