package com.myapp.mycoffeestyle.photoCoffee

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.repository.RepositoryForDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ViewModelPhoto(private val database: DaoPhotoCoffee?) : ViewModel() {

    private val job = Job()
    private val myScope = CoroutineScope(Dispatchers.IO + job)

    // получаем список фото
    val listPhoto: LiveData<List<PhotoInDatabase>>
        get() = RepositoryForDatabase(database!!).listPhoto

    // удаляем фото
    fun deletePhoto(photo: PhotoInDatabase) {
        myScope.launch {
            RepositoryForDatabase(database!!).deletePhoto(photo)
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}

class PhotoViewModelFactory(
    private val database: DaoPhotoCoffee?
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelPhoto::class.java)) {
            return ViewModelPhoto(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}