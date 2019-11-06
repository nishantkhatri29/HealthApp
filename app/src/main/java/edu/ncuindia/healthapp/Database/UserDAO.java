package edu.ncuindia.healthapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM usermodel")
    List<UserModel> getAll();

    @Query("SELECT * FROM usermodel WHERE email LIKE :email LIMIT 1")
    UserModel findByEmail(String email);

    @Insert
    void insert(UserModel userModel);

    @Update
    void update(UserModel userModel);

    @Delete
    void delete(UserModel userModel);
}
