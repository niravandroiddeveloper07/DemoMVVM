package com.example.demomvvm.network

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.demomvvm.util.Util
import com.example.demomvvm.util.Util.hideProgressDialog
import com.example.demomvvm.util.Util.showProgressDialog
import com.example.demomvvm.util.toast

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  Calling Network call with Retrofit Api
 */
val apiInterface = ApiClient.client!!.create(ApiInterface::class.java)!!

fun <T> Context.callApi(call: Call<T>,liveData : MutableLiveData<T>,showLoading:Boolean=true,showError:Boolean=true) {

    if (!Util.isNetworkConnected(this)) {
      //  networkError?.invoke()
        handleInternet()
        return
    }

    if (showLoading)
       showProgressDialog(this)

    call.enqueue {
        onResponse = {
            hideProgressDialog()
            if (it.body() != null) {
                liveData.postValue(it.body())
            }
        }
        onFailure = { body, t ->
           hideProgressDialog()
            if (showError)
                handleFailure(t)

        }
    }
}

fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((body: ResponseBody?, t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(null, t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onResponse?.invoke(response)
        } else {
            onFailure?.invoke(response.errorBody(), null)
        }
    }

}

/**
 *  Handle Network failure here
 */
fun Context.handleFailure(t: Throwable?) {

    toast("Something went wrong")
}


fun Context.handleInternet() {
    toast("No internet connection")
}
