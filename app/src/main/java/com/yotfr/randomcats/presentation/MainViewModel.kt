package com.yotfr.randomcats.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    private val _startDestination = mutableStateOf(RootGraph.AUTH)
    val startDestination:State<String> = _startDestination

    private val _isLoading = mutableStateOf(true)
    val isLoading:State<Boolean> = _isLoading


    init {
        checkSign()
    }

    private fun checkSign(){
        _isLoading.value = true
        viewModelScope.launch {
            if(userUseCases.checkUserSignUseCase()) {
               _startDestination.value = RootGraph.HOME
                _isLoading.value = false
            }else {
                _startDestination.value = RootGraph.AUTH
                _isLoading.value = false
            }
        }
    }
}