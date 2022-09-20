package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels()
    val binding: ActivityMainBinding by lazy {ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        subscribe()
        setupListeners()
    }

    private fun subscribe() {
        viewModel.data.observe(this) { post ->
            with(binding) {
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
            }
        }
    }

    private fun setupListeners() {
        binding.likes.setOnClickListener{
            viewModel.like()
        }

        binding.share.setOnClickListener{
            viewModel.share()
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