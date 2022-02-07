package com.example.favdish.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="fave_dishes_table")
class FavDish(
   /* @ColumnInfo val image:String,*/
    @ColumnInfo (name="imageSource")val imageSource:String ,
    @ColumnInfo val title:String,
    @ColumnInfo val type:String,
    @ColumnInfo val category: String,
    @ColumnInfo val ingradients:String,
    @ColumnInfo(name= "cooking_time") val cookingTime:String,
    @ColumnInfo(name= "instructions") val directionToCook:String,
    @ColumnInfo(name= "favorite_dish") val favoritDish:Boolean =false,

    @PrimaryKey(autoGenerate = true)val id:Int =0
)