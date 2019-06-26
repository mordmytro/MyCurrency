package com.example.mycurrency;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM USERS")
    List<User> getUser();

    @Query("SELECT * FROM USERS WHERE user_login LIKE :log")
    List<User>getUserPass(String log);
}
