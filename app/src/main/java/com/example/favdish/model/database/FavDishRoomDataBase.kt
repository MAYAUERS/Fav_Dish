package com.example.favdish.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdish.model.FavDish

@Database(entities = [FavDish::class],version = 1)
abstract class FavDishRoomDataBase:RoomDatabase() {

    abstract fun favDishDao():FavDishDao

         companion object{
             @Volatile
             private var INSTANCE:FavDishRoomDataBase? = null

             fun getDatabase(context: Context): FavDishRoomDataBase{

                 return (INSTANCE?: synchronized(this){
                     val instance=Room.databaseBuilder(
                         context.applicationContext,
                         FavDishRoomDataBase::class.java,
                         "fav_dish_database"
                     ).build()
                     INSTANCE =instance
                     instance
                 })
             }
         }

}