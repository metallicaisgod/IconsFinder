package com.kirillm.iconsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IconDTO(
    @SerialName("icon_id") val iconId: Long,
    @SerialName("raster_sizes") val rasterSizes: List<RasterSizeDTO>,
)
