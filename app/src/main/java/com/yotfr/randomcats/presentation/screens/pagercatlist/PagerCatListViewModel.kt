package com.yotfr.randomcats.presentation.screens.pagercatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.CatsUseCases
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListEvent
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListScreenEvent
import com.yotfr.randomcats.presentation.screens.pagercatlist.mapper.PagerCatListMapper
import com.yotfr.randomcats.presentation.screens.pagercatlist.model.PagerCatListModel
import com.yotfr.randomcats.presentation.screens.pagercatlist.model.PagerCatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagerCatListViewModel @Inject constructor(
    private val catsUseCases: CatsUseCases
) : ViewModel() {

    private val pagerCatListMapper = PagerCatListMapper()

    private val _state = MutableStateFlow(PagerCatListState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<PagerCatListScreenEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getCats()
    }

    fun onEvent(event: PagerCatListEvent) {
        when (event) {
            is PagerCatListEvent.DeleteCatClicked -> {
                deleteCatFromFavorite(
                    cat = event.cat
                )
            }
            is PagerCatListEvent.BackArrowPressed -> {
                sendToUi(
                    PagerCatListScreenEvent.NavigateToGridCatList(
                        selectedIndex = event.selectedIndex
                    )
                )
            }
            is PagerCatListEvent.PageChanged -> {
                changeDisplayDateTime(event)
            }
            is PagerCatListEvent.ChangeBitmap -> {
                changeBitmap(event)
            }
            PagerCatListEvent.OnScreenClicked -> {
                changeSystemBarsVisibility()
            }
        }
    }

    private fun changeBitmap(event: PagerCatListEvent.ChangeBitmap) {
        _state.update {
            it.copy(
                currentlyDisplayedImageBitmap = event.bitmap
            )
        }
    }

    private fun changeSystemBarsVisibility() {
        _state.update {
            it.copy(
                isSystemBarsVisible = !it.isSystemBarsVisible
            )
        }
    }

    private fun changeDisplayDateTime(event: PagerCatListEvent.PageChanged) {
        _state.update {
            it.copy(
                displayDate = it.cats[event.pageIndex].createdDateString.substringBeforeLast(
                    " "
                ),
                displayTime = it.cats[event.pageIndex].createdDateString.substringAfterLast(
                    " "
                ),
                currentPage = event.pageIndex
            )
        }
    }

    private fun deleteCatFromFavorite(cat: PagerCatListModel) {
        viewModelScope.launch {
            catsUseCases.deleteCatFromRemoteDb(
                cat = pagerCatListMapper.toDomain(
                    uiModel = cat
                )
            ).collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        // navigate back to grid screen when in pager screen nothing to show
                        if (_state.value.cats.isEmpty()) {
                            sendToUi(PagerCatListScreenEvent.NavigateBack)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            catsUseCases.getCatsFromRemoteDb().collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                cats = pagerCatListMapper.fromDomainList(
                                    initialList = result.data
                                )
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun sendToUi(uiEvent: PagerCatListScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
