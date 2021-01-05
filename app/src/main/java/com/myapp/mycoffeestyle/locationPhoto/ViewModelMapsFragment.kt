package com.myapp.mycoffeestyle.locationPhoto

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.repository.RepositoryForDatabase
import com.myapp.mycoffeestyle.ui.main.PageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ViewModelMapsFragment(private val database: DaoPhotoCoffee): ViewModel() {

    val listPhotoLocation: LiveData<List<PhotoInDatabase>>
    get() = RepositoryForDatabase(database).listPhoto
}

class MapsViewModelFactory(
    private val database: DaoPhotoCoffee
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMapsFragment::class.java)) {
            return ViewModelMapsFragment(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}