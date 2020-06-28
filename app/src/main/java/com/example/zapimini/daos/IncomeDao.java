package com.example.zapimini.daos;

import com.example.zapimini.data.Income;
import com.example.zapimini.data.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IncomeDao {
    @Query("select * from incomes order by id desc")
    public List<Income> getIncomes();

    @Insert
    public Long insertIncome(Income income);

    @Update
    public void updateIncome(Income income);

    @Delete
    public void deleteIncome(Income income);

    @Query("select * from incomes order by id desc limit 1 ")
    public List<Income> getLastInsertedIncome();

    @Query("select * from incomes where user_id=:userId order by id desc")
    public List<Income> getIncomesByUserId(int userId);

    @Query("select * from incomes where user_id=:userId and date_time between :dateFrom and :dateTo order by id desc")
    public List<Income> getIncomesByUserIdByDate(int userId, String dateFrom, String dateTo);
}
