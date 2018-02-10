package com.example.ileafdemoapp.Network;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Clint on 10/2/18.
 */

@Entity
public class User {

    @PrimaryKey
    public final int id;
    public String first_name;
    public String last_name;
    public String email;
    public String date_of_birth;
    public int marital_status;
    public int sex;
    public String location;

    public User(int id, String first_name, String last_name, String email, String date_of_birth, int marital_status, int sex, String location) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.marital_status = marital_status;
        this.sex = sex;
        this.location = location;
    }
}
