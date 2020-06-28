package com.example.zapimini.presenters;

import android.util.Log;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.data.User;
import com.example.zapimini.localDatabases.UserLocalDb;
import com.example.zapimini.repositories.UserRepository;
import com.example.zapimini.views.ProfileActivityView;

import java.util.List;

public class ProfileActivityPresenter {
    final static String mProfileActivityPresenter = "ProfileActivity";
    UserLocalDb repository;
    ProfileActivityView view;

    public ProfileActivityPresenter(UserLocalDb repository, ProfileActivityView view) {
        this.repository = repository;
        this.view = view;
    }

    public void getUser(User user){
        repository.getUser(user, new UserRepository.OnFinishedListerner() {
            @Override
            public void onFinished(List<User> userList) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(mProfileActivityPresenter, "Userlist: "+userList);
                            view.populateProfileForm(userList.get(0));
                        }catch(Exception e){
                            Log.d(mProfileActivityPresenter, "Error: "+e.getMessage());
                            view.displayError("There was a problem / no data found.");
                        }
                    }
                });
            }

            @Override
            public void onFinished(Object response) {

            }

            @Override
            public void onFailuire(Throwable t) {
                Log.d(mProfileActivityPresenter, "Error: "+t.getMessage());
                view.displayError("There was a problem retrievining the data.");
            }
        });
    }
}
