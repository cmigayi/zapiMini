package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.IncomeDao;
import com.example.zapimini.data.Income;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Income.class, exportSchema = false, version = 1)
public abstract class IncomeDaoDatabase extends RoomDatabase {
    private static final String DB_NAME = "income_db";
    private static IncomeDaoDatabase instance;

    public static synchronized IncomeDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    IncomeDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract IncomeDao incomeDao();
}
