package com.yotfr.randomcats.presentation.screens.cats_list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.UseCases
import com.yotfr.randomcats.presentation.screens.cats_list_screen.event.CatListEvent
import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(CatListState())
    val state: State<CatListState> = _state

    init {
        getCats()
    }

    fun onEvent(event: CatListEvent){
        when(event){
            is CatListEvent.DeleteCatFromFavorite -> {
                viewModelScope.launch {
                    useCases.deleteCatFromRemoteDb(
                        cat = event.cat
                    )
                }
            }
        }
    }

    private fun getCats() {
        viewModelScope.launch {
            useCases.getCatsFromRemoteDb().collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        //TODO:refactor fun groupCatsByDate add today yesterday
                        _state.value = CatListState(
                            groupedCats = groupCatsByDate(
                                result.data ?: emptyList()
                            )
                        )
                    }
                    else -> {
                        //TODO: exception handling
                    }
                }
            }
        }
    }

    private suspend fun groupCatsByDate(list: List<Cat>):Map<String, List<Cat>> =
        withContext(Dispatchers.Default){
            list.groupBy { it.createdDateString.substringBeforeLast(", ") }
        }

}
