package com.example.favdish.model.database

import com.example.favdish.model.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

   suspend fun insertFavDish(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList : Flow<List<FavDish>> =favDishDao.getAllDishesList()
}