package com.jmlee.photomap.global.common.exception

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(value = [DataNotFoundException::class])
    fun notFound(e: CustomException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = [InvalidRequestException::class])
    fun invalidRequest(e: InvalidRequestException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [NotAllowedException::class])
    fun notAllowed(e: NotAllowedException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.FORBIDDEN)

    @ExceptionHandler(value = [ConflictException::class])
    fun conflict(e: ConflictException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.CONFLICT)
}