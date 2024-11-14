package com.example.storyapps.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapps.R
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.databinding.ActivityMainBinding
import com.example.storyapps.helper.OnEventClickListener
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

// https://www.dicoding.com/academies/352/tutorials/22889
class MainActivity : AppCompatActivity(), OnEventClickListener {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = binding.rvStories
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val adapter = MainAdapter(this)
        recyclerView.adapter = adapter

        viewModel.listStory.observe(this) { stories ->
            adapter.submitList(stories)
        }

        // logout ketika token dihapus
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupAction()
        loadTokens()
    }



    // biar kalo log out langsung keluar
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
        binding.actionLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun loadTokens() {
        lifecycleScope.launch {
            val token = viewModel.getToken()
            if (token != null) {
                viewModel.getStory(token)
                Log.d(TAG, "Token is $token")
            } else {
                Log.d(TAG, "Token is null")
            }
        }
    }

    override fun onEventClick(event: ListStory) {
        Toast.makeText(this, "Clicked: ${event.name}", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainActivityqweqweq"
    }
}