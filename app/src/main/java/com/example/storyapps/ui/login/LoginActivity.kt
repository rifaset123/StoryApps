package com.example.storyapps.ui.login

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapps.R
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import com.example.storyapps.databinding.ActivityLoginBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.helper.afterTextChanged
import com.example.storyapps.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.isEnabled = false
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
        val emailBox = ObjectAnimator.ofFloat(binding.emailEditTextLayoutLogin, View.ALPHA, 1f).setDuration(200)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val passwordBox = ObjectAnimator.ofFloat(binding.passwordEditTextLayoutLogin, View.ALPHA, 1f).setDuration(200)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)

        // animasinya muncul secara bergantian
        AnimatorSet().apply {
            playSequentially(title, message, emailText, emailBox,  passwordText,  passwordBox, button)
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
            val email = binding.emailEditTextLayoutLogin.text.toString()
            val password = binding.passwordEditTextLayoutLogin.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() &&
                binding.passwordEditTextLayoutLogin.error == null && binding.passwordEditTextLayoutLogin.error == null) {
                binding.loginButton.isEnabled = false
                viewModel.login(email, password)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage(getString(R.string.please_fill_in_all_fields_correctly))
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
            binding.loginButton.isEnabled = !isLoading
        }
    }

    private fun verificationLogin(){
        with(binding){
            // format email
            emailEditTextLayoutLogin.afterTextChanged { email ->
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEditTextLayoutLogin.error = getString(R.string.use_correct_email_format)
                } else {
                    binding.emailEditTextLayoutLogin.error = null
                }
                enableSignUpButton()
            }

            // minimal 8 karakter
            passwordEditTextLayoutLogin.afterTextChanged { password ->
                if (password.length < 8) {
                    binding.passwordEditTextLayoutLogin.error = getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    binding.passwordEditTextLayoutLogin.error = null
                }
                enableSignUpButton()
            }
        }
    }

    private fun enableSignUpButton() {
        binding.loginButton.isEnabled = binding.emailEditTextLayoutLogin.error == null &&
                binding.passwordEditTextLayoutLogin.error == null &&
                binding.emailEditTextLayoutLogin.text?.isNotEmpty() == true &&
                binding.passwordEditTextLayoutLogin.text?.isNotEmpty() == true
    }

    private fun setupObservers() {
        viewModel.loggedInUser.observe(this) { user ->
            binding.progressBar1.visibility = View.GONE
            if (user != null) {
                AlertDialog.Builder(this).apply {
                    lifecycleScope.launch {
                        val userPreference = UserPreference.getInstance(dataStore)
                        userPreference.saveToken(user.token)
                    }
                    setTitle("Yeay")
                    setMessage(getString(R.string.you_have_successfully_logged_in_enjoy_all_the_features))
                    setCancelable(false)
                    setPositiveButton(getString(R.string.continue_register)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtras(Bundle().apply {
                            putString("extra_token", user.token)
                        })
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    create()
                    show()
                }
            }
        }
        viewModel.isLogin.observe(this) { isLogin ->
            if (!isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.login_failed))
                    setMessage(getString(R.string.account_not_found_please_make_sure_your_email_and_password_are_correct))
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
            }
        }
    }
}