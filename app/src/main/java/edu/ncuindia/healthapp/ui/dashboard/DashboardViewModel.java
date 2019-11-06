package edu.ncuindia.healthapp.ui.dashboard;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import edu.ncuindia.healthapp.Database.MyDatabase;
import edu.ncuindia.healthapp.Database.UserModel;

public class DashboardViewModel extends AndroidViewModel {
    private final Application application;
    private static final String DATABASE_NAME = "MyDatabase";
    private MyDatabase database;
    private MutableLiveData<UserModel> data;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        database = Room.databaseBuilder(application, MyDatabase.class, DATABASE_NAME).build();
    }

    public LiveData<UserModel> getUser(String email) {
        if (data == null) {
            data = new MutableLiveData<UserModel>();
            loadUser(email);
        }
        return data;
    }

    public  boolean setUser(final UserModel userModel) {
        if(
            userModel.name.isEmpty() ||
            userModel.email.isEmpty() ||
            userModel.date.isEmpty() ||
            userModel.country.isEmpty() ||
            userModel.gender.isEmpty() ||
            userModel.height == 0 ||
            userModel.weight == 0 ||
            userModel.stepGoal <= 0 ||
            userModel.weightGoal <= 0
        ) return false;
        data = new MutableLiveData<UserModel>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if(database.userDao().findByEmail(userModel.email) != null)
                    database.userDao().update(userModel);
                else
                    database.userDao().insert(userModel);
                data.postValue(userModel);
            }
        });
        return true;
    }

    private void loadUser(final String email) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                UserModel tempUserModel = database.userDao().findByEmail(email);
                if(tempUserModel != null)
                    data.postValue(tempUserModel);
            }
        });
    }
}