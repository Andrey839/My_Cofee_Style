package com.myapp.mycoffeestyle.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.databinding.FragmentMainBinding

const val TAKE_PHOTO = 23

class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentMainBinding
    private val interpolator = DecelerateInterpolator()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myFactory = context?.let { PageViewModelFactory(it) }
        pageViewModel = ViewModelProvider(this, myFactory!!).get(PageViewModel::class.java)
        pageViewModel.getCountWithSharedPref()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        auth = FirebaseAuth.getInstance()

        binding.lifecycleOwner = this
// привязываем переменные binding к классу
        binding.viewModel = pageViewModel
        binding.nameCoffee = this
// вызываем закрытие кнопок и анимацию счётчика и титлов
        pageViewModel.animationEnd.observe(viewLifecycleOwner, {
            when (it) {
                ESPRESSO -> {
                    endAnimationButtons()
                    binding.imageCountEspresso.rotationX = 0F
                    binding.imageCountEspresso.translationY = 0F
                    animatorCount(binding.imageCountEspresso)
                    binding.ImageTittleEspresso.rotation = 0F
                    animatorTittle(binding.ImageTittleEspresso)
                }
                AMERICANO -> {
                    endAnimationButtons()
                    binding.imageCountAmericano.rotationX = 0F
                    binding.imageCountAmericano.translationY = 0F
                    animatorCount(binding.imageCountAmericano)
                    binding.imageTittleAmericano.rotation = 0F
                    animatorTittle(binding.imageTittleAmericano)
                }
                CAPPUCCINO -> {
                    endAnimationButtons()
                    binding.imageCountCappuchino.rotationX = 0F
                    binding.imageCountCappuchino.translationY = 0F
                    animatorCount(binding.imageCountCappuchino)
                    binding.imageTittleCappuchino.rotation = 0F
                    animatorTittle(binding.imageTittleCappuchino)
                }
                LATTE -> {
                    endAnimationButtons()
                    binding.imageCountLatte.rotationX = 0F
                    binding.imageCountLatte.translationY = 0F
                    animatorCount(binding.imageCountLatte)
                    binding.imageTittleLatte.rotation = 0F
                    animatorTittle(binding.imageTittleLatte)
                }
                MACCHIATO -> {
                    endAnimationButtons()
                    binding.imageCountMakiato.rotationX = 0F
                    binding.imageCountMakiato.translationY = 0F
                    animatorCount(binding.imageCountMakiato)
                    binding.imageTittleMakiato.rotation = 0F
                    animatorTittle(binding.imageTittleMakiato)
                }
                RAF -> {
                    endAnimationButtons()
                    binding.imageCountRaf.rotationX = 0F
                    binding.imageCountRaf.translationY = 0F
                    animatorCount(binding.imageCountRaf)
                    binding.imageTittleRaf.rotation = 0F
                    animatorTittle(binding.imageTittleRaf)
                }
                DOPPIO -> {
                    endAnimationButtons()
                    binding.imageCountDoppio.rotationX = 0F
                    binding.imageCountDoppio.translationY = 0F
                    animatorCount(binding.imageCountDoppio)
                    binding.imageTittleDoppio.rotation = 0F
                    animatorTittle(binding.imageTittleDoppio)
                }
            }
        })

        // запускаем намерение сделать фото
        binding.floatingActionButtonPhoto.setOnClickListener {
            takePhotoCoffee()
        }

        return binding.root
    }

    // анимируем тайтли
    private fun animatorTittle(bindingTittle: View) {
        bindingTittle.animate().apply {
            this.rotation(360F)
            this.duration = 400
            this.interpolator = interpolator
            this.start()
        }
    }

    // анимируем счётчики
    private fun animatorCount(bindingCount: View) {
        bindingCount.animate().apply {
            this.translationY(-50F)
            this.rotationX(360F)
            this.duration = 400
            this.interpolator = interpolator
            this.start()
        }
    }

    companion object {
        const val MACCHIATO = "macchiato"
        const val CAPPUCCINO = "cappuccino"
        const val AMERICANO = "americano"
        const val DOPPIO = "doppio"
        const val ESPRESSO = "espresso"
        const val LATTE = "latte"
        const val RAF = "raf"
    }

    // закрываем кнопки
    private fun endAnimationButtons() {
        binding.constraintLayoutMain.transitionToStart()
    }

    //  намерение сделать фото ловим в Main Activity
    private fun takePhotoCoffee() {
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            activity?.startActivityForResult(takePhoto, TAKE_PHOTO)
        } catch (e: ActivityNotFoundException) {
            Log.e("tyi", "not found $e")
        }
    }

}