package com.myapp.mycoffeestyle.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoCoffee{
    @Insert
    fun insertDataCoffee(coffee: PhotoInDatabase)

    @Query("select * from table_photo")
    fun queryPhoto(): LiveData<List<PhotoInDatabase>>

    @Delete
    fun deleteCoffee(coffee: PhotoInDatabase)
}


@Database(entities = [PhotoInDatabase::class], version = 1)
abstract class DaoPhotoCoffee: RoomDatabase(){
    abstract val daoCoffee: DaoCoffee
}

private lateinit var INSTANCE: DaoPhotoCoffee

fun getDatabase(context: Context): DaoPhotoCoffee {
    synchronized(DaoPhotoCoffee::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DaoPhotoCoffee::class.java,
                "coffee"
            ).build()
        }
    }
    return INSTANCE
}
