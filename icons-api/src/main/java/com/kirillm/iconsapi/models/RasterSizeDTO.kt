package com.kirillm.iconsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RasterSizeDTO(
    @SerialName("formats") val formats: List<FormatDTO>,
    @SerialName("size") val size: Int,
)
