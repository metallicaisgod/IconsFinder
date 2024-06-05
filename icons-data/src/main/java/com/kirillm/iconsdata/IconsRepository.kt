package com.kirillm.iconsdata

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kirillm.iconsdata.model.Icon
import com.kirillm.iconspaging.IconsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IconsRepository @Inject constructor(
    private val iconsPagingSourceFactory: IconsPagingSource.Factory,
) {

    suspend fun getIcons(query: String): Flow<PagingData<Icon>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20,
        ),
        pagingSourceFactory = { iconsPagingSourceFactory.create(query) }
    ).flow
}