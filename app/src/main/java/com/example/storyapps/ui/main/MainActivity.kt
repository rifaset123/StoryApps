package com.example.storyapps.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
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
import com.example.storyapps.ui.addStory.AddStoryActivity
import com.example.storyapps.ui.detail.DetailActivity
import com.example.storyapps.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

// https://www.dicoding.com/academies/352/tutorials/22889
class MainActivity : AppCompatActivity(), OnEventClickListener {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

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

        viewModel.isLoading.observe(this) { isLoading ->
            // Show or hide loading indicator
            binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

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

        loadTokens()

        setupFab()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Handle logout action
                setupAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // biar fullscreren tanpa action bar
//    private fun setupView() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.hide()
//    }

    private fun setupAction() {
        viewModel.logout()
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
        val intentDetailPage = Intent(this, DetailActivity::class.java)
        intentDetailPage.putExtra("STORY_ID", event.id)
        startActivity(intentDetailPage, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

    }

    private fun setupFab() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val token = viewModel.getToken()
            if (token != null) {
                viewModel.getStory(token)
            } else {
                Log.d(TAG, "Token is null")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityqweqweq"
    }
}