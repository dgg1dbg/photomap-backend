package com.jmlee.photomap.domain.picture.service

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.picture.exception.FileError
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.picture.model.PictureNotFoundException
import com.jmlee.photomap.domain.picture.repository.PictureRepository
import com.jmlee.photomap.global.common.exception.ConflictException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.util.UUID;
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths

@Service
class PictureService(
    private val pictureRepository: PictureRepository,
    @Value("\${app.upload.dir}") private val uploadDir: String
) {
    private val logger = LoggerFactory.getLogger(PictureService::class.java)
    fun create(pictureCreateRequest: PictureDto.CreateRequest): Picture{

        val uuid = UUID.randomUUID().toString()
        val pictureFileName = uuid + "_" + pictureCreateRequest.file.getOriginalFilename()
        val destinationFile = File(uploadDir + pictureFileName)
        try {
            pictureCreateRequest.file.transferTo(destinationFile)
        } catch(e: IOException) {
            throw FileError(e.message?: "")
        }
        val newPicture = Picture(pictureFileName, pictureCreateRequest, destinationFile)
        return pictureRepository.save(newPicture)
    }
    fun view(id: Long): Picture {
        val picture = pictureRepository.findPictureById(id) ?: throw PictureNotFoundException()
        return picture
    }
    fun viewAll(): List<Picture> {
        return pictureRepository.findAll()
    }
    fun load(fileName: String): Resource {
        try {
            val filePath: Path = Paths.get(uploadDir).resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists() && resource.isReadable) {
                return resource
            } else {
                throw FileNotFoundException("File not found: $fileName")
            }
        } catch (ex: Exception) {
            throw FileError("Error loading file: ${ex.message}")
        }
    }
}