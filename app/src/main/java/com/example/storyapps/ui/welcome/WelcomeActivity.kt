package com.example.storyapps.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityWelcomeBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.ui.login.LoginActivity
import com.example.storyapps.ui.login.LoginViewModel
import com.example.storyapps.ui.main.MainActivity
import com.example.storyapps.ui.signup.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        enableEdgeToEdge()


        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()

        playAnimation()

        // login check
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun playAnimation() {
        // gambar yang berjalan ke kanan dan ke kiri secara berulang
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // menggunakan animasi fade in jadi harus pake ALPHA di xml nya
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(500)

        // memunculkan 2 Button dengan animasi bersamaan
        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        // animasinya muncul secara bergantian
        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        with(binding){
            loginButton.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }

            signupButton.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
            }
        }
    }
}