package ru.netology.nmedia.viewmodel

import PostRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryFileImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likesCount = 0,
    sharesCount = 0,
    viewsCount = 0,
    videoLink = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun clear() {
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun share(id: Long) = repository.share(id)
}