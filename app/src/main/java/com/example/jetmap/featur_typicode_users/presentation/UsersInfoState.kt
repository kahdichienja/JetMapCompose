package com.example.jetmap.featur_typicode_users.presentation

import com.example.jetmap.featur_typicode_users.domain.model.UserInfo

data class UsersInfoState(val usersInfo: List<UserInfo> = emptyList(), val isLoading: Boolean = false)