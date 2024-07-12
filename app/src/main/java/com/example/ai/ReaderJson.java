package com.example.ai;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReaderJson{

    private List<FoodItem> foodItems;
    private Context context;

    public ReaderJson(Context context){
        this.context = context;
        this.foodItems = new ArrayList<>();
    }

    public void readJsonFile(String filename){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = context.getAssets().open(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                int calories = jsonObject.getInt("calories");
                int sugar = jsonObject.getInt("sugar");
                double saturatedFat = jsonObject.getDouble("saturatedFat");
                int sodium = jsonObject.getInt("sodium");
                int fiber = jsonObject.getInt("fiber");
                int protein = jsonObject.getInt("protein");

                FoodItem foodItem = new FoodItem(name, calories, sugar, saturatedFat, sodium, fiber, protein);
                foodItems.add(foodItem);
            }

            inputStream.close();
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    public List<FoodItem> getFoodItems(){
        return foodItems;
    }

    public static class FoodItem{
        private String name;
        private int calories;
        private int sugar;
        private double saturatedFat;
        private int sodium;
        private int fiber;
        private int protein;

        public FoodItem(String name, int calories, int sugar, double saturatedFat, int sodium, int fiber, int protein){
            this.name = name;
            this.calories = calories;
            this.sugar = sugar;
            this.saturatedFat = saturatedFat;
            this.sodium = sodium;
            this.fiber = fiber;
            this.protein = protein;
        }

        public String getName(){
            return name;
        }

        public int getCalories(){
            return calories;
        }

        public int getSugar(){
            return sugar;
        }

        public double getSaturatedFat(){
            return saturatedFat;
        }

        public int getSodium(){
            return sodium;
        }

        public int getFiber(){
            return fiber;
        }

        public int getProtein(){
            return protein;
        }
    }
}