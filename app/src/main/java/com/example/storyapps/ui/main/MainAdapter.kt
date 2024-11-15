package com.example.storyapps.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.databinding.ItemCardViewBinding
import com.example.storyapps.helper.OnEventClickListener

class MainAdapter(private val listener: OnEventClickListener) : ListAdapter<ListStory, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

        // click handler
        holder.itemView.setOnClickListener {
            listener.onEventClick(news)
        }
    }

    class MyViewHolder(val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(stories: ListStory) {
            with(binding){
                itemTitle.text = stories.name
                itemDescription.text = stories.description
            }
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .into(binding.itemImage)
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(stories.photoUrl)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        // untuk memeriksa apakah suatu data masih sama atau tidak
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }
        }
    }
}