package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if(post.likedByMe) {
                likes.setImageResource(R.drawable.ic_heart_red_24)
            } else {
                likes.setImageResource(R.drawable.ic_heart_24)
            }
            likesText.text = formatCounterStr(post.likesCount)
            sharesText.text = formatCounterStr(post.sharesCount)
            viewText.text = formatCounterStr(post.viewsCount)

            likes.setOnClickListener{
                onLikeListener(post)
            }
            
            share.setOnClickListener{
                onShareListener(post)
            }
        }
    }

    private fun formatCounterStr(count: Int): String {
        return when(count) {
            in 0..999 -> count.toString()
            in 1000..1099 -> (count/1000).toString() + "K"
            in 1100..9999 -> ((count/100).toFloat()/10).toString() + "K"
            in 10000..999999 -> (count/1000).toString() + "K"
            in 1000000..1099999 -> (count/1000000).toString() + "M"
            else -> ((count/100000).toFloat()/10).toString() + "M"
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}