package com.jmlee.photomap.domain.picture.repository

import com.jmlee.photomap.domain.picture.model.Picture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PictureRepository: JpaRepository<Picture, Long> {
    fun findPictureById(id: Long): Picture?
    @Modifying
    @Query("DELETE FROM Picture p WHERE p.id IN :ids")
    fun deletePictures(@Param("ids") ids: List<Long?>?)
    @Modifying
    @Query("DELETE FROM Picture p WHERE p IN :pictures")
    fun deleteAllInBatch(pictures: List<Picture>)
    @Query("SELECT p FROM Picture p WHERE p.fileDir = :fileDir")
    fun findByPictureByFileDir(@Param("fileDir") fileDir: String): Picture?
}