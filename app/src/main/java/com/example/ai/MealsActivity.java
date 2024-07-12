package com.example.ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class MealsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        Intent intent = getIntent();
        if (intent != null) {
            List<FoodItem>[] bestPlansArray = (List<FoodItem>[]) intent.getSerializableExtra("best_plans");

            if (bestPlansArray != null) {
                List<String> mealRecommendations = new ArrayList<>();
                Map<String, List<FoodItem>> mealsMap = new LinkedHashMap<>();
                Map<String, Integer> totalCaloriesMap = new LinkedHashMap<>();
                Set<String> uniqueMealsSet = new HashSet<>();

                for (int i = bestPlansArray.length - 1; i >= 0; i--) {
                    if (bestPlansArray[i] != null && !bestPlansArray[i].isEmpty()) {
                        StringBuilder mealNames = new StringBuilder();
                        for (FoodItem item : bestPlansArray[i]) {
                            mealNames.append(item.getName()).append(",");
                        }
                        String mealNamesStr = mealNames.toString();

                        if (!uniqueMealsSet.contains(mealNamesStr)) {
                            uniqueMealsSet.add(mealNamesStr);

                            String recommendation = "Meal Recommendation " + (bestPlansArray.length - i);
                            mealRecommendations.add(recommendation);
                            mealsMap.put(recommendation, bestPlansArray[i]);

                            int totalCalories = 0;
                            for (FoodItem item : bestPlansArray[i]) {
                                totalCalories += item.getCalories();
                            }
                            totalCaloriesMap.put(recommendation, totalCalories);
                        }
                    }
                }

                if (!mealRecommendations.isEmpty()) {
                    ExpandableListView expandableListView = findViewById(R.id.expandableListView);
                    MealsAdapter adapter = new MealsAdapter(this, mealRecommendations, mealsMap, totalCaloriesMap);
                    expandableListView.setAdapter(adapter);
                }
            }
        }
    }
}