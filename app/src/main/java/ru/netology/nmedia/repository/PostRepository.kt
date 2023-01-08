package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun disLikeById(id: Long, callback: Callback<Post>)
    fun share(id: Long)
    fun save(post: Post, callback: Callback<Post>)
    fun removeById(id: Long, callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(value: T) {}
        fun onError(e: Exception) {}
    }
}