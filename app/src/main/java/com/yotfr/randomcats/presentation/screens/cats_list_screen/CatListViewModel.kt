package com.yotfr.randomcats.presentation.screens.cats_list_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.UseCases
import com.yotfr.randomcats.presentation.screens.cats_list_screen.event.CatListEvent
import com.yotfr.randomcats.presentation.screens.cats_list_screen.mapper.CatListMapper
import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListModel
import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val catListMapper = CatListMapper()

    private val _state = MutableStateFlow(CatListState())
    val state = _state.asStateFlow()

    init {
        getCats()
    }

    fun onEvent(event: CatListEvent){
        when(event){
            is CatListEvent.DeleteCatFromFavorite -> {
                deleteCatFromFavorite(
                    cat = event.cat
                )
            }
            is CatListEvent.GridListItemClicked -> {
                _state.update {
                    it.copy(
                        selectedIndex = event.selectedIndex
                    )
                }
            }
        }
    }

    private fun deleteCatFromFavorite(cat: CatListModel) {
        viewModelScope.launch {
            useCases.deleteCatFromRemoteDb(
                cat = catListMapper.toDomain(
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
                                groupedCats = catListMapper.fromDomainList(
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


}
