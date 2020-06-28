package com.example.zapimini.repositories;

import android.widget.ProgressBar;

import com.example.zapimini.data.User;

import java.util.List;

public interface UserRepository {
    public interface OnFinishedListerner{
        void onFinished(List<User> userList);
        void onFinished(Object response);
        void onFailuire(Throwable t);
    }

    public void createUser(User user, ProgressBar progressBar, OnFinishedListerner onFinishedListerner);

    public void getUser(User user, OnFinishedListerner onFinishedListerner);

    public void getUserAuth(User user, OnFinishedListerner onFinishedListerner);

    public void updateUser(User user, OnFinishedListerner onFinishedListerner);

    public void deleteUser(User user, OnFinishedListerner onFinishedListerner);
}
