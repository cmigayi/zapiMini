package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.User;
import com.example.zapimini.repositories.UserRepository;
import com.example.zapimini.views.LoginActivityView;

import java.util.List;

public class LoginActivityPresenter {
    final static String mLoginActivityPresenter = "LoginActivityPresenter";
    LoginActivityView view;
    UserRepository repository;

    public LoginActivityPresenter(UserRepository repository, LoginActivityView view) {
        this.view = view;
        this.repository = repository;
    }

    public void authorizeUser(User user){
        repository.getUserAuth(user, new UserRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<User> userList) {
                Log.d(mLoginActivityPresenter, "Userlist: "+userList);
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(userList.size() > 0){
                            view.authorizeUser(userList.get(0));
                        }else{
                            view.displayError("Username or password is invalid!");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mLoginActivityPresenter, "Error: "+t.getMessage());
                view.displayError("User authorization failed.");
            }
        });
    }

    public void createUser(User user){
        repository.createUser(user, view.getProgressbar(), new UserRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<User> userList) {
                Log.d(mLoginActivityPresenter, "Response: "+userList.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.authorizeUser(userList.get(0));
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mLoginActivityPresenter, "Error: "+t.getMessage());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.displayError("Sorry, user was not created.");
                    }
                });
            }
        });
    }
}
