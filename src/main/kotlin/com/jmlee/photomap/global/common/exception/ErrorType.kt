package com.jmlee.photomap.global.common.exception

enum class ErrorType(
    val code: Int
) {
    INVALID_REQUEST(0),
    NOT_ALLOWED(3000),
    USER_NOT_ALLOWED(3001),
    DATA_NOT_FOUND(4000),
    USER_NOT_FOUND(4001),
    POST_NOT_FOUND(4002),
    PICTURE_NOT_FOUND(4003),
    CONFLICT(9000),
    USER_ALREADY_EXISTS(9001),
    HASHTAG_ALREADY_EXISTS(9002),
    SERVER_ERROR(10000)
}