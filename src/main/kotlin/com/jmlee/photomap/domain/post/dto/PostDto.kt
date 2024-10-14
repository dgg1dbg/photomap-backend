package com.jmlee.photomap.domain.post.dto

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PostDto {
    data class CreateRequest(
        val name: String,
        val hashtag: String,
        val date: LocalDate,
        val time: LocalTime,
        val description: String,
    )
    data class EditRequest(
        val name: String,
        val hashtag: String,
        val date: LocalDate,
        val time: LocalTime,
        val description: String,
    )
    data class ListElementResponse(
        val id: Long,
        val name: String,
        val date: LocalDate
    ){
        constructor(post: Post) : this(
            id = post.id,
            name = post.name,
            date = post.date,
        )
    }
    data class Response(
        val name: String,
        val hashtag: String,
        val date: LocalDate,
        val time: LocalTime,
        val description: String,
        val user: UserDto.Response,
        val pictures: List<PictureDto.Response>
    ){
        constructor(post: Post) : this(
            name = post.name,
            hashtag = post.hashtag,
            date = post.date,
            time = post.time,
            description = post.description,
            user = UserDto.Response(post.user),
            pictures = post.pictures.map { PictureDto.Response(it) }
        )
    }
    data class DeleteResponse(
        val success: String = "Success",
    )

}