package com.example.storyapps.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapps.R
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import com.example.storyapps.databinding.ActivityLoginBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.helper.afterTextChanged
import com.example.storyapps.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
//        ViewModelFactory(Injection.provideRepository(this))
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
        setupObservers()

        verificationLogin()

        playAnimation()
        loading()
    }

    private fun playAnimation() {
        // gambar yang berjalan ke kanan dan ke kiri secara berulang
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(200)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailEdit = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(200)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)

        // animasinya muncul secara bergantian
        AnimatorSet().apply {
            playSequentially(title, message, emailText, emailEdit, passwordText, passwordEdit, login)
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
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() &&
                binding.edLoginEmail.error == null && binding.edLoginPassword.error == null) {
                viewModel.login(email, password)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Please fill in all fields correctly.")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }
        }
    }

    private fun loading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun verificationLogin(){
        with(binding){
            // format email
            edLoginEmail.afterTextChanged { email ->
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edLoginEmail.error = "Invalid email address"
                } else {
                    binding.edLoginEmail.error = null
                }
            }

            // minimal 8 karakter
            edLoginPassword.afterTextChanged { password ->
                if (password.length < 8) {
                    binding.edLoginPassword.error = "Password must be at least 8 characters"
                } else {
                    binding.edLoginPassword.error = null
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.loggedInUser.observe(this) { user ->
            binding.progressBar1.visibility = View.GONE
            if (user != null) {
                AlertDialog.Builder(this).apply {
                    lifecycleScope.launch {
                        val userPreference = UserPreference.getInstance(dataStore)
                        userPreference.saveToken(user.token)
                        // check if user preference successfully save token
                        val token = userPreference.getSession().map { it.token }.first()
                        Log.d("LoginActivity", "setupObservers: $token")
                    }
                    setTitle("Yeah!")
                    setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtras(Bundle().apply {
                            putString("extra_token", user.token)
                            Log.d("LoginActivity", "setupAction: ${user.token}")
                        })
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
        viewModel.isLogin.observe(this) { isLogin ->
            if (!isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle("Login Failed")
                    setMessage("Please check your email and password and try again.")
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
                Log.e("LoginActivitywwww", "setupAction: Login failed")
            }
        }
    }
}