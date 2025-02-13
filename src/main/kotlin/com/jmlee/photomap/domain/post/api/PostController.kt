package com.jmlee.photomap.domain.post.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.post.service.PostService
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.global.auth.CurrentUser
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalTime
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "Post API")
class PostController (
    private val postService: PostService
){
    @PostMapping("/create")
    @Operation(
        summary = "Create a new post",
        description = "Allows a user to create a new post with associated pictures, descriptions, and coordinates."
    )
    fun create(
        @CurrentUser user: User,
        @RequestParam(name = "name") name: String,
        @RequestParam(name = "hashtag") hashtag: String,
        @RequestParam(name = "date") date: LocalDate,
        @RequestParam(name = "time") time: LocalTime,
        @RequestParam(name = "description") description: String,
        @RequestParam(name = "files") files: List<MultipartFile>,
        @RequestParam(name = "descriptions") descriptions: List<String>,
        @RequestParam(name = "coordinates") coordinatesJson: String
        ): PostDto.PostCreateResponse{
        val coordinates = parseCoordinates(coordinatesJson)
        val postCreateRequest = PostDto.PostCreateRequest(name, hashtag, date, time, description)
        val pictureCreateRequests = files.mapIndexed { index, file ->  PictureDto.PictureCreateRequest(descriptions[index], file, coordinates[index])}
        val post = postService.create(user, postCreateRequest, pictureCreateRequests)
        return PostDto.PostCreateResponse(post)
    }
    @GetMapping("/view/{id}")
    @Operation(
        summary = "View a post",
        description = "Retrieves the details of a post by its ID."
    )
    fun view(@PathVariable id: Long): PostDto.PostResponse{
        val post = postService.view(id)
        return PostDto.PostResponse(post)
    }
    @PutMapping("/edit/{id}")
    @Operation(
        summary = "Edit a post",
        description = "Allows a user to edit an existing post, including adding or modifying associated pictures."
    )
    fun edit(
        @CurrentUser user: User,
        @RequestParam(name = "name") name: String,
        @RequestParam(name = "hashtag") hashtag: String,
        @RequestParam(name = "date") date: LocalDate,
        @RequestParam(name = "time") time: LocalTime,
        @RequestParam(name = "description") description: String,
        @RequestParam(name = "existingPictures") existingPictures: String = "",
        @RequestParam(name = "files") files: List<MultipartFile> = listOf(),
        @RequestParam(name = "descriptions") descriptions: List<String> = listOf(),
        @RequestParam(name = "coordinates") coordinatesJson: String = "",
        @PathVariable id: Long): PostDto.PostResponse{
        val pictureEditRequests: List<PictureDto.PictureEditRequest>

        if (existingPictures.isEmpty()) pictureEditRequests = listOf()
        else pictureEditRequests = parseEditRequest("[$existingPictures]")

        val pictureCreateRequests: List<PictureDto.PictureCreateRequest>

        if (coordinatesJson.isEmpty()) pictureCreateRequests = listOf()
        else{
            val coordinates = parseCoordinates(coordinatesJson)
            pictureCreateRequests = descriptions.mapIndexed { index, desc -> PictureDto.PictureCreateRequest(desc, files[index], coordinates[index]) }
        }
        val postEditRequest = PostDto.PostEditRequest(name, hashtag, date, time, description)


        val post = postService.edit(user, id, postEditRequest, pictureEditRequests, pictureCreateRequests)
        return PostDto.PostResponse(post)
    }
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a post",
        description = "Allows a user to delete a specific post by its ID."
    )
    fun delete(@CurrentUser user: User, @PathVariable id: Long): PostDto.PostDeleteResponse{
        postService.delete(user, id)
        return PostDto.PostDeleteResponse()
    }
    fun parseCoordinates(input: String): List<List<Double>> {
        return input
            .removeSurrounding("[", "]") // Remove the outermost brackets
            .split("],[").map { pairString ->
                pairString
                    .split(",")
                    .map { it.toDouble() }
            }
    }
    fun parseEditRequest(jsonString: String): List<PictureDto.PictureEditRequest> {
        val mapper = jacksonObjectMapper()

        // Parse the JSON string to a list of maps
        val jsonList: List<Map<String, Any>> = mapper.readValue(jsonString)

        // Convert each map to an EditRequest object
        return jsonList.mapNotNull { item ->
            try {
                PictureDto.PictureEditRequest(
                    id = (item["id"] as Number).toLong(),
                    description = item["description"] as? String ?: "",
                    coordinate = parseCoordinate(item["coordinates"])
                )
            } catch (e: Exception) {
                println("Error parsing item: $item")
                e.printStackTrace()
                null
            }
        }
    }

    fun parseCoordinate(coordinates: Any?): List<Double> {
        return when (coordinates) {
            is List<*> -> coordinates.mapNotNull { coord ->
                when (coord) {
                    is Number -> coord.toDouble()
                    is String -> coord.toDoubleOrNull()
                    else -> null
                }
            }
            is String -> coordinates.split(",").mapNotNull { it.trim().toDoubleOrNull() }
            else -> emptyList()
        }.takeIf { it.size == 2 } ?: emptyList()
    }

}