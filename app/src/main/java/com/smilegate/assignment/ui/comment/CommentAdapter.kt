package com.smilegate.assignment.ui.comment

import CommentViewHolder.CommentViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.smilegate.assignment.domain.Comment

class CommentAdapter(
    private val onDeleteClickListener: OnDeleteClickListener
) : ListAdapter<Comment, CommentViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    class OnDeleteClickListener(private val onClickListener: (Comment) -> Unit) {
        fun onClick(comment: Comment) = onClickListener(comment)
    }
}
