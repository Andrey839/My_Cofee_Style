package com.myapp.mycoffeestyle.registration.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.myapp.mycoffeestyle.MainActivity
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.database.DaoPhotoCoffee
import com.myapp.mycoffeestyle.databinding.ActivityRegistrationMainBinding
import com.myapp.mycoffeestyle.ui.main.STORAGE

const val CODE_SIGN_IN = 23
const val KEY_URL_AVATAR = "userAvatar"

class RegistrationMainActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationActivity

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(this)
        ).get(RegistrationActivity::class.java)
        binding.viewRegistrationActivity = viewModel

        sharedPref = this.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
            .requestProfile().requestIdToken(getString(R.string.auth_2_01)).build()
        val googleClient = GoogleSignIn.getClient(this, gso)
// проверяем эмеил на правильность ввода
        binding.textEmail.addTextChangedListener {
            if (it?.length != 0) {
                if ("@" in it.toString()) {
                    email = it.toString()
                    binding.email.error = null
                } else binding.email.error = this.getString(R.string.invalid_email_to)

            } else {
                binding.email.error = this.getString(R.string.invalid_email)
            }
        }
// проверяем пароль на длинну
        binding.textPassword.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.password.error = this.getString(R.string.invalid_email)
            } else {
                if (it.toString().length < 6) binding.password.error =
                    this.getString(R.string.invalid_password)
                else {
                    binding.password.error = null
                    password = it.toString()
                    if (binding.buttonSignInOrRegistration.text == getString(R.string.action_sign_in_short)) binding.buttonSignInOrRegistration.isEnabled =
                        true
                }
            }
        }
// проверяем пароль на совпадения
        binding.againTextPassword.addTextChangedListener {
            if (it.toString().length > 1 && !it.toString()
                    .equals(password)
            ) binding.againPassword.error =
                this.getString(R.string.password_again_failed)
            else {
                binding.againPassword.error = null
                binding.buttonSignInOrRegistration.isEnabled = true
            }
        }

// запускаем регистрацию или вход взависимости от нажатой кнопки

        binding.buttonSignInOrRegistration.setOnClickListener {
            when (binding.buttonSignInOrRegistration.text) {
                this.getString(R.string.action_sign_in_short) -> {
                    if (binding.buttonSignInOrRegistration.isClickable) viewModel.signInUser(
                        email,
                        password
                    )
                }
                this.getString(R.string.registration) -> {
                    if (binding.buttonSignInOrRegistration.isClickable) viewModel.createNewUser(
                        email,
                        password
                    )
                }
            }
        }

// запуск регистрации через Google
        binding.googleButton.setOnClickListener {
            startActivityForResult(googleClient.signInIntent, CODE_SIGN_IN)
        }

        // слушаем удачна регистрация, вход или нет
        viewModel.registrationIsSuccessful.observe(this, {
            if (it) {
                Toast.makeText(this, this.getText(R.string.welcome), Toast.LENGTH_LONG)
                    .show()
                viewModel.registrationIsSuccessful.value = false
                binding.constraintLayout.transitionToEnd()
                startActivity(Intent(this, MainActivity::class.java))
            } else Toast.makeText(
                this,
                this.getText(R.string.registration_is_failed),
                Toast.LENGTH_LONG
            ).show()
            binding.constraintLayout.transitionToEnd()
            viewModel.registrationIsSuccessful.value = false
        })
        // слушаем нажатия кнопки регистрация и запускаем анимацию
        binding.registrationButton.setOnClickListener {
            binding.constraintLayout.setTransition(R.id.startRegistration, R.id.endRegistration)
            binding.constraintLayout.transitionToEnd()
            binding.buttonSignInOrRegistration.text = getString(R.string.registration)
        }
//  слушаем нажатия кнопки вход и запускаем анимацию
        binding.comeIn.setOnClickListener {
            binding.constraintLayout.setTransition(R.id.startComeIn, R.id.endComeIn)
            binding.constraintLayout.transitionToEnd()
            binding.buttonSignInOrRegistration.text = getString(R.string.action_sign_in_short)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.e("tyi", "is successful ${account?.id}")
                account?.idToken?.let { viewModel.singInWithGoogle(it) }
// загружаем ссылку на аватарку пользователя в память
                sharedPref?.edit()?.putString(KEY_URL_AVATAR, account?.photoUrl.toString())?.apply()
            } catch (e: ApiException) {
                Log.e("tyi", "error Google $e")
            }
        }
    }

    override fun onStart() {
        if (auth.currentUser != null) {
            sharedPref?.edit()?.putString(KEY_URL_AVATAR, auth.currentUser?.photoUrl.toString())?.apply()
            startActivity(Intent(this, MainActivity::class.java))
        }
        super.onStart()
    }
}