package com.example.ai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    LinearLayout breakfastSection;
    LinearLayout lunchSection;
    LinearLayout dinnerSection;
    LinearLayout snacksSection;

    LinearLayout breakfastContent;
    LinearLayout lunchContent;
    LinearLayout dinnerContent;
    LinearLayout snacksContent;

    Button btn_breakfast;
    Button btn_lunch;
    Button btn_dinner;
    Button btn_snacks;

    EditText input_calorie;

    Button btn_optimize;

    ArrayList<String> breakfastInputs = new ArrayList<>();
    ArrayList<String> lunchInputs = new ArrayList<>();
    ArrayList<String> dinnerInputs = new ArrayList<>();
    ArrayList<String> snacksInputs = new ArrayList<>();
    String calorie;

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

        btn_breakfast = findViewById(R.id.btn_breakfast);
        btn_lunch = findViewById(R.id.btn_lunch);
        btn_dinner = findViewById(R.id.btn_dinner);
        btn_snacks = findViewById(R.id.btn_snacks);

        input_calorie = findViewById(R.id.input_calorie);
        btn_optimize = findViewById(R.id.btn_optimize);

        // Collect input of user
        btn_optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectInput();
                Main m = new Main();
                calorie = input_calorie.getText().toString();
                m.getInput(breakfastInputs, lunchInputs, dinnerInputs, snacksInputs, calorie);
            }
        });

        // Visibility of sections
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

        // Add text fields per section
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
    }

    private void addNewRow(final LinearLayout section) {
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

    private void toggleVisibility(LinearLayout sectionContent) {
        if (sectionContent.getVisibility() == View.VISIBLE) {
            sectionContent.setVisibility(View.GONE);
        } else {
            sectionContent.setVisibility(View.VISIBLE);
        }
    }

    private void collectInput() {
        breakfastInputs.clear();
        lunchInputs.clear();
        dinnerInputs.clear();
        snacksInputs.clear();

        collectInputsFromSection(breakfastContent, breakfastInputs);
        collectInputsFromSection(lunchContent, lunchInputs);
        collectInputsFromSection(dinnerContent, dinnerInputs);
        collectInputsFromSection(snacksContent, snacksInputs);
    }

    private void collectInputsFromSection(LinearLayout section, ArrayList<String> inputs) {
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
}