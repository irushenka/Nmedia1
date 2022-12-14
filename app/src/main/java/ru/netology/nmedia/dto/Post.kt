package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var sharesCount: Int = 0,
    var viewsCount: Int = 0,
    val videoLink: String?
)
