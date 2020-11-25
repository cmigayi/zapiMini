package com.example.zapimini.daos;

import com.example.zapimini.data.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("select * from users")
    public List<User> getUsers();

    @Insert
    public Long createUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("select * from users where id=:userId")
    public List<User> getUserById(int userId);

    @Query("select * from users order by id desc limit 1 ")
    public List<User> getLastInsertedUser();

    @Query("select * from users where username=:username and password=:password")
    public List<User> getAuthUser(String username, String password);
}
