package com.kirillm.icons_feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.kirillm.iconsdata.IconsRepository
import com.kirillm.iconsdata.model.Icon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val iconsRepository: IconsRepository,
) : ViewModel() {

    private val _query: MutableStateFlow<String> = MutableStateFlow("")

    val query: StateFlow<String>
        get() = _query.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<PagingData<IconUI>> = query
        .map { query ->
            if(query.isNotEmpty()) {
                iconsRepository.getIcons(
                    query
                )
            } else {
                flowOf(PagingData.empty())
            }
        }
        .flatMapConcat { pagingDataFlow ->
            pagingDataFlow.map { pagingData ->
                pagingData.map(Icon::mapToUI)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun search(s: String) {
        _query.value = s
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            search("")
        }
    }

}