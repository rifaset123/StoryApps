package com.example.storyapps.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.databinding.ActivityDetailBinding
import com.example.storyapps.helper.ViewModelFactory

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

        val story = intent.getSerializableExtra("STORY_OBJECT") as ListStory?
        if (story != null) {
            story.id?.let { viewModel.getStoryDetail(it) }
        }
        story?.let {
            showLoading(false)
            with(binding) {
                titleTextView.text = it.name
                descriptionTextView.text = it.description
                locationTextViewLat.text = it.lat.toString()
                locationTextViewLon.text = it.lon.toString()
                Glide.with(this@DetailActivity)
                    .load(it.photoUrl)
                    .placeholder(R.drawable.ng)
                    .into(imageView)

                btnBack.setOnClickListener {
                    finishAfterTransition()
                }
            }
        }

        // this doesnt work on first launch for some reason idk why
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
                    finishAfterTransition()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}