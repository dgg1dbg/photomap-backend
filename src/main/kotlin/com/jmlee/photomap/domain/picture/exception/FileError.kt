package com.jmlee.photomap.domain.picture.exception

import com.jmlee.photomap.global.common.exception.ConflictException
import com.jmlee.photomap.global.common.exception.ErrorType

class FileError(desc:String = "File Upload Error"): ConflictException(ErrorType.SERVER_ERROR, desc)