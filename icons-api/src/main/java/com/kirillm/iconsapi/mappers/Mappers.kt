package com.kirillm.iconsdata.mappers

import com.kirillm.iconsapi.models.IconDTO
import com.kirillm.iconsdata.model.Icon

fun IconDTO.mapToDomain(needSize: Int = 512, needFormat: String = "png") = Icon(
    id = iconId,
    url = this.rasterSizes.firstOrNull{rasterSizeDTO ->
        rasterSizeDTO.size == needSize
    }?.formats?.firstOrNull{format ->
        format.format == needFormat
    }?.previewUrl
)