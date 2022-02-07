package com.example.favdish.viewModel

import androidx.lifecycle.*
import com.example.favdish.model.FavDish
import com.example.favdish.model.database.FavDishRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavDishViewModel(private val repository:FavDishRepository) : ViewModel() {

    fun insert(dish:FavDish)=viewModelScope.launch {
        repository.insertFavDish(dish)
    }

    val allDisgesList:LiveData<List<FavDish>> =repository.allDishesList.asLiveData()
}

class FavDishViewModelFactory(private val repository: FavDishRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){

            return FavDishViewModel(repository) as  T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}