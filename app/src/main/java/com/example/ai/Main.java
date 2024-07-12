package com.example.ai;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Main {

    private static final String TAG = "Main";

    ArrayList<String> breakfastInputs = new ArrayList<>();
    ArrayList<String> lunchInputs = new ArrayList<>();
    ArrayList<String> dinnerInputs = new ArrayList<>();
    ArrayList<String> snacksInputs = new ArrayList<>();
    Double calorie;

    void getInput(ArrayList<String> breakfast, ArrayList<String> lunch, ArrayList<String> dinner, ArrayList<String> snacks, Double c) {
        breakfastInputs = breakfast;
        lunchInputs = lunch;
        dinnerInputs = dinner;
        snacksInputs = snacks;
        calorie = c;

        Log.d("USER-INPUT", "Breakfast Inputs: " + breakfastInputs.toString());
        Log.d("USER-INPUT", "Lunch Inputs: " + lunchInputs.toString());
        Log.d("USER-INPUT", "Dinner Inputs: " + dinnerInputs.toString());
        Log.d("USER-INPUT", "Snacks Inputs: " + snacksInputs.toString());
        Log.d("USER-INPUT", "Calorie: " + calorie);

    }

    List<FoodItem>[] foodItems;

    void getOptimized() {
        double calorieLimit = calorie;

        initializeFoodItems(breakfastInputs, lunchInputs, dinnerInputs, snacksInputs);

        int numGenerations = 5000;
        int populationSize = 10;
        int numFoodItems = 4;
        double mutationRate = 0.05;

        // Initialize population randomly
        List<List<FoodItem>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateRandomPlan(numFoodItems));
        }

        // Evolution loop
        for (int generation = 0; generation < numGenerations; generation++) {
            double[] fitnessScores = new double[populationSize];

            // Evaluate fitness of each plan in the population
            for (int i = 0; i < populationSize; i++) {
                fitnessScores[i] = calculateFitness(population.get(i), calorieLimit);
            }

            // Select parents based on fitness scores and perform crossover and mutation
            List<List<FoodItem>> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                List<FoodItem>[] parents = selectParents(population, fitnessScores);
                List<FoodItem>[] children = crossover(parents[0], parents[1]);
                newPopulation.add(mutate(children[0], mutationRate));
                newPopulation.add(mutate(children[1], mutationRate));
            }

            // Clear the old population and replace it with new
            population.clear();
            population.addAll(newPopulation);

            // Print best plan in each generation
            double bestFitness = Double.NEGATIVE_INFINITY;
            List<FoodItem> bestPlan = null;
            for (List<FoodItem> plan : population) {
                double fitness = calculateFitness(plan, calorieLimit);
                if (fitness > bestFitness) {
                    bestFitness = fitness;
                    bestPlan = plan;
                }
            }

            // Log best plan and its fitness for this generation
            if (bestPlan != null) {
               // Log.d("GENERATION", "Generation " + generation + ": Best Plan - Fitness: " + bestFitness);
                for (FoodItem item : bestPlan) {
                   // Log.d("OPTIMIZED", item.name + " - Calories: " + item.calories);
                }
            }
        };
    }

    void initializeFoodItems(List<String> breakFastInputs, List<String> lunchInputs, List<String> dinnerInputs, List<String> snackInputs) {
        foodItems = new List[]{
                parseFoodItems(breakFastInputs),
                parseFoodItems(lunchInputs),
                parseFoodItems(dinnerInputs),
                parseFoodItems(snackInputs)
        };
    }

    List<FoodItem> parseFoodItems(List<String> inputs) {
        List<FoodItem> foodList = new ArrayList<>();
        for (String input : inputs) {
            String[] parts = input.split(",");
            if (parts.length == 7) {
                String name = parts[0].trim();
                int calories = Integer.parseInt(parts[1].trim());
                int sugar = Integer.parseInt(parts[2].trim());
                double saturatedFat = Double.parseDouble(parts[3].trim());
                int sodium = Integer.parseInt(parts[4].trim());
                int fiber = Integer.parseInt(parts[5].trim());
                int protein = Integer.parseInt(parts[6].trim());
                foodList.add(new FoodItem(name, calories, sugar, saturatedFat, sodium, fiber, protein));
            }
        }
        return foodList;
    }

    List<FoodItem> generateRandomPlan(int numFoodItems) {
        List<FoodItem> plan = new ArrayList<>();

        plan.add(randomChoice(foodItems[0]));
        plan.add(randomChoice(foodItems[1]));
        plan.add(randomChoice(foodItems[2]));
        plan.add(randomChoice(foodItems[3]));

        for (int i = 4; i < numFoodItems; i++) {
            List<FoodItem> allFoodItems = new ArrayList<>();
            for (List<FoodItem> itemList : foodItems) {
                allFoodItems.addAll(itemList);
            }
            FoodItem randomFoodItem;
            do {
                randomFoodItem = randomChoice(allFoodItems);
            } while (plan.contains(randomFoodItem));
            plan.add(randomFoodItem);
        }

        shuffle(plan);

        return plan;
    }

    FoodItem randomChoice(List<FoodItem> itemList) {
        if (itemList.isEmpty()) {
            throw new IllegalArgumentException("Cannot select from an empty list");
        }
        Random random = new Random();
        return itemList.get(random.nextInt(itemList.size()));
    }

    void shuffle(List<FoodItem> list) {
        Random random = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            FoodItem temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }

    double[] calculateNutrientsTotals(List<FoodItem> plan) {
        double[] nutrientsTotals = new double[6]; // Change to the correct number of nutrients
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

    double calculateFitness(List<FoodItem> plan, double calorieLimit) {
        double[] nutrientsTotals = calculateNutrientsTotals(plan);
        int totalCalories = (int) nutrientsTotals[0];
        if (totalCalories > calorieLimit) {
            return 0; // penalize plans exceeding calorie limit
        } else {
            // calculate nutriscore
            NutriscoreCalculator ns = new NutriscoreCalculator();
            int nutriscore = ns.calculateNutriscore(Arrays.copyOfRange(nutrientsTotals, 0, 5));
            // Higher fitness for lower nutriscore (more negative)
            return -nutriscore;
        }
    }

    List<FoodItem>[] selectParents(List<List<FoodItem>> population, double[] fitnessScores) {
        Random random = new Random();
        List<FoodItem> parent1 = population.get(randomChoiceIndex(fitnessScores));
        List<FoodItem> parent2 = population.get(randomChoiceIndex(fitnessScores));
        return new List[]{parent1, parent2};
    }

    int randomChoiceIndex(double[] fitnessScores) {
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

    List<FoodItem>[] crossover(List<FoodItem> parent1, List<FoodItem> parent2) {
        Random random = new Random();
        int crossoverPoint = random.nextInt(parent1.size());
        List<FoodItem> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
        child1.addAll(parent2.subList(crossoverPoint, parent2.size()));
        List<FoodItem> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));
        child2.addAll(parent1.subList(crossoverPoint, parent1.size()));
        return new List[]{new ArrayList<>(child1), new ArrayList<>(child2)};
    }

    List<FoodItem> mutate(List<FoodItem> plan, double mutationRate) {
        Random random = new Random();
        if (random.nextDouble() < mutationRate) {
            int mutateIndex = random.nextInt(plan.size());
            List<FoodItem> allFoodItems = new ArrayList<>();
            for (List<FoodItem> itemList : foodItems) {
                allFoodItems.addAll(itemList);
            }
            FoodItem randomFoodItem;
            do {
                randomFoodItem = randomChoice(allFoodItems);
            } while (plan.contains(randomFoodItem)); // Ensure no duplicates
            plan.set(mutateIndex, randomFoodItem);
        }
        return plan;
    }

    void logPlan(List<FoodItem> plan) {
        for (FoodItem meal : plan) {
            Log.d(TAG, meal.name + " - Calories: " + meal.calories);
        }
    }
}

class FoodItem {
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

class NutriscoreCalculator {

    int calculateNutriscore(double[] nutrients) {
        double[][] thresholds = {
                {0, 335, 670, 1005, 1340, 1675, 2010, 2345, 2680, 3015, Double.POSITIVE_INFINITY},
                {0, 4.5, 9, 13.5, 18, 22.5, 27, 31, 36, 40, Double.POSITIVE_INFINITY},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, Double.POSITIVE_INFINITY},
                {0, 90, 180, 270, 360, 450, 540, 630, 720, 810, Double.POSITIVE_INFINITY},
                {0, 0.7, 1.4, 2.1, 2.8, 3.5, Double.POSITIVE_INFINITY},
                {0, 1.6, 3.2, 4.8, 6.4, 8.0, Double.POSITIVE_INFINITY}
        };
        int[][] points = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {0, 1, 2, 3, 4, 5},
                {0, 1, 2, 3, 4, 5}
        };

        int badPoints = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < thresholds[i].length; j++) {
                if (nutrients[i] <= thresholds[i][j]) {
                    badPoints += points[i][j];
                    break;
                }
            }
        }

        int goodPoints = 0;
        for (int i = 4; i < nutrients.length; i++) {
            for (int j = 0; j < thresholds[i].length; j++) {
                if (nutrients[i] >= thresholds[i][j]) {
                    goodPoints += points[i][j];
                    break;
                }
            }
        }

        int nutriscore = badPoints - goodPoints;
        return nutriscore;
    }
}
