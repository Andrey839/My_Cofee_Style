package com.myapp.mycoffeestyle.uttil

import android.provider.ContactsContract
import java.text.SimpleDateFormat
import java.util.*

fun toMyData(): String {
    val data = Calendar.getInstance().time
    val format = SimpleDateFormat.getDateInstance()
    return format.format(data)
}