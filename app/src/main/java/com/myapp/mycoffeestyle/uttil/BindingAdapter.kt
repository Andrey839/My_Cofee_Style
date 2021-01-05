package com.myapp.mycoffeestyle.uttil

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.ui.main.*

// просчитываем размеры круга, устанавливаем размер шрифта
@BindingAdapter("sizeTittleNameCoffee")
fun TextView.sizeTittle(map: MutableMap<String, Int>) {
    let {
        val list = ArrayList(map.values)
        list.sort()
        val set: Set<Int> = list.toSet()
        var index: Int? = null
        when (it.text) {
            "CA" -> {
                val count = map[CAPPUCCINO]
                index = set.indexOf(count)
            }
            "AM" -> {
                val count = map[CAPPUCCINO]
                index = set.indexOf(count)
            }
            "ES" -> {
                val count = map[ESPRESSO]
                index = set.indexOf(count)
            }
            "LA" -> {
                val count = map[LATTE]
                index = set.indexOf(count)
            }
            "RA" -> {
                val count = map[RAF]
                index = set.indexOf(count)
            }
            "DO" -> {
                val count = map[DOPPIO]
                index = set.indexOf(count)
            }
            "MA" -> {
                val count = map[MACCHIATO]
                index = set.indexOf(count)
            }
        }
        if (index != null) {
            when (index) {
                0 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.start_tittle_text_size).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.start_tittle_text_size).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.start_tittle_text_size)
                }
                1 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_2).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_2).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_2)
                }
                2 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_3).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_3).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_3)
                }
                3 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_4).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_4).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_4)
                }
                4 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_5).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_5).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_5)
                }
                5 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_6).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_6).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_6)
                }
                6 -> {
                    it.width =
                        it.context.resources.getDimension(R.dimen.position_7).toInt()
                    it.height =
                        it.context.resources.getDimension(R.dimen.position_7).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_tittle_7)
                }
            }
        }
    }
}

@BindingAdapter("sizeCountNameCoffee")
fun TextView.sizeCount(map: MutableMap<String, Int>) {
    let {
        val list = ArrayList(map.values)
        list.sort()
        val set: Set<Int> = list.toSet()
        var index: Int? = null
        when (it.text) {
            "imageCountEspresso" -> {
                val count = map[ESPRESSO]
                index = set.indexOf(count)
            }
            "imageCountDoppio" -> {
                val count = map[DOPPIO]
                index = set.indexOf(count)
            }
            "imageCountAmericano" -> {
                val count = map[AMERICANO]
                index = set.indexOf(count)
            }
            "imageCountCappuchino" -> {
                val count = map[CAPPUCCINO]
                index = set.indexOf(count)
            }
            "imageCountLatte" -> {
                val count = map[LATTE]
                index = set.indexOf(count)
            }
            "imageCountRaf" -> {
                val count = map[RAF]
                index = set.indexOf(count)
            }
            "imageCountMakiato" -> {
                val count = map[MACCHIATO]
                index = set.indexOf(count)
            }
        }
        if (index != null) {
            when (index) {
                0 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_1).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_1).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.start_size_count_text)
                }
                1 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_2).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_2).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_2)
                }
                2 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_3).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_3).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_3)
                }
                3 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_4).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_4).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_4)
                }
                4 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_5).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_5).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_5)
                }
                5 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_6).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_6).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_6)
                }
                6 -> {
                    it.width = it.context.resources.getDimension(R.dimen.size_oval_count_7).toInt()
                    it.height = it.context.resources.getDimension(R.dimen.size_oval_count_7).toInt()
                    it.textSize = it.context.resources.getDimension(R.dimen.size_count_7)
                }
            }
        }
    }
}

// устанавливаем фото на аватар
@BindingAdapter("setAvatar")
fun ImageView.setAvatarAndName(avatar: String?) {
    let {
        if (avatar?.isNotEmpty() == true) {
            Glide.with(it.context).load(avatar).circleCrop().into(it)
        }
    }
}

// устанавливаем фото
@BindingAdapter("setPhoto")
fun ImageView.setPhoto(photo: String?) {
    let {
        if (photo?.isNotEmpty() == true) {
            Glide.with(it.context).load(photo).into(it)
        }
    }
}
