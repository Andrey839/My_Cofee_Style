package com.myapp.mycoffeestyle.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_photo")
data class PhotoInDatabase(
    @PrimaryKey(autoGenerate = true)
    val position: Int = 0,
    val latitude: String = "",
    val longitude: String = "",
    val photoUri: String,
    val typeCoffee: String,
    val dataTakePhoto: String,
    var expandable: Boolean = false
)