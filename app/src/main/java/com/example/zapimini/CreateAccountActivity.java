package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityCreateAccountBinding;
import com.example.zapimini.localDatabases.UserLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreateAccountActivityPresenter;
import com.example.zapimini.views.CreateAccountActivityView;

public class CreateAccountActivity extends AppCompatActivity
        implements CreateAccountActivityView, View.OnClickListener {
    final static String mCreateAccountActivity = "CreateAccountActivity";
    ActivityCreateAccountBinding activityCreateAccountBinding;

    UserLocalStorage userLocalStorage;

    CreateAccountActivityPresenter presenter;
    UserLocalDb userLocalDb;

    String username, password;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateAccountBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_create_account);

        userLocalStorage= new UserLocalStorage(this);

        userLocalDb = new UserLocalDb(this);
        presenter = new CreateAccountActivityPresenter(userLocalDb, this);

        activityCreateAccountBinding.createAccountBtn.setOnClickListener(this);
        activityCreateAccountBinding.loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_account_btn:
                if(validateInputs()){
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setDateTime(new DateTimeUtils().getTodayDateTime());
                    presenter.createUser(user);
                }
                break;
            case R.id.login_btn:
                intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        activityCreateAccountBinding.progressBar.setVisibility(View.VISIBLE);
        activityCreateAccountBinding.contraint1.setVisibility(View.GONE);
        return activityCreateAccountBinding.progressBar;
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;

        if(activityCreateAccountBinding.username.getText().toString().equals("") ||
                activityCreateAccountBinding.password.getText().toString().equals("")){
            displayError("Username or password is invalid!");
        }else {
            if (!activityCreateAccountBinding.password.getText().toString()
                    .equals(activityCreateAccountBinding.passwordConfirm.getText().toString())) {
                displayError("Passwords do not match!");
            }else{
                username = activityCreateAccountBinding.username.getText().toString();
                password = activityCreateAccountBinding.password.getText().toString();
                validated = true;
            }
        }
        return validated;
    }

    @Override
    public void createdUser(User user) {
        Log.d(mCreateAccountActivity, "Create account: done");
        Log.d(mCreateAccountActivity, "Created account: "+user.getUsername()+
                " id: "+user.getId());
        userLocalStorage.storeUser(user);
        User userFromDb = userLocalStorage.getLoggedInUser();

        if(userLocalStorage.getLoggedInUser() != null){
            userLocalStorage.setUserLogged(true);
        }

        if(userLocalStorage.isUserLogged()) {
            Log.d(mCreateAccountActivity, "User from db: "+userFromDb.getUsername()+
                    " id: "+userFromDb.getId());
            intent = new Intent(CreateAccountActivity.this, CreateBusinessActivity.class);
            startActivity(intent);
            finish();
            LoginActivity.aLoginActivity.finish();
        }
    }

    @Override
    public void displayError(String message) {
        activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
        activityCreateAccountBinding.errorTv.setText(message);
        activityCreateAccountBinding.errorTv.setVisibility(View.VISIBLE);
    }
}
