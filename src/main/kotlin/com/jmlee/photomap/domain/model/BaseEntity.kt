package com.jmlee.photomap.domain.model

import jakarta.persistence.*

@MappedSuperclass
open class BaseEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
)