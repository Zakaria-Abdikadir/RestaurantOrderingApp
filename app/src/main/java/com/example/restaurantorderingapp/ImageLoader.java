package com.example.restaurantorderingapp;

import android.content.Context;
import android.content.res.Resources;

public class ImageLoader {
    
    public static void loadFoodImage(Context context, android.widget.ImageView imageView, int foodNumber) {
        int resId = getFoodDrawableResource(context, foodNumber);
        if (resId != 0) {
            imageView.setImageResource(resId);
        } else {
            // Fallback to placeholder
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }
    
    public static void loadDrinkImage(Context context, android.widget.ImageView imageView, int drinkNumber) {
        int resId = getDrinkDrawableResource(context, drinkNumber);
        if (resId != 0) {
            imageView.setImageResource(resId);
        } else {
            // Fallback to placeholder
            imageView.setImageResource(android.R.drawable.ic_menu_always_landscape_portrait);
        }
    }
    
    private static int getFoodDrawableResource(Context context, int foodNumber) {
        String packageName = context.getPackageName();
        Resources resources = context.getResources();
        
        // For files in drawable/food/ folder: "food/food 1.jpeg" becomes "food_food_1"
        // Try comprehensive list of patterns
        String[] patterns = {
            "food_food_" + foodNumber,                    // food/food 1.jpeg -> food_food_1
            "food_food" + foodNumber,                     // food/food1.jpeg -> food_food1
            "food_food_" + String.format("%02d", foodNumber), // food/food 01.jpeg
            "food_food" + String.format("%02d", foodNumber),
            "food_" + foodNumber,                         // food_1 (if in root)
            "food" + foodNumber                           // food1 (if in root)
        };
        
        for (String pattern : patterns) {
            int resId = resources.getIdentifier(pattern, "drawable", packageName);
            if (resId != 0) return resId;
        }
        
        return 0;
    }
    
    private static int getDrinkDrawableResource(Context context, int drinkNumber) {
        String packageName = context.getPackageName();
        Resources resources = context.getResources();
        
        // For files in drawable/drinks/ folder: "drinks/drink1.jpeg" becomes "drinks_drink1"
        String[] patterns = {
            "drinks_drink" + drinkNumber,                 // drinks/drink1.jpeg -> drinks_drink1
            "drink" + drinkNumber,                        // drink1 (if in root)
            "drink_" + drinkNumber,                       // drink_1
            "drinks_drink_" + drinkNumber                 // drinks/drink_1
        };
        
        for (String pattern : patterns) {
            int resId = resources.getIdentifier(pattern, "drawable", packageName);
            if (resId != 0) return resId;
        }
        
        return 0;
    }
}
