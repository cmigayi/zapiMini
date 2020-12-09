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

    @Query("select * from credits where user_id = :userId and type = :type order by id desc")
    public List<Credit> getCreditsByUserIdByType(int userId, String type);

    @Query("select * from credits where user_id = :userId and type = :type and date_time between :dateFrom and :dateTo order by id desc")
    public List<Credit> getCreditsByUserIdByTypeByDate(int userId, String type, String dateFrom, String dateTo);

    @Query("select * from credits where user_id = :userId and type = :type and credit_status = :creditStatus order by id desc")
    public List<Credit> getCreditsByUserIdByTypeByCreditStatus(int userId, String type, String creditStatus);

    @Query("select * from credits where user_id = :userId and type = :type and credit_status = :creditStatus and date_time between :dateFrom and :dateTo order by id desc")
    public List<Credit> getCreditsByUserIdByTypeByCreditStatusByDate(int userId, String type, String creditStatus, String dateFrom, String dateTo);
}
