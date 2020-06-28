package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.ExpenseDao;
import com.example.zapimini.data.Expense;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Expense.class, exportSchema = false, version = 1)
public abstract class ExpenseDaoDatabase extends RoomDatabase {
    private static final String DB_NAME = "expense_db";
    private static ExpenseDaoDatabase instance;

    public static synchronized ExpenseDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpenseDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ExpenseDao expenseDao();
}
