package edu.ncuindia.healthapp.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserModel {

    @ColumnInfo
    public String name;

    @PrimaryKey
    @NonNull
    public String email;

    @ColumnInfo
    public String date;

    @ColumnInfo
    public String country;

    @ColumnInfo
    public String gender;

    @ColumnInfo
    public int height;

    @ColumnInfo
    public int weight;

    @ColumnInfo
    public Integer stepGoal;

    @ColumnInfo
    public Integer weightGoal;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public Integer getStepGoal() {
        return stepGoal;
    }

    public Integer getWeightGoal() {
        return weightGoal;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setStepGoal(Integer stepGoal) {
        this.stepGoal = stepGoal;
    }

    public void setWeightGoal(Integer weightGoal) {
        this.weightGoal = weightGoal;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", stepGoal=" + stepGoal +
                ", weightGoal=" + weightGoal +
                '}';
    }
}
