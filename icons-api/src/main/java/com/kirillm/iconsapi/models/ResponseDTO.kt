package com.kirillm.iconsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDTO<E>(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("icons") val icons: List<E>
)
