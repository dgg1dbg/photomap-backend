package com.jmlee.photomap.domain.picture.api

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.picture.repository.PictureRepository
import com.jmlee.photomap.domain.picture.service.PictureService
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/picture")
@CrossOrigin(origins = ["http://localhost:3000"])
class PictureController(
    private val pictureService: PictureService
) {
    @GetMapping("/view/{id}")
    fun view(@PathVariable id: Long): PictureDto.Response {
        val picture = pictureService.view(id)
        return PictureDto.Response(picture)
    }
    @GetMapping("/viewAll")
    fun viewAll(): List<PictureDto.Response> {
        val pictures: List<Picture> = pictureService.viewAll()
        return pictures.map {PictureDto.Response(it)}
    }
    @GetMapping("/{dir}")
    fun get(@PathVariable dir: String): ResponseEntity<Resource>{
        val resource = pictureService.load(dir)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource)

    }

}