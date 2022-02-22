package com.example.jetmap.feature_google_places.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmap.core.util.Resource
import com.example.jetmap.featur_typicode_users.presentation.UserInfoViewModel
import com.example.jetmap.feature_google_places.domain.use_case.GetDirectionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GooglePlacesInfoViewModel @Inject constructor(private val getDirectionInfo: GetDirectionInfo): ViewModel() {

    private val _googlePlacesInfoState = mutableStateOf(GooglePlacesInfoState())
    val googlePlacesInfoState: State<GooglePlacesInfoState> = _googlePlacesInfoState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val evenFlow = _eventFlow.asSharedFlow()

    fun getDirection(origin: String, destination: String, key: String){
        viewModelScope.launch {
            getDirectionInfo(origin = origin, destination = destination, key = key).onEach { res ->
                when(res){
                    is Resource.Success ->{
                        _googlePlacesInfoState.value = googlePlacesInfoState.value.copy(
                            direction = res.data,
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.ShowSnackBar(message = "Direction Loaded"))
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackBar(message = res.message?:"Unknown Error"))
                    }
                    is Resource.Loading -> {
                        _googlePlacesInfoState.value = googlePlacesInfoState.value.copy(
                            direction = null,
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.ShowSnackBar(message = "Loading Direction"))
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class ShowSnackBar(val message: String): UIEvent()
    }
}