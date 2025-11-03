package com.example.restaurantorderingapp;

import android.content.Context;
import android.content.res.Resources;

public class ResourceHelper {
    
    public static int getFoodImageResource(Context context, int foodNumber) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        
        // Try various resource name patterns for images in food/ subdirectory
        // Android converts "food/food 1.jpeg" to "food_food_1" (with underscore and no extension)
        String[] patterns = {
            "food_food_" + foodNumber,           // food/food 1.jpeg -> food_food_1
            "food_food" + foodNumber,            // food/food1.jpeg -> food_food1
            "food_food_" + String.format("%02d", foodNumber),  // food/food 01.jpeg -> food_food_01
            "food_" + foodNumber,                // food_1 (if moved to root)
            "food" + foodNumber                  // food1 (if moved to root)
        };
        
        for (String pattern : patterns) {
            int resId = resources.getIdentifier(pattern.toLowerCase(), "drawable", packageName);
            if (resId != 0) {
                return resId;
            }
        }
        
        // Try direct file name patterns (handling spaces)
        String[] directPatterns = {
            "food_food_" + foodNumber,
            "food_food" + String.format("%02d", foodNumber)
        };
        
        for (String pattern : directPatterns) {
            int resId = resources.getIdentifier(pattern, "drawable", packageName);
            if (resId != 0) {
                return resId;
            }
        }
        
        return 0; // Return 0 if not found
    }
    
    public static int getDrinkImageResource(Context context, int drinkNumber) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        
        // Try various resource name patterns for images in drinks/ subdirectory
        String[] patterns = {
            "drinks_drink" + drinkNumber,        // drinks/drink1.jpeg -> drinks_drink1
            "drink" + drinkNumber,               // drink1 (if moved to root)
            "drink_" + drinkNumber,              // drink_1 (alternative)
            "drinks_drink_" + drinkNumber        // drinks/drink_1.jpeg -> drinks_drink_1
        };
        
        for (String pattern : patterns) {
            int resId = resources.getIdentifier(pattern.toLowerCase(), "drawable", packageName);
            if (resId != 0) {
                return resId;
            }
        }
        
        // Try direct patterns
        int resId = resources.getIdentifier("drinks_drink" + drinkNumber, "drawable", packageName);
        if (resId != 0) {
            return resId;
        }
        
        return 0; // Return 0 if not found
    }
}

