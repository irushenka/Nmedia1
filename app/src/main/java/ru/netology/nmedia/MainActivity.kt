package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетололгия. Университет интернет-профессий",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18.36",
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if(post.likedByMe) {
                likes.setImageResource(R.drawable.ic_heart_red_24)
            }
            likesText.text = formatCounterStr(post.likesCount)
            sharesText.text = formatCounterStr(post.sharesCount)
            viewText.text = formatCounterStr(post.viewsCount)

            likes.setOnClickListener{
                if(post.likedByMe) {
                    post.likedByMe = false
                    post.likesCount--
                    likes.setImageResource(R.drawable.ic_heart_24)
                } else {
                    post.likedByMe = true
                    post.likesCount++
                    likes.setImageResource(R.drawable.ic_heart_red_24)
                }
                likesText.text = formatCounterStr(post.likesCount)
            }

            share.setOnClickListener{
                post.sharesCount += 10
                sharesText.text = formatCounterStr(post.sharesCount)
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