package com.example.storyapps.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityRegisterBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.helper.afterTextChanged

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.signupButton.isEnabled = false

        setupView()
        setupAction()
        setupObservers()

        verificationSignUp()
        loading()

        playAnimation()
    }

    private fun playAnimation() {
        // gambar yang berjalan ke kanan dan ke kiri secara berulang
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(200)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(200)
        val nameTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val button = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(200)

        // animasinya muncul secara bergantian
        AnimatorSet().apply {
            playSequentially(image, title, AnimatorSet().apply {
                playTogether(nameText, nameTextLayout, emailText, emailTextLayout, passwordText, passwordTextLayout)
            }, button)
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditTextLayout.text.toString()
            val email = binding.emailEditTextLayout.text.toString()
            val password = binding.passwordEditTextLayout.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() &&
                binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
                binding.signupButton.isEnabled = false
                // save session login
                registerViewModel.register(name, email, password)
            } else {
                binding.signupButton.isEnabled = false
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Please fill in all fields correctly.")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
                binding.signupButton.isEnabled = true
            }
        }
    }

    private fun setupObservers() {
        registerViewModel.registerStatus.observe(this) { user ->
            binding.progressBar2.visibility = View.GONE
            if (user != null) {
                AlertDialog.Builder(this).apply {
                    setTitle("Yeay!")
                    setMessage(getString(R.string.account_has_been_created_successfully))
                    setPositiveButton(getString(R.string.continue_register)) { _, _ ->
                        finishAfterTransition()
                    }
                    create()
                    show()
                }
            }
        }
        registerViewModel.isRegister.observe(this) { isLogin ->
            if (!isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.register_failed))
                    setMessage(getString(R.string.email_has_already_taken))
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
            }
        }
    }

    private fun verificationSignUp(){
        with(binding){
            // format email
            emailEditTextLayout.afterTextChanged { email ->
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEditTextLayout.error = getString(R.string.use_correct_email_format)
                } else {
                    binding.emailEditTextLayout.error = null
                }
                enableSignUpButton()
            }

            // minimal 8 karakter
            passwordEditTextLayout.afterTextChanged { password ->
                if (password.length < 8) {
                    binding.passwordEditTextLayout.error = getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    binding.passwordEditTextLayout.error = null
                }
                enableSignUpButton()
            }
        }
    }

    private fun enableSignUpButton() {
        binding.signupButton.isEnabled = binding.emailEditTextLayout.error == null &&
                binding.passwordEditTextLayout.error == null &&
                binding.emailEditTextLayout.text?.isNotEmpty() == true &&
                binding.passwordEditTextLayout.text?.isNotEmpty() == true
    }

    private fun loading() {
        registerViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.signupButton.isEnabled = !isLoading
        }
    }
}