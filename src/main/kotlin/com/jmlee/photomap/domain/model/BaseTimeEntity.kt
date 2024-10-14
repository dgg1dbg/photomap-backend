package com.jmlee.photomap.domain.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
open class BaseTimeEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @CreatedDate
    var createdDate: LocalDateTime? = null,
    @LastModifiedDate
    var modifiedDate: LocalDateTime? = null,
)