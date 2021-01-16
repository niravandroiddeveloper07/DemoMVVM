package com.example.demomvvm.network

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  Calling Network call with Retrofit Api
 */


 fun <T> Context.callApi(
    call: Call<T>,
    liveData: MutableLiveData<T>,
    showLoading: Boolean = true,
    showError: Boolean = true, noInternetView: View?=null
) {


    call.enqueue {
        onResponse = {

            if (it.body() != null) {

                liveData.postValue(it.body())
            }
        }
        onFailure = { body, t ->

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


}


fun Context.handleInternet() {

}
