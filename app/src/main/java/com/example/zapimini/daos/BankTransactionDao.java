package com.example.zapimini.daos;

import com.example.zapimini.data.BankTransaction;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BankTransactionDao {
    @Query("select * from bank_transactions order by id desc")
    public List<BankTransaction> getBankTransactions();

    @Insert
    public Long insertBankTransaction(BankTransaction bankTransaction);

    @Update
    public void updateBankTransaction(BankTransaction bankTransaction);

    @Delete
    public void deleteBankTransaction(BankTransaction bankTransaction);

    @Query("select * from bank_transactions where date_time between :dateFrom and :dateTo order by id desc")
    public List<BankTransaction> getBankTransactionsByDate(String dateFrom, String dateTo);

    @Query("select * from bank_transactions where user_id = :userId and date_time between :dateFrom and :dateTo order by id desc")
    public List<BankTransaction> getBankTransactionsByUserIdByDate(int userId, String dateFrom, String dateTo);

    @Query("SELECT * FROM bank_transactions where user_id = :userId order by id desc")
    public List<BankTransaction> getBankTransactionByUserId(int userId);

    @Query("SELECT * FROM bank_transactions ORDER BY id DESC LIMIT 1")
    public List<BankTransaction> getPreviousBankTransaction();
}
