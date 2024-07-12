package me.oikvpqya.apps.music.feature.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
class MainViewModel : ViewModel() {

    data class UiState(
//    val isCollapsingBottomBar: Boolean,
//    val isScrollingProgressMainContent: Boolean,
        val isCollapsingPlayer: Boolean
    )

    private val _uiStateFlow = MutableStateFlow(
        UiState(
            isCollapsingPlayer = true
        )
    )

    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun setCollapsingPlayerFlag(flag: Boolean) {
        _uiStateFlow.value = uiStateFlow.value.copy(isCollapsingPlayer = flag)
    }
}
