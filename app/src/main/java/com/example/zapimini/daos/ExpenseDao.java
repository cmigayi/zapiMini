package com.example.zapimini.daos;

import com.example.zapimini.data.Expense;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ExpenseDao {
    @Query("select * from expenses order by id desc")
    public List<Expense> getExpenses();

    @Insert
    public Long insertExpense(Expense expense);

    @Update
    public void updateExpense(Expense expense);

    @Delete
    public void deleteExpense(Expense expense);

    @Query("select * from expenses order by id desc limit 1 ")
    public List<Expense> getLastInsertedExpense();

    @Query("select * from expenses where date_time between :dateFrom and :dateTo order by id desc")
    public List<Expense> getExpensesByDate(String dateFrom, String dateTo);
}
