package com.jmlee.photomap.global.common.exception

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Represents an error response")
data class ErrorResponse(
    @JsonProperty("errorCode")
    @Schema(description = "The error code representing the type of error", example = "404")
    val errorCode: Int,

    @JsonProperty("errorMessage")
    @Schema(description = "A brief message describing the error", example = "Resource not found")
    val errorMessage: String = "",

    @Schema(description = "Detailed information about the error", example = "The requested resource does not exist")
    val detail: String = ""
)