package com.example.demomvvm.util

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class SubscriptionHelperModel(
        var startTime: String? = null,
        var endTime: String? = null,
        var purchaseToken: String? = null,
        var orderId: String? = null,
        var currencyType: String? = null,
        var amount: String? = null,
        var subscriptionType: String? = null,
        var tiStsatus: String? = null ,
        var deviceType: String? = null
)
