package com.kirillm.iconsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormatDTO(
    @SerialName("format") val format: String,
    @SerialName("preview_url") val previewUrl: String,
)
