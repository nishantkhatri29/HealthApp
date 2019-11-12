package edu.ncuindia.healthapp;

public class ItemModal {
    public String Name;
    public int Portion_Default;
    public int Consumed;

    public int getConsumed() {
        return Consumed;
    }

    public void setConsumed(int consumed) {
        Consumed = consumed;
    }

    public String Unit;
    public float Calories;
    public float  Saturated_Fats;
    public String Image;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPortion_Default() {
        return Portion_Default;
    }

    public void setPortion_Default(int portion_Default) {
        Portion_Default = portion_Default;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }

    public float getSaturated_Fats() {
        return Saturated_Fats;
    }

    public void setSaturated_Fats(float saturated_Fats) {
        Saturated_Fats = saturated_Fats;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public String toString() {
        return "ItemModal{" +
                "Name='" + Name + '\'' +
                ", Portion_Default=" + Portion_Default +
                ", Consumed=" + Consumed +
                ", Unit='" + Unit + '\'' +
                ", Calories=" + Calories +
                ", Saturated_Fats=" + Saturated_Fats +
                ", Image='" + Image + '\'' +
                '}';
    }
}
