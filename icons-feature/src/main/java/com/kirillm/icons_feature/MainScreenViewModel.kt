package com.kirillm.icons_feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.kirillm.iconsdata.IconsRepository
import com.kirillm.iconsdata.model.Icon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val iconsRepository: IconsRepository,
) : ViewModel() {

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _state: MutableStateFlow<PagingData<IconUI>> = MutableStateFlow(PagingData.empty())
    val state: StateFlow<PagingData<IconUI>> = _state.asStateFlow()

    init {
        fetchIcons()
    }

    fun search(s: String) {
        Log.d("MainScreenViewModel", "search: $s")
        query.value = s.trim()
    }

    private fun fetchIcons() {
        viewModelScope.launch {
            query.map { querySearch ->
                if (querySearch.isNotEmpty()) {
                    iconsRepository.getIcons(
                        querySearch
                    ).onEach {
                    }
                } else {
                    flowOf(PagingData.empty())
                }
            }.collect {
                _state.value = it.map { pagingData ->
                    pagingData.map(Icon::mapToUI)

                }.first()
            }
        }
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            search("")
        }
    }

}