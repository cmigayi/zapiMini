package com.example.zapimini.daoDatabases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.zapimini.daos.CashUpDao;
import com.example.zapimini.data.CashUp;

@Database(entities = CashUp.class, exportSchema = false, version = 1)
public abstract class CashUpDaoDatabase extends RoomDatabase {
    private static final String DB_NAME = "cash_up_db";
    private static CashUpDaoDatabase instance;

    public static synchronized CashUpDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CashUpDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CashUpDao cashUpDao();
}
