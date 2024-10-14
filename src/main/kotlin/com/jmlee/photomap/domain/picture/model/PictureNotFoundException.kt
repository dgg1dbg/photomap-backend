package com.jmlee.photomap.domain.picture.model

import com.jmlee.photomap.global.common.exception.DataNotFoundException
import com.jmlee.photomap.global.common.exception.ErrorType

class PictureNotFoundException: DataNotFoundException(ErrorType.PICTURE_NOT_FOUND, "Picture not found")