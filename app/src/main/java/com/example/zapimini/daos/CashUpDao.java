package com.example.zapimini.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zapimini.data.CashUp;
import com.example.zapimini.data.Income;

import java.util.List;

@Dao
public interface CashUpDao {
    @Query("select * from cash_ups order by id desc")
    public List<CashUp> getCashUps();

    @Insert
    public Long insertCashUp(CashUp cashUp);

    @Update
    public void updateCashUp(CashUp cashUp);

    @Delete
    public void deleteCashUp(CashUp cashUp);

    @Query("select * from cash_ups order by id desc limit 1 ")
    public List<CashUp> getLastInsertedCashUp();

    @Query("select * from cash_ups where user_id=:userId order by id desc")
    public List<CashUp> getCashUpsByUserId(int userId);

    @Query("select * from cash_ups where user_id=:userId and date_time between :dateFrom and :dateTo order by id desc")
    public List<CashUp> getCashUpsByUserIdByDate(int userId, String dateFrom, String dateTo);
}
