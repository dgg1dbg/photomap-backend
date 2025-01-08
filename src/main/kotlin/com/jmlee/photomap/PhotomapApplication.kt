package com.jmlee.photomap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
	info = Info(
		title = "PhotoMap API",
		version = "1.0"
	)
)

@SpringBootApplication
class PhotomapApplication

fun main(args: Array<String>) {
	runApplication<PhotomapApplication>(*args)
}
