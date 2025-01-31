package com.jmlee.photomap.domain.user.dto

import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.user.model.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class UserDto {
    @Schema(description = "Sign Up Request")
    data class SignUpRequest(
        @field:NotBlank(message = "Username cannot be blank")
        @Schema(description = "Username", example = "johndoe", nullable = false)
        val username: String,

        @field:NotBlank(message = "Email cannot be blank")
        @field:Email(message = "Invalid email format")
        @Schema(description = "User email", example = "user@example.com", nullable = false)
        val email: String,

        @field:NotBlank(message = "Password is required")
        @field:Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        @Schema(description = "User password", example = "password123", nullable = false)
        val password: String,

        @Schema(description = "User description", example = "Photography enthusiast", nullable = true)
        val description: String? = null,
    )

    @Schema(description = "Sign In Request")
    data class SignInRequest(
        @field:Email(message = "Invalid email format")
        @field:NotBlank(message = "Email cannot be blank")
        @Schema(description = "User email", example = "user@example.com")
        val email: String,

        @field:NotBlank(message = "Password cannot be blank")
        @Schema(description = "User password", example = "password123")
        val password: String,
    )

    @Schema(description = "Edit Request")
    data class UserEditRequest(
        @Schema(description = "New username", example = "newusername", nullable = true)
        val username: String?,

        @Schema(description = "New password", example = "newpassword123", nullable = true)
        val password: String?,

        @Schema(description = "New description", example = "Love taking landscape photos", nullable = true)
        val description: String? = null
    )

    @Schema(description = "Delete Request")
    data class UserDeleteRequest(
        @field:NotBlank(message = "Username cannot be blank")
        @Schema(description = "Username to delete", example = "johndoe")
        val username: String,
    )

    @Schema(description = "User Response")
    data class UserResponse(
        @Schema(description = "Username", example = "johndoe")
        val username: String,

        @Schema(description = "User email", example = "user@example.com")
        val email: String,

        @Schema(description = "User description", example = "Photography enthusiast", nullable = true)
        val description: String? = null
    ){
        constructor(user: User) : this(user.username, user.email, user.description)
    }

    @Schema(description = "User View Response")
    data class UserViewResponse(
        @Schema(description = "Username", example = "johndoe")
        val username: String,

        @Schema(description = "User email", example = "user@example.com")
        val email: String,

        @Schema(description = "User description", example = "Photography enthusiast", nullable = true)
        val description: String?,

        @Schema(description = "List of user's posts", nullable = true)
        val posts: List<PostDto.ListElementResponse>?,
    ){
        constructor(user: User): this(
            username = user.username,
            email = user.email,
            description = user.description,
            posts = user.posts.map { PostDto.ListElementResponse(it) },
        )
    }

    @Schema(description = "Delete Response")
    data class UserDeleteResponse(
        @Schema(description = "Success message", example = "Success")
        val success: String = "Success",
    )
}