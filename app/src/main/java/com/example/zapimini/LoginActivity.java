package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityLoginBinding;
import com.example.zapimini.localDatabases.UserLocalDb;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.CreateAccountActivityPresenter;
import com.example.zapimini.presenters.LoginActivityPresenter;
import com.example.zapimini.views.LoginActivityView;

public class LoginActivity extends AppCompatActivity
        implements LoginActivityView, View.OnClickListener {
    final static String mLogAccountActivity = "LoginAccountActivity";
    ActivityLoginBinding activityLoginBinding;
    static Activity aLoginActivity;

    UserLocalStorage userLocalStorage;

    String username, password;
    Intent intent;

    LoginActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        aLoginActivity = this;

        userLocalStorage= new UserLocalStorage(this);

        UserLocalDb userLocalDb = new UserLocalDb(this);
        presenter = new LoginActivityPresenter(userLocalDb, this);

        activityLoginBinding.loginBtn.setOnClickListener(this);
        activityLoginBinding.createAccountBtn.setOnClickListener(this);
        activityLoginBinding.noAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                if(validateInputs()){
                    activityLoginBinding.errorTv.setVisibility(View.GONE);
                    activityLoginBinding.contraint1.setVisibility(View.GONE);

                    String username = activityLoginBinding.username.getText().toString();
                    String password = activityLoginBinding.password.getText().toString();

                    User user= new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    Log.d(mLogAccountActivity, "user: "+user.getUsername());
                    presenter.authorizeUser(user);
                }
                break;
            case R.id.create_account_btn:
                intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.no_account_btn:
                Log.d(mLogAccountActivity, "user: test");
                if(noAccountUser()){
                    User user= new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    Log.d(mLogAccountActivity, "user: "+user.getUsername());
                    presenter.createUser(user);
                }
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityLoginBinding.progressBar;
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;

        if(activityLoginBinding.username.getText().toString().equals("") ||
                activityLoginBinding.password.getText().toString().equals("")){
            displayError("Username or password is invalid!");
        }else {
            username = activityLoginBinding.username.getText().toString();
            password = activityLoginBinding.password.getText().toString();
            validated = true;
        }
        return validated;
    }

    @Override
    public boolean noAccountUser() {
        username = "zm_001";
        password = "123";
        return true;
    }

    @Override
    public void authorizeUser(User user) {
        try{
            Log.d(mLogAccountActivity, "Authorize user: done");

            userLocalStorage.storeUser(user);
            if(userLocalStorage.getLoggedInUser() != null){
                userLocalStorage.setUserLogged(true);
            }

            if(userLocalStorage.isUserLogged()){
                if(userLocalStorage.isUserBusinessAdded()){
                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(LoginActivity.this, CreateBusinessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }catch(Exception e){
            Log.d(mLogAccountActivity, "Authorize user Error: "+e.getMessage());
        }
    }

    @Override
    public void displayError(String message) {
        activityLoginBinding.contraint1.setVisibility(View.VISIBLE);
        activityLoginBinding.errorTv.setText(message);
        activityLoginBinding.errorTv.setVisibility(View.VISIBLE);
    }
}
