package com.yotfr.randomcats.presentation.screens.random_cat_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.use_case.cats.UseCases
import com.yotfr.randomcats.presentation.screens.random_cat_screen.event.RandomCatEvent
import com.yotfr.randomcats.presentation.screens.random_cat_screen.model.RandomCatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RandomCatViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(RandomCatState())
    val state: State<RandomCatState> = _state


    init {
        getCat()
    }

    fun onEvent(event: RandomCatEvent) {
        when (event) {
            RandomCatEvent.FavCat -> {
                viewModelScope.launch {
                    useCases.uploadCatToRemoteDbUseCase(
                        cat = _state.value.cat?.copy(
                            created = getCurrentDay()
                        ) ?: throw Exception(
                            "Trying to upload null value"
                        )
                    )
                }
                getCat()
            }
            RandomCatEvent.GetNewCat -> {
                getCat()
            }
        }
    }

    private fun getCat() {
        viewModelScope.launch {
            useCases.getRandomCat().collectLatest { result ->
                when(result) {
                    is MResult.Loading -> {
                        _state.value = RandomCatState(isLoading = true)
                    }
                    is MResult.Success -> {
                        _state.value = RandomCatState(cat = result.data)
                    }
                    is MResult.Error -> {
                        _state.value = RandomCatState(error = result.message ?:
                        "Unknown error occured")
                    }
                }
            }
        }
    }

    private fun getCurrentDay():Long {
        return Calendar.getInstance(Locale.getDefault()).timeInMillis
    }
}