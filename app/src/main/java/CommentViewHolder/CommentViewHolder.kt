package CommentViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.assignment.domain.Comment
import com.smilegate.assignment.databinding.ItemCommentBinding
import com.smilegate.assignment.ui.comment.CommentAdapter

class CommentViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        comment: Comment,
        onDeleteClickListener: CommentAdapter.OnDeleteClickListener
    ) {
        binding.comment = comment
        binding.onDeleteClickListener = onDeleteClickListener
    }

    companion object {
        fun from(parent: ViewGroup): CommentViewHolder {
            return ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).let { CommentViewHolder(it) }
        }
    }
}
