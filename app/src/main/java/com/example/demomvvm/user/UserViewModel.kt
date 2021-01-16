package com.example.demomvvm.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demomvvm.user.model.ResponseUserList
import com.example.demomvvm.network.ApiInterface
import com.example.demomvvm.network.callApi
import javax.inject.Inject

class UserViewModel @Inject constructor(val context: UserListActivity):ViewModel()
{
    @Inject
    lateinit var apiInterface: ApiInterface
    val userListLiveData= MutableLiveData<ResponseUserList>()

    fun callUserListAPi() {
        context.callApi(
            apiInterface.getUserList(), userListLiveData, true,
            showError = false
        )
    }


}