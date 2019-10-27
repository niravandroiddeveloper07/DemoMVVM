package com.example.demomvvm.login.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demomvvm.model.LoginRequest
import com.example.demomvvm.model.LoginResponse
import com.example.demomvvm.network.ApiClient
import com.example.demomvvm.network.ApiInterface
import com.example.demomvvm.network.ResponseWraper
import com.example.demomvvm.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val context: Context) : ViewModel() {

    val loginResponseLiveData = MutableLiveData<ResponseWraper<LoginResponse>>()

    fun callLoginApi(email: String, password: String) {

        Util.showProgressDialog(context)

        val apiInterface = ApiClient.client!!.create(ApiInterface::class.java)
        val call = apiInterface.login(LoginRequest(email, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Util.hideProgressDialog()
                if (response.isSuccessful) {
                    loginResponseLiveData.postValue(ResponseWraper(response.body()!!))
                } else {
                    loginResponseLiveData.postValue(ResponseWraper(error = response.errorBody()))

                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                Util.hideProgressDialog()
                loginResponseLiveData.postValue(ResponseWraper(t = t))

            }
        })
    }

}
