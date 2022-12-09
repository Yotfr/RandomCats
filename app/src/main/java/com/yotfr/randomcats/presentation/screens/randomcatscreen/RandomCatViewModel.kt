package com.yotfr.randomcats.presentation.screens.randomcatscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.cats.UseCases
import com.yotfr.randomcats.presentation.screens.randomcatscreen.event.RandomCatEvent
import com.yotfr.randomcats.presentation.screens.randomcatscreen.model.PeekingCatsLocations
import com.yotfr.randomcats.presentation.screens.randomcatscreen.model.RandomCatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RandomCatViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(RandomCatState())
    val state = _state.asStateFlow()


    init {
        getCat()
    }

    fun onEvent(event: RandomCatEvent) {
        when (event) {
            RandomCatEvent.FavCat -> {
                uploadCatToRemoteDb()
                changePeekingCatLocation()
                getCat()
            }
            RandomCatEvent.GetNewCat -> {
                changePeekingCatLocation()
                getCat()
            }
            RandomCatEvent.ChangePeekingCatLocation -> {
                changePeekingCatLocation()
            }
        }
    }

    private fun changePeekingCatLocation(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    peekingCatsLocation = PeekingCatsLocations.HIDDEN
                )
            }
            delay(Random.nextLong(0,5000))
            _state.update {
                it.copy(
                    peekingCatsLocation = PeekingCatsLocations.values().random()
                )
            }
        }
    }

    private fun uploadCatToRemoteDb() {
        viewModelScope.launch {
            useCases.uploadCatToRemoteDbUseCase(
                cat = _state.value.cat?.copy(
                    created = getCurrentDay()
                ) ?: throw Exception(
                    "Trying to upload null value"
                )
            ).collectLatest { result ->
                when(result) {

                    is Response.Loading -> {
                        _state.update {
                            it.copy(
                                isCatUploading = true
                            )
                        }
                    }
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                isCatUploading = false
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getCat() {
        viewModelScope.launch {
            useCases.getRandomCat().collectLatest { result ->
                when(result) {
                    is Response.Loading -> {
                        _state.update {
                            it.copy(
                                isCatLoading = true
                            )
                        }
                    }
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                isCatLoading = false,
                                cat = result.data
                            )
                        }
                    }
                    is Response.Exception -> {
                        _state.update {
                            it.copy(
                                isCatLoading = false
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getCurrentDay():Long {
        return Calendar.getInstance(Locale.getDefault()).timeInMillis
    }
}