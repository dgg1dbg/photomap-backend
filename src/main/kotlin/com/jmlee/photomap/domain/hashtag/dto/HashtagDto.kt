package com.jmlee.photomap.domain.hashtag.dto
import com.jmlee.photomap.domain.hashtag.model.Hashtag
import io.swagger.v3.oas.annotations.media.Schema
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.picture.model.Picture

class HashtagDto{
    @Schema(description = "Hashtag name", example = "travel", nullable = false)
    data class HashtagRequest(
        val name: String,
    )

    @Schema(description = "posts response")
    data class PostsResponse(
        val posts: List<PostDto.ListElementResponse>
    ){
        companion object {
            fun from(postsFound: List<Post>?): PostsResponse {
                return PostsResponse(postsFound?.map { PostDto.ListElementResponse(it) } ?: emptyList())
            }
        }
    }

    @Schema(description = "pictures response")
    data class PicturesResponse(
        val pictures: List<PictureDto.PictureResponse>
    ){
        companion object {
            fun from(picturesFound: List<Picture>?): PicturesResponse {
                return PicturesResponse(picturesFound?.map { PictureDto.PictureResponse(it) } ?: emptyList())
            }
        }
    }

    @Schema(description = "hashtag create response")
    data class HashtagCreateResponse(
        val success: Boolean
    )

    @Schema(description = "hashtag search response")
    data class HashtagSearchResponse(
        val hashtags: List<HashtagResponse>?
    )

    @Schema(description = "hashtag response")
    data class HashtagResponse(
        val name: String
    ){
        constructor(hashtag: Hashtag) : this(
            name = hashtag.name
        )
    }
}