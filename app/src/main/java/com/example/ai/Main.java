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

    // food items
    static List<FoodItem>[] foodItems = new List[]{
        new ArrayList<FoodItem>() {{
            add(new FoodItem("Oatmeal", 150, 1, 0.5, 75, 3, 5));
            add(new FoodItem("Berries", 50, 3, 0, 10, 2, 1));
            add(new FoodItem("Greek Yogurt", 100, 4, 1.5, 50, 1, 9));
            add(new FoodItem("Granola", 100, 2, 0.5, 25, 1, 2));
            add(new FoodItem("Scrambled Eggs", 200, 1, 2, 100, 0, 13));
            add(new FoodItem("Toast", 50, 1, 1, 100, 1, 3));
        }},
        new ArrayList<FoodItem>() {{
            add(new FoodItem("Grilled Chicken Breast", 200, 0, 1, 150, 0, 25));
            add(new FoodItem("Salad Mix", 50, 2, 0, 50, 3, 1));
            add(new FoodItem("Vegetarian Wrap", 300, 3, 1, 200, 5, 10));
            add(new FoodItem("Quinoa", 150, 1, 0, 50, 2, 5));
            add(new FoodItem("Roasted Veggies", 100, 2, 0.5, 100, 4, 3));
        }},
        new ArrayList<FoodItem>() {{
            add(new FoodItem("Salmon Fillet", 250, 0, 2, 200, 1, 22));
            add(new FoodItem("Brown Rice", 150, 0, 0, 0, 2, 3));
            add(new FoodItem("Pasta", 200, 2, 1, 100, 1, 7));
            add(new FoodItem("Marinara Sauce", 100, 5, 0.5, 150, 2, 1));
            add(new FoodItem("Tofu", 150, 1, 0.5, 75, 1, 10));
            add(new FoodItem("Stir-Fry Vegetables", 100, 2, 0, 50, 3, 2));
        }},
        new ArrayList<FoodItem>() {{
            add(new FoodItem("Mixed Nuts", 200, 2, 3, 150, 4, 10));
            add(new FoodItem("Fruit Smoothie", 150, 10, 1, 100, 3, 5));
            add(new FoodItem("Cheese and Crackers", 250, 3, 4, 200, 2, 8));
        }}
    };

    // food item class
    static class FoodItem {
        String name;
        int calories;
        int sugar;
        double saturatedFat;
        int sodium;
        int fiber;
        int protein;

        FoodItem(String name, int calories, int sugar, double saturatedFat, int sodium, int fiber, int protein) {
            this.name = name;
            this.calories = calories;
            this.sugar = sugar;
            this.saturatedFat = saturatedFat;
            this.sodium = sodium;
            this.fiber = fiber;
            this.protein = protein;
        }
    }

    // list of nutrients to consider
    static String[] nutrientsList = {"calories", "sugar", "saturatedFat", "sodium", "fiber", "protein"};

    static List<FoodItem> generateRandomPlan(int numFoodItems) {
        List<FoodItem> plan = new ArrayList<>();

        // ensure at least one meal from each category
        plan.add(randomChoice(foodItems[0]));
        plan.add(randomChoice(foodItems[1]));
        plan.add(randomChoice(foodItems[2]));
        plan.add(randomChoice(foodItems[3]));

        // Select additional food items randomly
        for (int i = 4; i < numFoodItems; i++) {
            List<FoodItem> allFoodItems = new ArrayList<>();
            for (List<FoodItem> itemList : foodItems) {
                allFoodItems.addAll(itemList);
            }
            plan.add(randomChoice(allFoodItems));
        }

        // shuffle for randomness
        shuffle(plan);

        return plan;
    }

    static FoodItem randomChoice(List<FoodItem> itemList) {
        Random random = new Random();
        return itemList.get(random.nextInt(itemList.size()));
    }

    static void shuffle(List<FoodItem> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            FoodItem temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }

    static double[] calculateNutrientsTotals(List<FoodItem> plan) {
        double[] nutrientsTotals = new double[nutrientsList.length];
        for (FoodItem meal : plan) {
            nutrientsTotals[0] += meal.calories;
            nutrientsTotals[1] += meal.sugar;
            nutrientsTotals[2] += meal.saturatedFat;
            nutrientsTotals[3] += meal.sodium;
            nutrientsTotals[4] += meal.fiber;
            nutrientsTotals[5] += meal.protein;
        }
        return nutrientsTotals;
    }

    static double calculateFitness(List<FoodItem> plan, int calorieLimit) {
        double[] nutrientsTotals = calculateNutrientsTotals(plan);
        int totalCalories = (int) nutrientsTotals[0];
        if (totalCalories > calorieLimit) {
            return 0; // penalize plans exceeding calorie limit
        } else {
            // calculate nutriscore
            int nutriscore = NutriscoreCalculator.calculateNutriscore(Arrays.copyOfRange(nutrientsTotals, 0, 5));
            // Higher fitness for lower nutriscore (more negative)
            return -nutriscore;
        }
    }

    static List<FoodItem>[] selectParents(List<List<FoodItem>> population, double[] fitnessScores) {
        Random random = new Random();
        List<FoodItem> parent1 = population.get(randomChoiceIndex(fitnessScores));
        List<FoodItem> parent2 = population.get(randomChoiceIndex(fitnessScores));
        return new List[]{parent1, parent2};
    }

    static int randomChoiceIndex(double[] fitnessScores) {
        Random random = new Random();
        double totalFitness = 0;
        for (double fitness : fitnessScores) {
            totalFitness += fitness;
        }
        double randomValue = random.nextDouble() * totalFitness;
        for (int i = 0; i < fitnessScores.length; i++) {
            randomValue -= fitnessScores[i];
            if (randomValue <= 0) {
                return i;
            }
        }
        return fitnessScores.length - 1;
    }

    static List<FoodItem>[] crossover(List<FoodItem> parent1, List<FoodItem> parent2) {
        Random random = new Random();
        int crossoverPoint = random.nextInt(parent1.size());
        List<FoodItem> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
        child1.addAll(parent2.subList(crossoverPoint, parent2.size()));
        List<FoodItem> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));
        child2.addAll(parent1.subList(crossoverPoint, parent1.size()));
        return new List[]{new ArrayList<>(child1), new ArrayList<>(child2)};
    }

    static List<FoodItem> mutate(List<FoodItem> plan, double mutationRate) {
        Random random = new Random();
        if (random.nextDouble() < mutationRate) {
            int mutateIndex = random.nextInt(plan.size());
            List<FoodItem> allFoodItems = new ArrayList<>();
            for (List<FoodItem> itemList : foodItems) {
                allFoodItems.addAll(itemList);
            }
            plan.set(mutateIndex, randomChoice(allFoodItems));
        }
        return plan;
    }

    static void printPlan(List<FoodItem> plan) {
        for (FoodItem meal : plan) {
            System.out.println(meal.name + " - Calories: " + meal.calories);
        }
    }

    public static void main(String[] args) {
        int numGenerations = 10000;
        int populationSize = 50;
        int numFoodItems = 6;
        int calorieLimit = 2000;
        double mutationRate = 0.05;

        // initialize population randomly
        List<List<FoodItem>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateRandomPlan(numFoodItems));
        }

        // evolution loop

    for (int generation = 0; generation < numGenerations; generation++) {
        double[] fitnessScores = new double[populationSize];

        // evaluate fitness of each plan in the population
        for (int i = 0; i < populationSize; i++) {
            fitnessScores[i] = calculateFitness(population.get(i), calorieLimit);
        }

        // select parents based on fitness scores and perform crossover and mutation
        List<List<FoodItem>> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<FoodItem>[] parents = selectParents(population, fitnessScores);
            List<FoodItem>[] children = crossover(parents[0], parents[1]);
            newPopulation.add(mutate(children[0], mutationRate));
            newPopulation.add(mutate(children[1], mutationRate));
        }

        // clear the old population and replace it with new
        population.clear();
        population.addAll(newPopulation);

        // print best plan in each generation
        double bestFitness = Double.NEGATIVE_INFINITY;
        List<FoodItem> bestPlan = null;
        for (List<FoodItem> plan : population) {
            double fitness = calculateFitness(plan, calorieLimit);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPlan = plan;
            }
        }
        System.out.println("Generation " + (generation + 1) + ", Best Fitness: " + bestFitness);
        printPlan(bestPlan);
        System.out.println();
    }
    }
}


// nutriscore calculator class
class NutriscoreCalculator {

    static int calculateNutriscore(double[] nutrients) {
        // define thresholds and points arrays/lists
        double[][] thresholds = {
                {0, 335, 670, 1005, 1340, 1675, 2010, 2345, 2680, 3015, Double.POSITIVE_INFINITY}, // calories
                {0, 4.5, 9, 13.5, 18, 22.5, 27, 31, 36, 40, Double.POSITIVE_INFINITY},             // sugar
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, Double.POSITIVE_INFINITY},                         // saturated_fat
                {0, 90, 180, 270, 360, 450, 540, 630, 720, 810, Double.POSITIVE_INFINITY},        // sodium
                {0, 0.7, 1.4, 2.1, 2.8, 3.5, Double.POSITIVE_INFINITY},                           // fiber
                {0, 1.6, 3.2, 4.8, 6.4, 8.0, Double.POSITIVE_INFINITY}                             // protein
        };
        int[][] points = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},   // calories
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},   // sugar
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},   // saturated_fat
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},   // sodium
                {0, 1, 2, 3, 4, 5},                  // fiber
                {0, 1, 2, 3, 4, 5}   // protein
        };

        // calculate bad points
        int badPoints = 0;
        for (int i = 0; i < 4; i++) { // calories, sugar, saturated_fat, sodium
            for (int j = 0; j < thresholds[i].length; j++) {
                if (nutrients[i] <= thresholds[i][j]) {
                    badPoints += points[i][j];
                    break;
                }
            }
        }

        // calculate good points
        int goodPoints = 0;
        for (int i = 4; i < nutrients.length; i++) { // fiber, protein
            for (int j = 0; j < thresholds[i].length; j++) {
                if (nutrients[i] >= thresholds[i][j]) {
                    goodPoints += points[i][j];
                    break;
                }
            }
        }

        // calculate nutriscore
        int nutriscore = badPoints - goodPoints;
        return nutriscore;
    }
}