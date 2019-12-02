package com.example.demomvvm.login.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demomvvm.model.LoginRequest
import com.example.demomvvm.model.LoginResponse
import com.example.demomvvm.network.*
import com.example.demomvvm.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val context: Context) : ViewModel() {


    val loginResponseLiveData = MutableLiveData<LoginResponse>()

    fun callLoginApi(email: String, password: String) {
        context.callApi(apiInterface.login(LoginRequest(email,password)),loginResponseLiveData,true)
    }

}
