package com.dattingtimo.meetlandia.subscription

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.MeasureUnit.WEEK
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.android.billingclient.api.*
import com.example.demomvvm.login.ui.BaseActivity
import com.example.demomvvm.util.*

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.security.Security
import java.util.*

class SubscriptionHelper(val context: Context, val subscriptionHelper: SubscriptionHelper) : PurchasesUpdatedListener,
    BillingClientStateListener {

    private lateinit var currentSku: SkuDetails
    private var playStoreBillingClient: BillingClient? = null
    private val TAG_INAPP = "InAppPurchase"
    private var curentSkuId: String = ""
    private var isLaunchFLow: Boolean = false

    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        if (isLaunchFLow && playStoreBillingClient!!.isReady) {
            isLaunchFLow = false
            launchBillingFlow(skuId = curentSkuId)
        }
    }

    fun instantiateAndConnectToPlayBillingService() {
        playStoreBillingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases() // required or app will crash
            .setListener(this).build()
        connectToPlayBillingService()
    }

    private fun connectToPlayBillingService(): Boolean {

        if (!playStoreBillingClient!!.isReady) {
            playStoreBillingClient?.startConnection(this)
            return true
        }
        return false
    }

    fun launchBillingFlow(skuId: String) {

        if (!playStoreBillingClient!!.isReady) {
            isLaunchFLow = true
            connectToPlayBillingService()
            return
        }


        val inAppType = BillingClient.SkuType.SUBS
        val skuParam = SkuDetailsParams
            .newBuilder()
            .setType(inAppType)
            .setSkusList(listOf(skuId))
            .build()

        playStoreBillingClient!!.querySkuDetailsAsync(skuParam) { billingResult, skuDetailsList ->

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList.isNotEmpty()) {

                        skuDetailsList.forEach {

                            val oldSku = getActivePlanSku()/*getOldSku(skuDetails.sku)*/
                            currentSku = it
                            val purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(it)
                                .setOldSku(oldSku)
                                .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION)
                                .build()

                            playStoreBillingClient!!.launchBillingFlow(context as Activity, purchaseParams)
                        }
                    }
                }
                else -> {
                    CoroutineScope(Job() + Dispatchers.IO).launch {

                        //#PurchaseDialog#
                        withContext(Dispatchers.Main.immediate) {
                            //  showOkDialog(context as Activity, message = "launch="+billingResult.responseCode.toString())
                        }
                    }

                }
            }


        }

    }

    /**
     * Return current active plan sku if any, otherwise return null
     * */
    private fun getActivePlanSku(): String? {


        if (isSubscriptionSupported()) {
            val result = playStoreBillingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
            return if (result.purchasesList.isNullOrEmpty()) {
                null
            } else
                result.purchasesList[0].sku

        }
        return null
    }

    /**
     * Checks if the user's device supports subscriptions
     */
    private fun isSubscriptionSupported(): Boolean {
        val billingResult =
            playStoreBillingClient?.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        var succeeded = false
        when (billingResult!!.responseCode) {
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connectToPlayBillingService()
            BillingClient.BillingResponseCode.OK -> succeeded = true
            else -> Log.w(TAG_INAPP,
                "isSubscriptionSupported() error: ${billingResult.debugMessage}")
        }
        return succeeded
    }

    override fun onBillingServiceDisconnected() {

    }

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {

        when (billingResult?.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if(!(context as BaseActivity).isFinishing)
                    (context as BaseActivity).showProgressDialog(context)

                // will handle server verification, consumables, and updating the local cache
                purchases?.apply { processPurchases(this.toSet()) }
            }
            /*BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                //queryPurchasesAsync()

            }*/
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                connectToPlayBillingService()
            }
            else -> {
                CoroutineScope(Job() + Dispatchers.IO).launch {

                    //#PurchaseDialog#
                    withContext(Dispatchers.Main.immediate) {
                        //   showOkDialog(context as Activity, message ="purchase update="+ billingResult!!.responseCode.toString())
                    }
                }
            }
        }


    }

    private fun processPurchases(purchasesResult: Set<Purchase>) =

        CoroutineScope(Job() + Dispatchers.IO).launch {

            //#PurchaseDialog#
            withContext(Dispatchers.Main.immediate) {

            }
            Log.d(TAG_INAPP, "processPurchases called")
            val validPurchases = HashSet<Purchase>(purchasesResult.size)
            Log.d(TAG_INAPP, "processPurchases newBatch content $purchasesResult")
            purchasesResult.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    //CommonUtils.showOkDialog(application,title="In App : ",message = "Purchased success:\n ${purchase.originalJson}")
                    //TODO: Remove comment for actual product
                    if (isSignatureValid(purchase)) {
                        validPurchases.add(purchase)
                    } else {
                        //#PurchaseDialog#
                        withContext(Dispatchers.Main.immediate) {

                            showOkDialog(context as Activity, message = "Invalid purchase")
                        }
                    }
                    //validPurchases.add(purchase)
                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    //CommonUtils.showOkDialog(application,title="In App : ",message = "Purchased pending:\n ${purchase.originalJson}")
                    Log.d(TAG_INAPP, "Received a pending purchase of SKU: ${purchase.sku}")
                    // handle pending purchases, e.g. confirm with users about the pending
                    // purchases, prompt them to complete it, etc.
                    //#PurchaseDialog#
                    withContext(Dispatchers.Main.immediate) {

                        showOkDialog(context as Activity, message = "Your transaction is pending")
                    }
                }
            }

            val (consumables, nonConsumables) = validPurchases.partition {
                false

            }
            withContext(Dispatchers.Main.immediate) {
                //  handleConsumablePurchasesAsync(consumables)
                acknowledgeNonConsumablePurchasesAsync(nonConsumables)
            }

        }

    /**
     * Ideally your implementation will comprise a secure server, rendering this check
     * unnecessary. @see [Security]
     */
    private fun isSignatureValid(purchase: Purchase): Boolean {
        return Security.verifyPurchase(
            Security.BASE_64_ENCODED_PUBLIC_KEY, purchase.originalJson, purchase.signature
        )
    }

    /**
     * If you do not acknowledge a purchase, the Google Play Store will provide a refund to the
     * users within a few days of the transaction. Therefore you have to implement
     * [BillingClient.acknowledgePurchaseAsync] inside your app.
     */
    private fun acknowledgeNonConsumablePurchasesAsync(nonConsumables: List<Purchase>) {
        nonConsumables.forEach { purchase ->

            val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase
                .purchaseToken).setDeveloperPayload(PreferenceHelper.getUserObject().iUserId.toString()).build()

            playStoreBillingClient!!.acknowledgePurchase(params) { billingResult ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        //disburseNonConsumableEntitlement(purchase)
                        val time: String;
                        val c = Calendar.getInstance()
                        c.timeInMillis = purchase.purchaseTime.toTimeStamp()

                        when {
                            currentSku.subscriptionPeriod == WEEK -> {
                                time = WEEK_TEXT
                                c.add(Calendar.DATE, 7)
                            }
                            currentSku.subscriptionPeriod == MONTH -> {
                                time = MONTH_TEXT
                                c.add(Calendar.MONTH, 1)
                            }
                            currentSku.subscriptionPeriod == THREE_MONTH -> {
                                time = THREE_MONTH_TEXT
                                c.add(Calendar.MONTH, 3)
                            }
                            currentSku.subscriptionPeriod == SIX_MONTH -> {
                                time = SIX_MONTH_TEXT
                                c.add(Calendar.MONTH, 6)
                            }
                            else -> {
                                time = YEAR_TEXT
                                c.add(Calendar.YEAR, 1)
                            }
                        }

                        subscriptionHelper.onPurchased(SubscriptionHelperModel(startTime = (purchase.purchaseTime/1000).toInt().toString(), amount = (currentSku.priceAmountMicros / 1000000).toString(), currencyType = currentSku.priceCurrencyCode, orderId = purchase.orderId, deviceType = DEVICE_TYPE_ANDROID, purchaseToken = purchase.purchaseToken, tiStsatus = purchase.purchaseState.toString(), subscriptionType = time, endTime = (c.timeInMillis/1000).toInt().toString()))


                    }
                    else -> {
                        CoroutineScope(Job() + Dispatchers.IO).launch {

                            //#PurchaseDialog#
                            withContext(Dispatchers.Main.immediate) {
                                // showOkDialog(context as Activity, message = "api call="+billingResult.responseCode.toString())
                            }
                        }

                    }
                }
            }

        }
    }

    interface SubscriptionHelper {
        fun onPurchased(subscriptionHelperModel: SubscriptionHelperModel)
    }



}
