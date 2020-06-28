package com.example.zapimini.daos;

import com.example.zapimini.data.Business;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BusinessDao {
    @Query("select * from business order by id desc")
    public List<Business> getBusiness();

    @Insert
    public Long insertBusiness(Business business);

    @Update
    public void updateBusiness(Business business);

    @Delete
    public void deleteBusiness(Business business);

    @Query("select * from business order by id desc limit 1")
    public List<Business> getLastInsertedBusiness();

    @Query("select * from business where user_id = :userId")
    public List<Business> getBusinessByUserId(int userId);
}
