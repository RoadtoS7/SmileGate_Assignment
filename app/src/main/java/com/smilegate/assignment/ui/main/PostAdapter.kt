package com.smilegate.assignment.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.smilegate.assignment.domain.Post

class PostAdapter(private val onClickListener: OnClickListener) : ListAdapter<Post, PostViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    class OnClickListener(private val onClickListener: (Post) -> Unit) {
        fun onClick(post: Post) = onClickListener(post)
    }
}
