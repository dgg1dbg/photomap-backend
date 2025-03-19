package com.jmlee.photomap.domain.hashtag.api
import org.springframework.web.bind.annotation.*
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.global.auth.CurrentUser
import com.jmlee.photomap.domain.hashtag.service.HashtagService
import com.jmlee.photomap.domain.hashtag.dto.HashtagDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag


@RestController
@RequestMapping("/api/hashtag")
@Tag(name = "Hashtag API", description = "Hashtag API")
class HashtagController(
    private val hashtagService: HashtagService
)    
{
    @GetMapping("/post/{hashtag}")
    @Operation(
        summary = "Get posts by hashtag",
        description = "Retrieves all posts associated with a specific hashtag."
    )
    fun getPosts(@CurrentUser user: User, @PathVariable hashtag: String): HashtagDto.PostsResponse {
        val posts = hashtagService.getPosts(hashtag)
        return HashtagDto.PostsResponse.from(posts)
    }
    @GetMapping("/picture/{hashtag}")
    @Operation(
        summary = "Get pictures by hashtag",
        description = "Retrieves all pictures associated with a specific hashtag."
    )
    fun getPictures(@CurrentUser user: User, @PathVariable hashtag: String): HashtagDto.PicturesResponse{
        val pictures = hashtagService.getPictures(hashtag)
        return HashtagDto.PicturesResponse.from(pictures)
    }
    @PutMapping("/create/{hashtag}")
    @Operation(
        summary = "Create a hashtag",
        description = "Allows a user to create a new hashtag"
    )
    fun create(@CurrentUser user: User, @PathVariable hashtag: String): HashtagDto.HashtagCreateResponse{
        val newHashtag = hashtagService.create(hashtag)
        return HashtagDto.HashtagCreateResponse(true)
    }
    @GetMapping("/search/{hashtag}")
    @Operation(
        summary = "Search for a hashtag",
        description = "Searches for a hashtag by name."
    )
    fun search(@CurrentUser user: User, @PathVariable hashtag: String): HashtagDto.HashtagSearchResponse{
        val foundHashtags = hashtagService.search(hashtag)
        val hashTagResponse = foundHashtags?.map { HashtagDto.HashtagResponse(it) }
        return HashtagDto.HashtagSearchResponse(hashTagResponse)
    }
    @GetMapping("/get/trending")
    fun getTrending(
        @RequestParam(name = "count", defaultValue = "10") count: Int
    ): HashtagDto.HashtagSearchResponse{
        val trendingHashtags = hashtagService.getTrending(count)
        val hashTagResponse = trendingHashtags?.map { HashtagDto.HashtagResponse(it) }
        return HashtagDto.HashtagSearchResponse(hashTagResponse)
    }
}
        
    