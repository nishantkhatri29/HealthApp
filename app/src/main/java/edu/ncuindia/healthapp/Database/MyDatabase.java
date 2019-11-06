package edu.ncuindia.healthapp.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserModel.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDAO userDao();
}