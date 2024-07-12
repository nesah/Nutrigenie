package com.example.ai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    LinearLayout breakfastSection, lunchSection, dinnerSection, snacksSection;
    LinearLayout breakfastContent, lunchContent, dinnerContent, snacksContent;
    EditText input_calorie;
    Button btn_breakfast, btn_lunch, btn_dinner, btn_snacks;
    Button btn_optimize;

    ArrayList<String> breakfastInputs = new ArrayList<>();
    ArrayList<String> lunchInputs = new ArrayList<>();
    ArrayList<String> dinnerInputs = new ArrayList<>();
    ArrayList<String> snacksInputs = new ArrayList<>();
    String calorie;

    ArrayList<String> bOutput = new ArrayList<>();
    ArrayList<String> lOutput = new ArrayList<>();
    ArrayList<String> dOutput = new ArrayList<>();
    ArrayList<String> sOutput = new ArrayList<>();
    double calorieValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        breakfastSection = findViewById(R.id.breakfastSection);
        lunchSection = findViewById(R.id.lunchSection);
        dinnerSection = findViewById(R.id.dinnerSection);
        snacksSection = findViewById(R.id.snacksSection);

        breakfastContent = findViewById(R.id.breakfastContent);
        lunchContent = findViewById(R.id.lunchContent);
        dinnerContent = findViewById(R.id.dinnerContent);
        snacksContent = findViewById(R.id.snacksContent);

        input_calorie = findViewById(R.id.input_calorie);

        btn_breakfast = findViewById(R.id.btn_breakfast);
        btn_lunch = findViewById(R.id.btn_lunch);
        btn_dinner = findViewById(R.id.btn_dinner);
        btn_snacks = findViewById(R.id.btn_snacks);

        btn_optimize = findViewById(R.id.btn_optimize);

        breakfastSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(breakfastContent);
            }
        });
        lunchSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(lunchContent);
            }
        });
        dinnerSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(dinnerContent);
            }
        });
        snacksSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(snacksContent);
            }
        });

        btn_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(breakfastContent);
            }
        });
        btn_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(lunchContent);
            }
        });
        btn_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(dinnerContent);
            }
        });
        btn_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(snacksContent);
            }
        });

        // button optimize functionality
        btn_optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all user input
                collectInput();

                // read json file
                ReaderJson reader = new ReaderJson(v.getContext());
                reader.readJsonFile("foods.json");

                // store json file contents
                List<ReaderJson.FoodItem> foodItems = reader.getFoodItems();

                // storage for data of input from json
                 bOutput = new ArrayList<>();
                 lOutput = new ArrayList<>();
                 dOutput = new ArrayList<>();
                 sOutput = new ArrayList<>();

                // get food data in json
                for (String keyword : breakfastInputs) {
                    List<ReaderJson.FoodItem> filteredItems = searchFoodItems(foodItems, keyword);
                    ReaderJson.FoodItem lowestCalorieItem = findItemWithLowestCalories(filteredItems);
                    if (lowestCalorieItem != null) {
                        String formattedItem = formatFoodItem(lowestCalorieItem);
                        bOutput.add(formattedItem);
                    }
                }
                for (String keyword : lunchInputs) {
                    List<ReaderJson.FoodItem> filteredItems = searchFoodItems(foodItems, keyword);
                    ReaderJson.FoodItem lowestCalorieItem = findItemWithLowestCalories(filteredItems);
                    if (lowestCalorieItem != null) {
                        String formattedItem = formatFoodItem(lowestCalorieItem);
                        lOutput.add(formattedItem);
                    }
                }
                for (String keyword : dinnerInputs) {
                    List<ReaderJson.FoodItem> filteredItems = searchFoodItems(foodItems, keyword);
                    ReaderJson.FoodItem lowestCalorieItem = findItemWithLowestCalories(filteredItems);
                    if (lowestCalorieItem != null) {
                        String formattedItem = formatFoodItem(lowestCalorieItem);
                        dOutput.add(formattedItem);
                    }
                }
                for (String keyword : snacksInputs) {
                    List<ReaderJson.FoodItem> filteredItems = searchFoodItems(foodItems, keyword);
                    ReaderJson.FoodItem lowestCalorieItem = findItemWithLowestCalories(filteredItems);
                    if (lowestCalorieItem != null) {
                        String formattedItem = formatFoodItem(lowestCalorieItem);
                        sOutput.add(formattedItem);
                    }
                }

                // get calorie input
                calorie = input_calorie.getText().toString();

                // check all input and process
                if(breakfastInputs.size() == 0 || lunchInputs.size() == 0 || dinnerInputs.size() == 0 || lunchInputs.size() == 0){
                    Toast.makeText(getApplicationContext(), "All section must have one meal!", Toast.LENGTH_SHORT).show();
                }
                else if(calorie.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Calorie input cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        calorieValue = Double.parseDouble(calorie);

                        if (calorieValue <= 0) {
                            Toast.makeText(getApplicationContext(), "Calorie input must be a positive number!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            new OptimizeTask().execute();
                        }
                    }
                    catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Invalid calorie input! Please enter a valid number!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    List<ReaderJson.FoodItem> searchFoodItems(List<ReaderJson.FoodItem> foodItems, String keyword) {
        List<ReaderJson.FoodItem> filteredItems = new ArrayList<>();
        for (ReaderJson.FoodItem item : foodItems) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    ReaderJson.FoodItem findItemWithLowestCalories(List<ReaderJson.FoodItem> foodItems) {
        if (foodItems.isEmpty()) {
            return null;
        }

        ReaderJson.FoodItem lowestCalorieItem = foodItems.get(0);
        for (ReaderJson.FoodItem item : foodItems) {
            if (item.getCalories() < lowestCalorieItem.getCalories()) {
                lowestCalorieItem = item;
            }
        }
        return lowestCalorieItem;
    }

    String formatFoodItem(ReaderJson.FoodItem item) {
        return String.format("%s, %d, %d, %.1f, %d, %d, %d",
                item.getName(), item.getCalories(), item.getSugar(), item.getSaturatedFat(),
                item.getSodium(), item.getFiber(), item.getProtein());
    }

    void addNewRow(final LinearLayout section) {
        final LinearLayout newRow = new LinearLayout(this);
        newRow.setOrientation(LinearLayout.HORIZONTAL);

        EditText newEditText = new EditText(this);
        newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        newEditText.setHint("Enter meal: ");

        Button removeButton = new Button(this);
        removeButton.setText("x");
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section.removeView(newRow);
            }
        });

        newRow.addView(newEditText);
        newRow.addView(removeButton);
        section.addView(newRow);
    }

    void toggleVisibility(LinearLayout sectionContent) {
        if (sectionContent.getVisibility() == View.VISIBLE) {
            sectionContent.setVisibility(View.GONE);
        } else {
            sectionContent.setVisibility(View.VISIBLE);
        }
    }

    void collectInput() {
        breakfastInputs.clear();
        lunchInputs.clear();
        dinnerInputs.clear();
        snacksInputs.clear();

        collectInputsFromSection(breakfastContent, breakfastInputs);
        collectInputsFromSection(lunchContent, lunchInputs);
        collectInputsFromSection(dinnerContent, dinnerInputs);
        collectInputsFromSection(snacksContent, snacksInputs);
    }

    void collectInputsFromSection(LinearLayout section, ArrayList<String> inputs) {
        int childCount = section.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = section.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) child;
                EditText editText = (EditText) row.getChildAt(0);
                String inputText = editText.getText().toString().trim();
                inputs.add(inputText);
            }
        }
    }

    class OptimizeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Main m = new Main();
            m.getInput(bOutput, lOutput, dOutput, sOutput, calorieValue);
            m.getOptimized();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Perform any UI updates here if needed
            Toast.makeText(getApplicationContext(), "Optimization complete!", Toast.LENGTH_SHORT).show();
        }
    }
}