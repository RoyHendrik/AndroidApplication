package com.pkg.to_day.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pkg.to_day.models.User;

@Dao
public interface UserDao {

    @Insert
    void register(User user);

    @Query("SELECT * from users where email=(:email) and password=(:password)")
    User login(String email, String password);

    @Query("SELECT EXISTS(SELECT * FROM users WHERE email = :email)")
    boolean exists(String email);

    @Query("SELECT * FROM users WHERE email=(:email)")
    User find(String email);
}
