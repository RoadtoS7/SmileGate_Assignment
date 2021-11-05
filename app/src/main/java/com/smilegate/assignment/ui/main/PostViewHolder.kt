package com.smilegate.assignment.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.assignment.domain.Post
import com.smilegate.assignment.databinding.ItemPostBinding

class PostViewHolder private constructor(
    private val binding: ItemPostBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post, onClickListener: PostAdapter.OnClickListener) {
        binding.post = post
        binding.onClickListener = onClickListener
    }

    companion object {
        fun from(parent: ViewGroup): PostViewHolder {
            return ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).let { PostViewHolder(it) }
        }
    }
}