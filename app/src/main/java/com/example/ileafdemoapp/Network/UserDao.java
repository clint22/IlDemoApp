package com.example.ileafdemoapp.Network;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Clint on 10/2/18.
 */

@Dao
public interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("select * from user")
    public List<User> getAllUser();

    @Query("delete from user")
    void removeAllUsers();
}
