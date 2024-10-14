package com.jmlee.photomap.domain.picture.dto

import com.jmlee.photomap.domain.picture.model.Picture
import org.springframework.web.multipart.MultipartFile

class PictureDto {
    data class CreateRequest(
        val description: String = "",
        val file: MultipartFile,
        val coordinate: List<Double>
    )
    data class EditRequest(
        val id: Long,
        val description: String = "",
        val coordinate: List<Double>,
    )
    data class Response(
        val id: Long,
        val fileDir: String,
        val longitude: Double,
        val latitude: Double,
        val description: String,
        val postId: Long?,
    ){
        constructor(picture: Picture) : this(
            id = picture.id,
            fileDir = picture.fileDir,
            longitude = picture.longitude,
            latitude = picture.latitude,
            description = picture.description,
            postId = picture.post!!.id,
        )
    }
}