package com.example.ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class MealsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> mealRecommendations;
    private Map<String, List<FoodItem>> mealsMap;
    private Map<String, Integer> totalCaloriesMap;

    public MealsAdapter(Context context, List<String> mealRecommendations, Map<String, List<FoodItem>> mealsMap, Map<String, Integer> totalCaloriesMap) {
        this.context = context;
        this.mealRecommendations = mealRecommendations;
        this.mealsMap = mealsMap;
        this.totalCaloriesMap = totalCaloriesMap;
    }

    @Override
    public int getGroupCount() {
        return mealRecommendations.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String recommendation = mealRecommendations.get(groupPosition);
        List<FoodItem> items = mealsMap.get(recommendation);
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mealRecommendations.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String recommendation = mealRecommendations.get(groupPosition);
        return mealsMap.get(recommendation).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String recommendation = (String) getGroup(groupPosition);
        int totalCalories = totalCaloriesMap.get(recommendation);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView textViewGroup = convertView.findViewById(R.id.textViewGroup);
        TextView textViewTotalCalories = convertView.findViewById(R.id.textViewTotalCalories);

        textViewGroup.setText(recommendation);
        textViewTotalCalories.setText(totalCalories + " calories");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FoodItem meal = (FoodItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView textViewMealName = convertView.findViewById(R.id.textViewMealName);
        TextView textViewCalories = convertView.findViewById(R.id.textViewCalories);

        textViewMealName.setText(meal.getName());
        textViewCalories.setText(meal.getCalories() + " calories");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}