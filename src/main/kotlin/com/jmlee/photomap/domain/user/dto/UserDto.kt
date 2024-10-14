package com.jmlee.photomap.domain.user.dto

import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.user.model.User

class UserDto {
    data class SignUpRequest(
        val username: String,
        val email: String,
        val password: String,
        val description: String? = null,
    )
    data class SignInRequest(
        val email: String,
        val password: String,
    )
    data class EditRequest(
        val username: String?,
        val password: String?,
        val description: String? = null
    )
    data class DeleteRequest(
        val username: String,
    )

    data class Response(
        val username: String,
        val email: String,
        val description: String? = null
    ){
        constructor(user: User) : this(user.username, user.email, user.description)
    }
    data class ViewResponse(
        val username: String,
        val email: String,
        val description: String?,
        val posts: List<PostDto.ListElementResponse>?,
    ){
        constructor(user: User): this(
            username = user.username,
            email = user.email,
            description = user.description,
            posts = user.posts.map { PostDto.ListElementResponse(it) },
        )
    }
    data class DeleteResponse(
        val success: String = "Success",
    )
}