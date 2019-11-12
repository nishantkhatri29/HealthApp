package edu.ncuindia.healthapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private MutableLiveData<List<ItemModal>> data;
    private MutableLiveData<Float> total;
    private Application application;
    public FoodViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<ItemModal>> getItems() {
        if (data == null) {
            data = new MutableLiveData<List<ItemModal>>();
            loadUsers();
        }
        return data;
    }
    public LiveData<Float> getTotal(){
        total = new MutableLiveData<Float>();
        float t = 0;
        for(int i=0;  i<data.getValue().size(); i++){
            t += (data.getValue().get(i).getCalories() * data.getValue().get(i).getConsumed());
        }
        total.setValue(t);
        return total;
    }

    private void loadUsers() {
        String json = null;
        InputStream is;
        try {
            is = this.application.getAssets().open("calorie.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            List<ItemModal> itemList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ItemModal item = new ItemModal();
                item.setName(obj.getString("Name"));
                item.setPortion_Default(obj.getInt("Portion_Default"));
                item.setConsumed(obj.getInt("Consumed"));
                item.setUnit(obj.getString("Unit"));
                item.setCalories((float) obj.getDouble("Calories"));
                item.setSaturated_Fats((float) obj.getDouble("Saturated_Fats"));
                item.setImage(obj.getString("Image"));
                itemList.add(item);
            }
            data.setValue(itemList);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            data.setValue(null);
        }
    }
}