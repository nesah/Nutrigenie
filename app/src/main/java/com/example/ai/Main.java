package com.example.ai;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Main {

    ArrayList<String> breakfastInputs = new ArrayList<>();
    ArrayList<String> lunchInputs = new ArrayList<>();
    ArrayList<String> dinnerInputs = new ArrayList<>();
    ArrayList<String> snacksInputs = new ArrayList<>();
    String calorie;

    public void getInput(ArrayList<String> breakfast, ArrayList<String> lunch, ArrayList<String> dinner, ArrayList<String> snacks, String c) {
        Log.d("HomeActivity", "Calorie Input: " + c.toString());
        Log.d("HomeActivity", "Breakfast Inputs: " + breakfast.toString());
        Log.d("HomeActivity", "Lunch Inputs: " + lunch.toString());
        Log.d("HomeActivity", "Dinner Inputs: " + dinner.toString());
        Log.d("HomeActivity", "Snacks Inputs: " + snacks.toString());

        breakfastInputs = breakfast;
        lunchInputs = lunch;
        dinnerInputs = dinner;
        snacksInputs = snacks;
        calorie = c;
    }



}