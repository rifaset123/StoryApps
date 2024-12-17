package com.example.storyapps.ui.introduction

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.storyapps.databinding.ActivityIntroductionBinding
import com.example.storyapps.ui.main.MainActivity
import com.example.storyapps.ui.welcome.WelcomeActivity

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // cuman buat first launch
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

        if (!isFirstLaunch) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        enableEdgeToEdge()

        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}