package com.jmlee.photomap.domain.post.dto

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.user.dto.UserDto
import java.time.LocalDate
import java.time.LocalTime
import io.swagger.v3.oas.annotations.media.Schema

class PostDto {

    @Schema(description = "Request to create a new post")
    data class PostCreateRequest(
        @Schema(description = "Name of the post", example = "My Vacation Post")
        val name: String,
        @Schema(description = "Hashtag associated with the post", example = "#Vacation")
        val hashtag: String,
        @Schema(description = "Date of the post", example = "2025-01-01")
        val date: LocalDate,
        @Schema(description = "Time of the post", example = "12:00:00")
        val time: LocalTime,
        @Schema(description = "Description of the post", example = "This is a description of my vacation post.")
        val description: String,
    )

    @Schema(description = "Request to edit an existing post")
    data class PostEditRequest(
        @Schema(description = "Updated name of the post", example = "Updated Vacation Post")
        val name: String,
        @Schema(description = "Updated hashtag associated with the post", example = "#UpdatedVacation")
        val hashtag: String,
        @Schema(description = "Updated date of the post", example = "2025-01-02")
        val date: LocalDate,
        @Schema(description = "Updated time of the post", example = "14:00:00")
        val time: LocalTime,
        @Schema(description = "Updated description of the post", example = "This is an updated description of my vacation post.")
        val description: String,
    )

    @Schema(description = "Response containing a summarized post element")
    data class ListElementResponse(
        @Schema(description = "ID of the post", example = "1")
        val id: Long,
        @Schema(description = "Name of the post", example = "My Vacation Post")
        val name: String,
        @Schema(description = "Date of the post", example = "2025-01-01")
        val date: LocalDate
    ) {
        constructor(post: Post) : this(
            id = post.id,
            name = post.name,
            date = post.date,
        )
    }

    @Schema(description = "Detailed response for a single post")
    data class PostResponse(
        @Schema(description = "Name of the post", example = "My Vacation Post")
        val name: String,
        @Schema(description = "Hashtag associated with the post", example = "#Vacation")
        val hashtag: String,
        @Schema(description = "Date of the post", example = "2025-01-01")
        val date: LocalDate,
        @Schema(description = "Time of the post", example = "12:00:00")
        val time: LocalTime,
        @Schema(description = "Description of the post", example = "This is a description of my vacation post.")
        val description: String,
        @Schema(description = "Details of the user who created the post")
        val user: UserDto.UserResponse,
        @Schema(description = "List of pictures associated with the post")
        val pictures: List<PictureDto.PictureResponse>
    ) {
        constructor(post: Post) : this(
            name = post.name,
            hashtag = post.hashtag,
            date = post.date,
            time = post.time,
            description = post.description,
            user = UserDto.UserResponse(post.user),
            pictures = post.pictures.map { PictureDto.PictureResponse(it) }
        )
    }

    @Schema(description = "Response for a successful deletion operation")
    data class PostDeleteResponse(
        @Schema(description = "Success message", example = "Success")
        val success: String = "Success",
    )
}