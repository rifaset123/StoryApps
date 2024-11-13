package com.example.storyapps.ui.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapps.R
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.databinding.ActivityRegisterBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.helper.afterTextChanged
import com.example.storyapps.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registerViewModel by viewModels<RegisterViewModel> {
            ViewModelFactory.getInstance(this)
        }

        setupView()
        setupAction(registerViewModel)

        verificationSignUp()
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

    private fun setupAction(registerViewModel: RegisterViewModel) {
        binding.signupButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() &&
                binding.edRegisterEmail.error == null && binding.edRegisterPassword.error == null) {

                // save session login
                registerViewModel.saveSession(UserModel(email, "sample_token"))
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("Akun dengan $email sudah jadi nih. Yuk eksplor aplikasi ini.")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
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

    private fun verificationSignUp(){
        with(binding){
            // format email
            edRegisterEmail.afterTextChanged { email ->
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edRegisterEmail.error = "Invalid email address"
                } else {
                    binding.edRegisterEmail.error = null
                }
            }

            // minimal 8 karakter
            edRegisterPassword.afterTextChanged { password ->
                if (password.length < 8) {
                    binding.edRegisterPassword.error = "Password must be at least 8 characters"
                } else {
                    binding.edRegisterPassword.error = null
                }
            }
        }
    }
}