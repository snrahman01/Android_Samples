package com.example.mykitchen.data

import android.content.res.Resources
import com.example.mykitchen.R

object FoodItemList {
    private var foodItems: List<FoodItem> = listOf(
        FoodItem(
            1,
            "Backed Salmon",
            12.4F,
            2000,
            R.drawable.baked_samon,
            R.string.burger_description

        ),
        FoodItem(
            2,
            "Chef Finishing Salmon",
            20.4F,
            200,
            R.drawable.chef_finishing_salmon,
            R.string.chef_salmon

        ),
        FoodItem(
            3,
            "Duck Leg Mashed Potatoes",
            15.6F,
            350,
            R.drawable.duck_leg_mashed_potatoes,
            R.string.steak_marbled

        ),
        FoodItem(
            4,
            "Gourmet Tasty Steak Burgers",
            8.9F,
            100,
            R.drawable.gourmet_tasty_steak_burgers,
            R.string.burger_description
        ),
        FoodItem(
            5,
            "Grilled Fried Sliced Marbled Meat",
            15.4F,
            175,
            R.drawable.grilled_fried_sliced_marbled_meat,
            R.string.steak_marbled
        ),
        FoodItem(
            6,
            "Grilled Meat Skewers Shish Kebab",
            12.4F,
            100,
            R.drawable.grilled_meat_skewers_shish_kebab,
            R.string.chef_salmon
        )
    )

    fun getItems(): List<FoodItem> = foodItems

    fun getItem(id: Int): FoodItem = foodItems[id]
}