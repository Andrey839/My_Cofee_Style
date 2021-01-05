package com.myapp.mycoffeestyle.repository

import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryForDatabase(private val database: DaoPhotoCoffee) {

    val listPhoto = database.daoCoffee.queryPhoto()

    suspend fun deletePhoto(photo: PhotoInDatabase){
        withContext(Dispatchers.IO){
            database.daoCoffee.deleteCoffee(photo)
        }

    }

    suspend fun insertPhoto(photo: PhotoInDatabase){
        withContext(Dispatchers.IO){
            database.daoCoffee.insertDataCoffee(photo)
        }
    }
}