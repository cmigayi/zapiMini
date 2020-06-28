package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.UserDao;
import com.example.zapimini.data.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = User.class, exportSchema = false, version = 1)
public abstract class UserDaoDatabase  extends RoomDatabase {
    private static final String DB_NAME = "user_db";
    private static UserDaoDatabase instance;

    public static synchronized UserDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}
