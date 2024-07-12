package com.example.ai;

import java.io.Serializable;

public class FoodItem implements Serializable{

    String name;
    int calories;
    int sugar;
    double saturatedFat;
    int sodium;
    int fiber;
    int protein;

    FoodItem(String name, int calories, int sugar, double saturatedFat, int sodium, int fiber, int protein){
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