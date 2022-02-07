package com.example.favdish.util

object Constants {
    
    const val DISH_TYPE:String="DishType"
    const val DISH_CATEGORY:String="DishCategory"
    const val DISH_COOKING_TIME:String="DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL:String="Local"
    const val DISH_IMAGE_SOURCE_ONLINE:String="Online"

    fun dishTypes():ArrayList<String>{
        val arrayList = ArrayList<String>()
        arrayList.add("breakfast")
        arrayList.add("lunch")
        arrayList.add("snacks")
        arrayList.add("dinner")
        arrayList.add("salad")
        arrayList.add("side dish")
        arrayList.add("dessert")
        arrayList.add("other")

        return arrayList
    }

    fun dishCategory():ArrayList<String>{
        val category=ArrayList<String>()
        category.add("Pizza")
        category.add("BBQ")
        category.add("Bakery")
        category.add("Burger")
        category.add("Cafe")
        category.add("Chicken")
        category.add("Dessert")
        category.add("Drinks")
        category.add("Hot Dogs")
        category.add("Juices")
        category.add("Sandwich")
        category.add("Tea & Coffee")
        category.add("Other")
        return category
    }

    fun dishCookingTime():ArrayList<String>{
        val time=ArrayList<String>()
        time.add("10")
        time.add("20")
        time.add("30")
        time.add("40")
        time.add("50")
        time.add("60")
        time.add("70")
        time.add("80")
        time.add("90")
        time.add("100")
        time.add("110")
        time.add("120")
        time.add("130")
        return time
    }
}