package com.example.storyapps.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityDetailBinding
import com.example.storyapps.helper.ViewModelFactory
import com.example.storyapps.ui.main.MainViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val storyId = intent.getStringExtra("STORY_ID")
        if (storyId != null) {
            viewModel.getStoryDetail(storyId)
        }

        viewModel.storyDetail.observe(this) { story ->
            showLoading(false)
            with(binding){
                titleTextView.text = story?.name
                descriptionTextView.text = story?.description
                Glide.with(this@DetailActivity)
                    .load(story?.photoUrl)
                    .placeholder(R.drawable.ng)
                    .into(imageView)

                btnBack.setOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}