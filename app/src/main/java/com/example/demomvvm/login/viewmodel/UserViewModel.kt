package com.example.demomvvm.login.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demomvvm.database.User
import com.example.demomvvm.model.ResponseUserList
import com.example.demomvvm.network.ApiClient
import com.example.demomvvm.network.ApiInterface
import com.example.demomvvm.network.ResponseWraper
import com.example.demomvvm.util.MyApp
import com.example.demomvvm.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(val context:Context):ViewModel()
{
    val userListLiveData= MutableLiveData<ResponseWraper<List<User>>>()

    fun callUserListAPi() {

        Util.showProgressDialog(context)

        val apiInterface = ApiClient.client!!.create(ApiInterface::class.java)
        val call = apiInterface.getUserList()

        call.enqueue(object : Callback<ResponseUserList> {
            override fun onResponse(
                call: Call<ResponseUserList>,
                response: Response<ResponseUserList>
            ) {
                Util.hideProgressDialog()

                val userList = response.body()!!.data

                CoroutineScope(Dispatchers.IO).launch {

                    for (user in userList) {
                        MyApp.userDao.insert(user = user)
                    }

                    userListLiveData.postValue(ResponseWraper(userList))
                }
            }

            override fun onFailure(
                call: Call<ResponseUserList>,
                t: Throwable
            ) {
                Util.hideProgressDialog()
                userListLiveData.postValue(ResponseWraper(t=t))

            }
        })
    }

}