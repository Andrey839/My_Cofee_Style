package com.myapp.mycoffeestyle

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Interpolator
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.database.getDatabase
import com.myapp.mycoffeestyle.databinding.ActivityMainBinding
import com.myapp.mycoffeestyle.registration.ui.KEY_URL_AVATAR
import com.myapp.mycoffeestyle.repository.RepositoryForDatabase
import com.myapp.mycoffeestyle.ui.main.PlaceholderFragment
import com.myapp.mycoffeestyle.ui.main.STORAGE
import com.myapp.mycoffeestyle.ui.main.SectionsPagerAdapter
import com.myapp.mycoffeestyle.ui.main.TAKE_PHOTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

const val PERMISSIONS = 34

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    EasyPermissions.PermissionCallbacks {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var auth: FirebaseAuth
    private var sharedPref: SharedPreferences? = null
    private lateinit var viewModel: ViewModelActivity
    private lateinit var avatar: String
    private lateinit var name: String
    private lateinit var binding: ActivityMainBinding
    private val listNameCoffee = arrayOf(
        PlaceholderFragment.AMERICANO,
        PlaceholderFragment.CAPPUCCINO,
        PlaceholderFragment.LATTE,
        PlaceholderFragment.RAF,
        PlaceholderFragment.MACCHIATO,
        PlaceholderFragment.DOPPIO,
        PlaceholderFragment.ESPRESSO
    )
    private lateinit var myScope: CoroutineScope
    private lateinit var database: DaoPhotoCoffee
    private lateinit var repository: RepositoryForDatabase
    private var nameCoffee = ""
    private val permissionLocation =
        arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private lateinit var animWidth: ValueAnimator
    private lateinit var animHeight: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val job = Job()
        myScope = CoroutineScope(Dispatchers.IO + job)

        database = getDatabase(this)
        repository = RepositoryForDatabase(database)

        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(this, database)
        ).get(ViewModelActivity::class.java)

        binding.viewActivity = viewModel

        auth = FirebaseAuth.getInstance()

        sharedPref = this.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
// загружаем картинку пользователя
        avatar = sharedPref?.getString(KEY_URL_AVATAR, "").toString()
        name = auth.currentUser?.displayName.toString()
        viewModel.setAvatarAndName(avatar, name)

        viewModel.currentFilePath.observe(this, {
            // записываем в базу данных путь к фото имя и дату
            if (it.isNotEmpty()) {
                viewModel.writeToDatabase(it, nameCoffee)
            }

        })

    }

    override fun onResume() {
        sharedPref?.registerOnSharedPreferenceChangeListener(this)
        requestPermissions()
        // определяем местоположения
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                if (p0?.lastLocation != null) {
                    Log.e("tyi", "${p0.lastLocation.latitude}")
                    viewModel.latitude = p0.lastLocation.latitude.toString()
                    viewModel.longitude = p0.lastLocation.longitude.toString()
                }
            }
        }
        super.onResume()
    }

    override fun onPause() {
        sharedPref?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    // загружаем картинку пользователя после регистрации
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == KEY_URL_AVATAR) {
            avatar = sharedPreferences?.getString(key, "").toString()
            name = auth.currentUser?.displayName.toString()
            viewModel.setAvatarAndName(avatar, name)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) Toast.makeText(
                this,
                getString(R.string.permission_rationale_sitting),
                Toast.LENGTH_LONG
            ).show()
            if (requestCode == TAKE_PHOTO) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val icon = BitmapDrawable(imageBitmap)
                locationUpdate()
                MaterialAlertDialogBuilder(this)
                    .setIcon(icon)
                    .setTitle(getString(R.string.tittle_dialog))
                    .setPositiveButton(getString(R.string.positive_button)) { dialog, which ->
// даём имя и сохраняем в память устройства
                        viewModel.createImageFile(imageBitmap)
                    }
                    .setNegativeButton(getString(R.string.negative_button)) { dialog, swich ->

                    }
                    .setSingleChoiceItems(listNameCoffee, 1) { dialog, swith ->
                        nameCoffee = listNameCoffee[swith]
                    }
                    .show()
            }

        } catch (e: Exception) {
            Log.e("tyi", "not photo $e")
        }
    }

    //  переопределяем запрос разрешения в Easy Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        currentLocationUser()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, permissionLocation)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    // запрос разрешения
    private fun requestPermissions() {
        if (requestPermission()) {
            currentLocationUser()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_rationale),
                PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    // проверка давал ражрешение пользователь?
    private fun requestPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // нажатие на аватар что бы выйти
    fun clickOnAvatar(view: View){
        animationExitAccount()
    }

    private fun animationExitAccount() {
        animHeight = ValueAnimator.ofInt(binding.textExit.width, 160).apply {
            duration = 600
        }
        animHeight.addUpdateListener {
            val value = it.animatedValue as Int
            Log.e("tyi", "$value")
            binding.textExit.height = value
            binding.textExit.requestLayout()
        }
        animHeight.start()

        animWidth = ValueAnimator.ofInt(binding.textExit.width, 450).apply {
            duration = 600
        }
        animWidth.addUpdateListener {
            val value = it.animatedValue as Int
            Log.e("tyi", "$value")
            binding.textExit.width = value
            binding.textExit.requestLayout()
        }
        animWidth.start()
        binding.textExit.text = getString(R.string.exit_with_account)
    }

    // запускаем определение местоположение
    private fun currentLocationUser() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    // обновляем местоположения
    private fun locationUpdate(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
}
