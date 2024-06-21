package com.kirillm.icons_feature

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kirillm.icons_uikit.IconsFinderTheme


@Composable
fun IconsMainScreen(
    modifier: Modifier = Modifier,
) {
    IconsMainScreen(viewModel = viewModel(), modifier = modifier)
}

@Composable
internal fun IconsMainScreen(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val iconsItems: LazyPagingItems<IconUI> = viewModel.state.collectAsLazyPagingItems()
    val isSearching = viewModel.isSearching.collectAsState()
    IconsMainContent(
        modifier,
        iconsItems = iconsItems,
        isSearching = isSearching.value,
        onSearch = { viewModel.search(it) },
        onActiveChange = { viewModel.onToggleSearch() },
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IconsMainContent(
    modifier: Modifier = Modifier,
    iconsItems: LazyPagingItems<IconUI>,
    isSearching: Boolean,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
) {
    var query by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { onSearch(it) },
                active = isSearching,
                onActiveChange = { onActiveChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .then(modifier)
            ) {
                when (iconsItems.loadState.refresh) {
                    is LoadState.Loading -> ProgressIndicator()
                    is LoadState.Error -> ErrorMessage()
                    is LoadState.NotLoading -> IconList(iconsItems)
                }
            }
        }
    ) { _ ->

    }

}

@Composable
internal fun ErrorMessage() {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(IconsFinderTheme.colorScheme.error)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error during update", color = IconsFinderTheme.colorScheme.onError)
        }
    }
}

@Composable
internal fun ProgressIndicator() {
    CircularProgressIndicator(Modifier.padding(8.dp))
}
