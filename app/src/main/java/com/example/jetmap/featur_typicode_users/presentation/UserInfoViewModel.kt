package com.example.jetmap.featur_typicode_users.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmap.core.util.Resource
import com.example.jetmap.featur_typicode_users.domain.model.UserInfo
import com.example.jetmap.featur_typicode_users.domain.use_case.GetUsersInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserInfoViewModel @Inject constructor(private val getUsersInfo: GetUsersInfo): ViewModel(){
    private val _userInfoState = mutableStateOf(UsersInfoState())
    val usersInfoState: State<UsersInfoState> = _userInfoState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val evenFlow = _eventFlow.asSharedFlow()
    init {
        getUsers()
    }
    private fun getUsers(){
        viewModelScope.launch {
            getUsersInfo().onEach {
                res ->
                when(res){
                    is Resource.Success ->{
                        _userInfoState.value = usersInfoState.value.copy(
                            usersInfo = res.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _userInfoState.value = usersInfoState.value.copy(
                            usersInfo = res.data ?: emptyList(),
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.ShowSnackBar(message = res.message?:"Unknown Error"))
                    }
                    is Resource.Loading -> {
                        _userInfoState.value = usersInfoState.value.copy(
                            usersInfo = res.data ?: emptyList(),
                            isLoading = true
                        )
                        _eventFlow.emit(UIEvent.ShowSnackBar(message = "Loading ..."))
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class ShowSnackBar(val message: String): UIEvent()
    }
}