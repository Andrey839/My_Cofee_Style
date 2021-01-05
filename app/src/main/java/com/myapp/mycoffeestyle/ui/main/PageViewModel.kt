package com.myapp.mycoffeestyle.ui.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val MACCHIATO = "macchiato"
const val CAPPUCCINO = "cappuccino"
const val AMERICANO = "americano"
const val DOPPIO = "doppio"
const val ESPRESSO = "espresso"
const val LATTE = "latte"
const val RAF = "raf"
const val STORAGE = "nameCoffee"

class PageViewModel(private val context: Context) : ViewModel() {

    private val sharedPref = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)

    // Живые данные для отображения щётчика выпитого
    private var _espressoLive = MutableLiveData<Int>()
    private var _cappuccinoLive = MutableLiveData<Int>()
    private var _latteLive = MutableLiveData<Int>()
    private var _doppioLive = MutableLiveData<Int>()
    private var _rafLive = MutableLiveData<Int>()
    private var _macchiatoLive = MutableLiveData<Int>()
    private var _americanoLive = MutableLiveData<Int>()

    val espressoLive: LiveData<Int>
        get() = _espressoLive
    val cappuccinoLive: LiveData<Int>
        get() = _cappuccinoLive
    val latteLive: LiveData<Int>
        get() = _latteLive
    val doppioLive: LiveData<Int>
        get() = _doppioLive
    val rafLive: LiveData<Int>
        get() = _rafLive
    val macchiatoLive: LiveData<Int>
        get() = _macchiatoLive
    val americanoLive: LiveData<Int>
        get() = _americanoLive

    // переменные для местного пользования
    private var espresso = 0
    private var cappuccino = 0
    private var latte = 0
    private var doppio = 0
    private var raf = 0
    private var macchiato = 0
    private var americano = 0

    // переменная для управления завершения анимации
    var animationEnd = MutableLiveData<String>()

    // переменная для отслеживания большего значения числа
    private val _sizeOvalTittle = MutableLiveData<MutableMap<String, Int>>()
    private val sizeOvalTittleMap = mutableMapOf<String, Int>()
    val sizeOvalTittle: LiveData<MutableMap<String, Int>>
    get() = _sizeOvalTittle

    // прибавляем +1 по нажатию соответствующей кнопкиб записываем данные и завершаем анимацию
    fun writeCountToSharedPref(nameCoffee: String) {
        when (nameCoffee) {
            ESPRESSO -> {
                espresso++
                sharedPref.edit().putInt(ESPRESSO, espresso).apply()
                _espressoLive.value = espresso
                animationEnd.value = ESPRESSO
                sizeOvalTittleMap[ESPRESSO] = espresso
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            AMERICANO -> {
                americano++
                sharedPref.edit().putInt(AMERICANO, americano).apply()
                _americanoLive.value = americano
                animationEnd.value = AMERICANO
                sizeOvalTittleMap[AMERICANO] = americano
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            CAPPUCCINO -> {
                cappuccino++
                sharedPref.edit().putInt(CAPPUCCINO, cappuccino).apply()
                _cappuccinoLive.value = cappuccino
                animationEnd.value = CAPPUCCINO
                sizeOvalTittleMap[CAPPUCCINO] = cappuccino
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            LATTE -> {
                latte++
                sharedPref.edit().putInt(LATTE, latte).apply()
                _latteLive.value = latte
                animationEnd.value = LATTE
                sizeOvalTittleMap[LATTE] = latte
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            RAF -> {
                raf++
                sharedPref.edit().putInt(RAF, raf).apply()
                _rafLive.value = raf
                animationEnd.value = RAF
                sizeOvalTittleMap[RAF] = raf
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            DOPPIO -> {
                doppio++
                sharedPref.edit().putInt(DOPPIO, doppio).apply()
                _doppioLive.value = doppio
                animationEnd.value = DOPPIO
                sizeOvalTittleMap[DOPPIO] = doppio
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
            MACCHIATO -> {
                macchiato++
                sharedPref.edit().putInt(MACCHIATO, macchiato).apply()
                _macchiatoLive.value = macchiato
                animationEnd.value = MACCHIATO
                sizeOvalTittleMap[MACCHIATO] = macchiato
                _sizeOvalTittle.value = sizeOvalTittleMap
            }
        }
    }

    // инициализация щётчика берём количество из Shared Pref
    fun getCountWithSharedPref() {
        espresso = sharedPref.getInt(ESPRESSO, 0)
        sizeOvalTittleMap[ESPRESSO] = espresso
        _espressoLive.value = espresso
        cappuccino = sharedPref.getInt(CAPPUCCINO, 0)
        sizeOvalTittleMap[CAPPUCCINO] = cappuccino
        _cappuccinoLive.value = cappuccino
        latte = sharedPref.getInt(LATTE, 0)
        sizeOvalTittleMap[LATTE] = latte
        _latteLive.value = latte
        doppio = sharedPref.getInt(DOPPIO, 0)
        sizeOvalTittleMap[DOPPIO] = doppio
        _doppioLive.value = doppio
        raf = sharedPref.getInt(RAF, 0)
        sizeOvalTittleMap[RAF] = raf
        _rafLive.value = raf
        macchiato = sharedPref.getInt(MACCHIATO, 0)
        sizeOvalTittleMap[MACCHIATO] = macchiato
        _macchiatoLive.value = macchiato
        americano = sharedPref.getInt(AMERICANO, 0)
        sizeOvalTittleMap[AMERICANO] = americano
        _americanoLive.value = americano
        _sizeOvalTittle.value = sizeOvalTittleMap
    }

}

//  фабрика для получения контекста во PageViewModel()
class PageViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java)) {
            return PageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}