package com.example.demomvvm.network

import com.example.demomvvm.login.model.LoginRequest
import com.example.demomvvm.login.model.LoginResponse
import com.example.demomvvm.user.model.ResponseUserList

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiInterface {

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/api/users?")
    fun getUserList(): Call<ResponseUserList>

}