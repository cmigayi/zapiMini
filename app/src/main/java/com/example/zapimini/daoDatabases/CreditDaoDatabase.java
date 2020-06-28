package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.CreditDao;
import com.example.zapimini.daos.ExpenseDao;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.Expense;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Credit.class, exportSchema = false, version = 1)
public abstract class CreditDaoDatabase extends RoomDatabase {
        private static final String DB_NAME = "credit_db";
        private static CreditDaoDatabase instance;

        public static synchronized CreditDaoDatabase getInstance(Context context){
            if(instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        CreditDaoDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }

        public abstract CreditDao creditDao();
}
