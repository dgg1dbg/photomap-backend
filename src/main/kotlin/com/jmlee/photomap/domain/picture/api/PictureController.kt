package com.jmlee.photomap.domain.picture.api

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.picture.service.PictureService
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/picture")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tag(name = "Picture API", description = "Operations related to pictures")
class PictureController(
    private val pictureService: PictureService
) {
    @GetMapping("/view/{id}")
    @Operation(summary = "Get a picture by its ID", description = "Fetches a picture based on the provided ID.")
    fun view(@PathVariable id: Long): PictureDto.PictureResponse {
        val picture = pictureService.view(id)
        return PictureDto.PictureResponse(picture)
    }
    @GetMapping("/viewAll")
    @Operation(summary = "Get all pictures", description = "Fetches a list of all pictures stored in the system.")
    fun viewAll(): List<PictureDto.PictureResponse> {
        val pictures: List<Picture> = pictureService.viewAll()
        return pictures.map {PictureDto.PictureResponse(it)}
    }
    @GetMapping("/{dir}")
    @Operation(summary = "Get a picture file by its directory", description = "Fetches a picture file based on the directory name provided.")
    fun get(@PathVariable dir: String): ResponseEntity<Resource>{
        val resource = pictureService.load(dir)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource)

    }

}