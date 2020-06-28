package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.User;
import com.example.zapimini.repositories.UserRepository;
import com.example.zapimini.views.CreateAccountActivityView;

import java.util.List;

public class CreateAccountActivityPresenter {
    final static String mCreateAccountActivityPresenter = "CreateAccountActivityP";
    UserRepository repository;
    CreateAccountActivityView view;

    public CreateAccountActivityPresenter(UserRepository repository, CreateAccountActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void createUser(User user){
        repository.createUser(user, view.getProgressbar(), new UserRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<User> userList) {
                Log.d(mCreateAccountActivityPresenter, "Response: "+userList.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.createdUser(userList.get(0));
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mCreateAccountActivityPresenter, "Error: "+t.getMessage());
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
