package com.myapp.mycoffeestyle.registration.ui

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.util.rangeTo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.log

class RegistrationActivity(val context: Activity) : ViewModel() {

    val registrationIsSuccessful = MutableLiveData<Boolean>()

    val auth = FirebaseAuth.getInstance()

    fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context) { it ->
            if (it.isSuccessful) {
                registrationIsSuccessful.value = true
            } else {
                registrationIsSuccessful.value = false
                Log.e("tyi", "registration failed ${it.exception}")
            }
        }
    }

    fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context) {
            try {
                if (it.isSuccessful) registrationIsSuccessful.value = true
                else registrationIsSuccessful.value = false
            } catch (e: Exception){
                Log.e("tyi", "error sign in $e")
            }
        }
    }

    fun singInWithGoogle(idToken: String) {
        auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            .addOnCompleteListener(context) {
                if (it.isSuccessful) registrationIsSuccessful.value = true
                else{
                    registrationIsSuccessful.value = false
                    Log.e("tyi", "error sign in with google ${it.exception}")
                }
            }
    }
}

class RegistrationViewModelFactory(
    private val context: Activity
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationActivity::class.java)) {
            return RegistrationActivity(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}