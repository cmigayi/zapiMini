package com.example.zapimini.daos;

import com.example.zapimini.data.Credit;
import com.example.zapimini.data.Expense;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CreditDao {
    @Query("select * from credits order by id desc")
    public List<Credit> getCredits();

    @Insert
    public Long insertCredit(Credit credit);

    @Update
    public void updateCredit(Credit credit);

    @Delete
    public void deleteCredit(Credit credit);

    @Query("select * from credits order by id desc limit 1 ")
    public List<Credit> getLastInsertedCredit();

    @Query("select * from credits where user_id = :userId order by id desc")
    public List<Credit> getCreditsByUserId(int userId);

    @Query("select * from credits where user_id = :userId and date_time between :dateFrom and :dateTo order by id desc")
    public List<Credit> getCreditsByUserIdByDate(int userId, String dateFrom, String dateTo);
}
