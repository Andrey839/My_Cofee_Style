package com.myapp.mycoffeestyle.photoCoffee

import android.animation.ObjectAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.database.getDatabase
import com.myapp.mycoffeestyle.databinding.FragmentPhotoBinding
import com.myapp.mycoffeestyle.recyclerAdapter.AdapterPhotoCoffee
import com.myapp.mycoffeestyle.recyclerAdapter.ListenerCallback
import com.myapp.mycoffeestyle.ui.main.TAKE_PHOTO

const val DELAY: Long = 3000

class PhotoFragment : Fragment(), ListenerCallback {

    private val listPhoto = arrayListOf<PhotoInDatabase>()
    private lateinit var adapter: AdapterPhotoCoffee
    private var deletePhotoOrNot = false
    private lateinit var viewModel: ViewModelPhoto
    private var snack: Snackbar? = null
    private lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        binding.lifecycleOwner = this

        val database = context?.let { getDatabase(it) }

        viewModel =
            ViewModelProvider(this, PhotoViewModelFactory(database)).get(ViewModelPhoto::class.java)
        binding.viewFragment = viewModel

        binding.floatingActionPhoto.setOnClickListener {
            takePhotoCoffee()
        }

        adapter = AdapterPhotoCoffee(arrayListOf(), this)
        val manager = GridLayoutManager(context, 2)
        binding.recyclerPhotoCoffee.let {
            it.adapter = adapter
            it.layoutManager = manager
            it.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            it.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        // Добовляем новые фото в список
        viewModel.listPhoto.observe(viewLifecycleOwner, {
            listPhoto.clear()
            listPhoto.addAll(it)
            adapter.setData(listPhoto)
        })

        return binding.root
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

    // слушаем нажатия кнопок
    override fun onClick(view: View, data: PhotoInDatabase) {
        var index = 0
        when (view.id) {
            // шарим фотку
            R.id.shared -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, data.photoUri)
                context?.startActivities(
                    arrayOf(
                        Intent.createChooser(
                            intent,
                            context?.getString(R.string.send_to)
                        )
                    )
                )
            }
            // удаляем фотку
            R.id.delete -> {
                // анимация кнопки фото
                val translationY = -180F
                val anim = ObjectAnimator.ofFloat(binding.floatingActionPhoto, View.TRANSLATION_Y, translationY)
                anim.duration = 400
                anim.start()
                // узнаём индекс для обратной вставки на место
                index = listPhoto.indexOf(data)
                listPhoto.removeAt(index)
                adapter.setData(listPhoto)
                context?.getString(R.string.delete_photo)?.let {
                    snack = Snackbar.make(view, it, Snackbar.LENGTH_LONG)
                        .setAction(context?.getString(R.string.cancel)) {
                            deletePhotoOrNot = true
                        }
                }
                snack?.apply {
                    // настраиваем snack bar
                    setActionTextColor(resources.getColor(R.color.design_default_color_primary))
                    setBackgroundTint(resources.getColor(R.color.colorAccent))
                    show()
                }?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    // завершение показа
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT && !deletePhotoOrNot) {
                            viewModel.deletePhoto(data)
                        } else {
                            listPhoto.add(index, data)
                            adapter.setData(listPhoto)
                        }
                        anim.reverse()
                    }
                })
            }
        }
    }
}