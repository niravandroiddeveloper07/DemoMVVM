package com.example.demomvvm.network

import okhttp3.ResponseBody

class ResponseWraper<T> (
     val data: T?= null,
     val t: Throwable? = null ,
     val error: ResponseBody?=null
     //or A message String, Or whatever
)