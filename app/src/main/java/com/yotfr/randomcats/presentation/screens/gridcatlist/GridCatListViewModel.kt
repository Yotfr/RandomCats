package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.usecase.cats.CatsUseCases
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListScreenEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.mapper.GridCatListMapper
import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GridCatListViewModel @Inject constructor(
    private val catsUseCases: CatsUseCases
) : ViewModel() {

    private val gridCatListMapper = GridCatListMapper()

    private val _state = MutableStateFlow(GridCatListState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<GridCatListScreenEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getCats()
    }

    fun onEvent(event: GridCatListEvent) {
        when (event) {
            is GridCatListEvent.GridCatListItemClicked -> {
                sendToUi(
                    GridCatListScreenEvent.NavigateToPagerScreen(
                        selectedIndex = event.selectedIndex
                    )
                )
            }
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            catsUseCases.getCatsFromRemoteDb().collectLatest { result ->
                when (result) {
                    is Response.Loading -> {
                        processLoadingState()
                    }
                    is Response.Success -> {
                        processSuccessState(result)
                    }
                    /**
                     * This shouldn't happen because non authenticated user only have access to auth
                     * graph screens, here for the unexpected cases
                     */
                    is Response.Exception -> {
                        processErrorState(result)
                    }
                }
            }
        }
    }

    private fun processErrorState(result: Response.Exception) {
        when (result.cause) {
            Cause.UserIsNotLoggedIn -> {
                sendToUi(GridCatListScreenEvent.NavigateToAuth)
            }
            else -> Unit
        }
    }

    private fun processSuccessState(result: Response.Success<List<Cat>>) {
        _state.update {
            it.copy(
                cats = gridCatListMapper.fromDomainList(
                    initialList = result.data
                ),
                isLoading = false
            )
        }
    }

    private fun processLoadingState() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun sendToUi(uiEvent: GridCatListScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
