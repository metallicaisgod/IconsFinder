package com.kirillm.iconspaging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kirillm.iconsapi.IconsApi
import com.kirillm.iconsdata.mappers.mapToDomain
import com.kirillm.iconsdata.model.Icon
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

class IconsPagingSource @AssistedInject constructor(
    @Assisted private val query: String,
    private val iconsApi: IconsApi,
) : PagingSource<Int, Icon>() {

    override fun getRefreshKey(state: PagingState<Int, Icon>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        return try {
            val page = params.key ?: 0
            val response = iconsApi.getIcons(
                query = query,
                count = params.loadSize,
                offset = page * params.loadSize
            )
            if (response.isSuccess) {
                val icons = response.getOrThrow().icons.map { it.mapToDomain() }
                LoadResult.Page(
                    data = icons,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (icons.isEmpty()) null else page + 1
                )
            }
            else {
                Log.e("IconsPagingSource", response.exceptionOrNull()?.message ?: "Unknown error")
                LoadResult.Error(response.exceptionOrNull() ?: RuntimeException("Unknown error"))
//                LoadResult.Invalid()
            }
        } catch (e: IOException) {
            Log.e("IconsPagingSource", e.message ?: "IOException error")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("IconsPagingSource", e.message ?: "HttpException error")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("IconsPagingSource", e.message ?: "Exception error")
            LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted query: String): IconsPagingSource
    }

}