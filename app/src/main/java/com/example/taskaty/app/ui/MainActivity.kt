package com.example.taskaty.app.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.auth.login.LoginFragment
import com.example.taskaty.app.ui.fragments.onBoarding.OnBoardingFragment
import com.example.taskaty.databinding.ActivityMainBinding
import com.example.taskaty.global.GlobalState

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppContext()
        if (!hasOnBoardingBeenShown()) {
            replaceFragment(OnBoardingFragment())
        } else {
            replaceFragment(LoginFragment())
        }
    }

    private fun setupAppContext() {
        GlobalState.appContext = applicationContext
    }

    fun hasOnBoardingBeenShown(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("onBoardingShown", false)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}