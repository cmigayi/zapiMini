package com.example.zapimini.localDatabases;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.zapimini.commons.AppExecutors;
import com.example.zapimini.daoDatabases.UserDaoDatabase;
import com.example.zapimini.data.User;
import com.example.zapimini.repositories.UserRepository;

import java.util.List;

public class UserLocalDb implements UserRepository {
    final static String mUserLocalDb = "UserLocalDb";
    UserDaoDatabase userDaoDatabase;

    public UserLocalDb(Context context) {
        userDaoDatabase = UserDaoDatabase.getInstance(context);
    }

    @Override
    public void createUser(User user, ProgressBar progressBar, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Long status = userDaoDatabase.userDao().createUser(user);
                    if(status > 0){
                        List<User> userList = userDaoDatabase.userDao().getLastInsertedUser();
                        Log.d(mUserLocalDb, "Success01: " + userList.get(0).getId());
                        onFinishedListerner.onFinished(userList);
                    }
                }catch(Exception e){
                    Log.d(mUserLocalDb, "Error: "+e.getMessage());
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void getUser(User user, OnFinishedListerner onFinishedListerner) {

    }

    @Override
    public void getUserAuth(User user, OnFinishedListerner onFinishedListerner) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<User> userList = userDaoDatabase.userDao().getAuthUser(
                            user.getUsername(), user.getPassword());
                    onFinishedListerner.onFinished(userList);
                }catch(Exception e){
                    Log.d(mUserLocalDb, "Error: "+e.getMessage());
                    onFinishedListerner.onFailuire(e);
                }
            }
        });
    }

    @Override
    public void updateUser(User user, OnFinishedListerner onFinishedListerner) {

    }

    @Override
    public void deleteUser(User user, OnFinishedListerner onFinishedListerner) {

    }
}
