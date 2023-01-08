package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.repository.PostRepository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0,
    sharesCount = 0,
    viewsCount = 0,
    videoLink = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun save() {
        edited.value?.let {
            repository.save(it, object : Callback<Post> {
                override fun onSuccess(post: Post) {
                    _postCreated.postValue(Unit)
                    val newPosts = _data.value?.posts.orEmpty().map { if (it.id == post.id) post else it }
                    _data.postValue(data.value?.copy(posts = newPosts))
                }

                override fun onError(e: Exception) {
                }
            })
        }
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

    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        val post = old.first { it.id == id }

        if (post.likedByMe) {
            repository.disLikeById(id, object : Callback<Post> {
                override fun onSuccess(likedPost: Post) {
                    val newPosts = _data.value?.posts.orEmpty().map { if (it.id == id) likedPost else it }
                    _data.postValue(data.value?.copy(posts = newPosts))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        } else {
            repository.likeById(id, object : Callback<Post> {
                override fun onSuccess(likedPost: Post) {
                    val newPosts = _data.value?.posts.orEmpty().map { if (it.id == id) likedPost else it }
                    _data.postValue(data.value?.copy(posts = newPosts))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )

        repository.removeById(id, object : Callback<Unit> {
            override fun onSuccess(value: Unit) {
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }
}