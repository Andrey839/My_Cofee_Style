package com.myapp.mycoffeestyle

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.repository.RepositoryForDatabase
import com.myapp.mycoffeestyle.uttil.toMyData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ViewModelActivity(val context: Context, database: DaoPhotoCoffee) : ViewModel() {

    private val job = Job()
    private val myScope = CoroutineScope(Dispatchers.IO + job)

    val avatarLive = MutableLiveData<String>()
    val nameLive = MutableLiveData<String>()
    var latitude = ""
    var longitude = ""

    private val repository = RepositoryForDatabase(database)

    private val _currentFilePath = MutableLiveData<String>()
    val currentFilePath: LiveData<String>
        get() = _currentFilePath

    fun setAvatarAndName(avatar: String?, name: String?) {
        avatarLive.value = avatar
        nameLive.value = name
    }

    // создание файла для фото и имени
    fun createImageFile(bitmap: Bitmap) {
        myScope.launch {
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            try {
                val file = File.createTempFile("JPEG_", "jpg", storageDir)
                Log.e("tyi", "${file.absoluteFile}")
                _currentFilePath.postValue(file.absolutePath)
                val fileOut = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOut)
                fileOut.close()
            } catch (e: Exception) {
                Log.e("tyi", "create file $e")
            }
        }
    }

    // запись в базу данных путь, имя, дата, местоположение
    fun writeToDatabase(uriPhoto: String, typeCoffee: String) {
        myScope.launch {
            repository.insertPhoto(
                PhotoInDatabase(
                    latitude = latitude,
                    longitude = longitude,
                    photoUri = uriPhoto,
                    dataTakePhoto = toMyData(),
                    typeCoffee = typeCoffee
                )
            )
        }
    }
}

//  фабрика для получения контекста и базы данных
class ActivityViewModelFactory(
    private val context: Context,
    private val database: DaoPhotoCoffee
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActivity::class.java)) {
            return ViewModelActivity(context, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}