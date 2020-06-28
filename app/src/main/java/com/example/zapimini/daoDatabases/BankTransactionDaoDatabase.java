package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.BankTransactionDao;
import com.example.zapimini.data.BankTransaction;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = BankTransaction.class, exportSchema = false, version = 1)
public abstract class BankTransactionDaoDatabase extends RoomDatabase {
    private static final String DB_NAME = "bank_transaction_db";
    private static BankTransactionDaoDatabase instance;

    public static synchronized BankTransactionDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BankTransactionDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BankTransactionDao bankTransactionDao();
}
