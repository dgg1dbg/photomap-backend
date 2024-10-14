package com.jmlee.photomap.global.common.exception

abstract class CustomException(val errorType: ErrorType, val detail: String) : RuntimeException()

abstract class InvalidRequestException(errorType: ErrorType, detail: String = "") : CustomException(errorType, detail)
abstract class DataNotFoundException(errorType: ErrorType, detail: String = "") : CustomException(errorType, detail)
abstract class NotAllowedException(errorType: ErrorType, detail: String = "") : CustomException(errorType, detail)
abstract class ConflictException(errorType: ErrorType, detail: String = "") : CustomException(errorType, detail)