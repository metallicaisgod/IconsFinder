package com.kirillm.icons_feature

import com.kirillm.iconsdata.model.Icon

fun Icon.mapToUI() = IconUI(
    id = id,
    iconUrl = url ?: "",
)