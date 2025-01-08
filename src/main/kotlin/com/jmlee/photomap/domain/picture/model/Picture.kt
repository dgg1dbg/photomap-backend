package com.jmlee.photomap.domain.picture.model

import com.jmlee.photomap.domain.model.BaseTimeEntity
import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.post.model.Post
import jakarta.persistence.*
import org.slf4j.LoggerFactory
import java.io.File


@Entity
class Picture(
    @Column
    var fileDir: String,
    @Column
    var longitude: Double = 0.0,
    @Column
    var latitude: Double = 0.0,
    @Column
    var description: String,
    @JoinColumn(name = "post")
    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post? = null
): BaseTimeEntity() {
    constructor(dir: String, pictureCreateRequest: PictureDto.PictureCreateRequest, file: File) : this(
        fileDir = dir,
        description = pictureCreateRequest.description,
        longitude = pictureCreateRequest.coordinate[0],
        latitude = pictureCreateRequest.coordinate[1]

    )

    @PreRemove
    fun deleteFile() {
        val file = File(fileDir)
        if (file.exists()) {
            if (!file.delete()) {
                LoggerFactory.getLogger(Picture::class.java).warn("Failed to delete file: $fileDir")
            }
        }
    }

}