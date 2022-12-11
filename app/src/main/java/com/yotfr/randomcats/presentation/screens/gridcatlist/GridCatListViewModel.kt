package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.CatsUseCases
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListScreenEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.mapper.GridCatListMapper
import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListModel
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

    private val _event = Channel<GridCatListScreenEvent>()
    val event = _event.receiveAsFlow()

    init {
        getCats()
    }

    fun onEvent(event: GridCatListEvent){
        when(event){
            is GridCatListEvent.DeleteCatFromFavorite -> {
                deleteCatFromFavorite(
                    cat = event.cat
                )
            }
            is GridCatListEvent.GridCatListItemClicked -> {
                sendToUi(
                    GridCatListScreenEvent.NavigateToPagerScreenCat(
                    selectedIndex = event.selectedIndex
                ))
            }
        }
    }

    private fun deleteCatFromFavorite(cat: GridCatListModel) {
        viewModelScope.launch {
            catsUseCases.deleteCatFromRemoteDb(
                cat = gridCatListMapper.toDomain(
                    uiModel = cat
                )
            )
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            catsUseCases.getCatsFromRemoteDb().collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                cats = gridCatListMapper.fromDomainList(
                                    initialList =  result.data
                                )
                            )
                        }
                    }
                    else -> {
                        //TODO: exception handling
                    }
                }
            }
        }
    }

    private fun sendToUi(uiEvent: GridCatListScreenEvent) {
        viewModelScope.launch {
            _event.send(uiEvent)
        }
    }


}
