package com.example.favdish.application

import android.app.Application
import com.example.favdish.model.database.FavDishRepository
import com.example.favdish.model.database.FavDishRoomDataBase

class FavDishApplication :Application() {

    private val database by lazy {

        FavDishRoomDataBase.getDatabase(this@FavDishApplication)
    }

    val repository by lazy {
        FavDishRepository(database.favDishDao())
    }
}