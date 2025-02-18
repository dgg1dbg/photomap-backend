package com.jmlee.photomap.domain.picture.dto

import com.jmlee.photomap.domain.picture.model.Picture
import org.springframework.web.multipart.MultipartFile
import io.swagger.v3.oas.annotations.media.Schema

class PictureDto {

    @Schema(description = "Request to create a new picture")
    data class PictureCreateRequest(
        @Schema(description = "Description of the picture", example = "A beautiful sunset")
        val description: String = "",

        @Schema(description = "File of the picture", required = true)
        val file: MultipartFile,

        @Schema(description = "Coordinates of the picture in latitude and longitude", example = "[37.7749, -122.4194]")
        val coordinate: List<Double>
    )

    @Schema(description = "Request to edit an existing picture")
    data class PictureEditRequest(
        @Schema(description = "Description of the picture", example = "A beautiful sunset")
        val description: String = "",

        @Schema(description = "File of the picture", required = true)
        val file: Any,

        @Schema(description = "Coordinates of the picture in latitude and longitude", example = "[37.7749, -122.4194]")
        val coordinate: List<Double>
    )

    @Schema(description = "Response representing a picture")
    data class PictureResponse(
        @Schema(description = "ID of the picture", example = "1")
        val id: Long,

        @Schema(description = "Directory where the file is stored", example = "/images/sunset.jpg")
        val fileDir: String,

        @Schema(description = "Longitude of the picture's location", example = "-122.4194")
        val longitude: Double,

        @Schema(description = "Latitude of the picture's location", example = "37.7749")
        val latitude: Double,

        @Schema(description = "Description of the picture", example = "A beautiful sunset")
        val description: String,

        @Schema(description = "ID of the post this picture belongs to", example = "1", nullable = true)
        val postId: Long?,
    ) {
        constructor(picture: Picture) : this(
            id = picture.id,
            fileDir = picture.fileDir,
            longitude = picture.longitude,
            latitude = picture.latitude,
            description = picture.description,
            postId = picture.post?.id
        )
    }
}