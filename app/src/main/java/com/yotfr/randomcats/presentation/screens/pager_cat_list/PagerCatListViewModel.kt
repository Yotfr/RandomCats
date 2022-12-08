package com.yotfr.randomcats.presentation.screens.pager_cat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.UseCases
import com.yotfr.randomcats.presentation.screens.pager_cat_list.event.PagerCatListEvent
import com.yotfr.randomcats.presentation.screens.pager_cat_list.event.PagerCatListScreenEvent
import com.yotfr.randomcats.presentation.screens.pager_cat_list.mapper.PagerCatListMapper
import com.yotfr.randomcats.presentation.screens.pager_cat_list.model.PagerCatListModel
import com.yotfr.randomcats.presentation.screens.pager_cat_list.model.PagerCatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagerCatListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val pagerCatListMapper = PagerCatListMapper()

    private val _state = MutableStateFlow(PagerCatListState())
    val state = _state.asStateFlow()

    private val _event = Channel<PagerCatListScreenEvent>()
    val event = _event.receiveAsFlow()

    init {
        getCats()
    }

    fun onEvent(event: PagerCatListEvent){
        when(event){
            is PagerCatListEvent.DeleteCatFromFavorite -> {
                deleteCatFromFavorite(
                    cat = event.cat
                )
            }
            is PagerCatListEvent.BackArrowPressed -> {
                sendToUi(PagerCatListScreenEvent.NavigateToGridCatList(
                    selectedIndex = event.selectedIndex
                ))
            }
        }
    }

    private fun deleteCatFromFavorite(cat: PagerCatListModel) {
        viewModelScope.launch {
            useCases.deleteCatFromRemoteDb(
                cat = pagerCatListMapper.toDomain(
                    uiModel = cat
                )
            )
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            useCases.getCatsFromRemoteDb().collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                cats = pagerCatListMapper.fromDomainList(
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

    private fun sendToUi(uiEvent: PagerCatListScreenEvent) {
        viewModelScope.launch {
            _event.send(uiEvent)
        }
    }


}
