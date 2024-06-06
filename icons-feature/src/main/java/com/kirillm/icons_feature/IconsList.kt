package com.kirillm.icons_feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
internal fun IconList(
    pagingItems: LazyPagingItems<IconUI>,
) {
    val lazyListState = rememberLazyGridState()

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(4), state = lazyListState) {

            if (pagingItems.loadState.prepend is LoadState.Loading) {
                item { ProgressIndicator() }
            }

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) {
                val item = pagingItems[it]
                if (item != null) Icon(item)
            }

            if (pagingItems.loadState.append is LoadState.Loading) {
                item { ProgressIndicator() }
            }
        }
        if (pagingItems.loadState.append is LoadState.Error) {
            ErrorMessage()
        }
    }
}

@Composable
internal fun Icon(
    icon: IconUI,
) {
    Box(
        Modifier
            .padding(bottom = 4.dp)
            .fillMaxSize()
    ) {
        var isImageVisible by remember { mutableStateOf(true) }
        if (isImageVisible) {
            AsyncImage(
                model = icon.iconUrl,
                onState = { state ->
                    if (state is AsyncImagePainter.State.Error) {
                        isImageVisible = false
                    }
                },
                contentDescription = "Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}