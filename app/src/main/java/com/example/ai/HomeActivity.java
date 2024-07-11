package com.example.ai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout breakfastSection;
    private LinearLayout lunchSection;
    private LinearLayout dinnerSection;
    private LinearLayout snacksSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        breakfastSection = findViewById(R.id.breakfastSection);
        lunchSection = findViewById(R.id.lunchSection);
        dinnerSection = findViewById(R.id.dinnerSection);
        snacksSection = findViewById(R.id.snacksSection);

        Button addBreakfastButton = findViewById(R.id.addBreakfastButton);
        Button addLunchButton = findViewById(R.id.addLunchButton);
        Button addDinnerButton = findViewById(R.id.addDinnerButton);
        Button addSnacksButton = findViewById(R.id.addSnacksButton);

        addBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(breakfastSection);
            }
        });

        addLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(lunchSection);
            }
        });

        addDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(dinnerSection);
            }
        });

        addSnacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(snacksSection);
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
        newEditText.setHint("Enter text");

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
}
