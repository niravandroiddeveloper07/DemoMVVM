package com.example.demomvvm.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demomvvm.login.model.LoginRequest
import com.example.demomvvm.login.model.LoginResponse
import com.example.demomvvm.network.*
import com.example.demomvvm.network.callApi

import javax.inject.Inject

class LoginViewModel @Inject constructor(var context: LoginActivity) : ViewModel() {

    @Inject
    lateinit var apiInterface: ApiInterface

    val loginResponseLiveData = MutableLiveData<LoginResponse>()

    fun callLoginApi(email: String, password: String) {
        context.callApi(
            apiInterface.login(LoginRequest(email, password)), loginResponseLiveData, true,
            showError = false
        )
    }

}
