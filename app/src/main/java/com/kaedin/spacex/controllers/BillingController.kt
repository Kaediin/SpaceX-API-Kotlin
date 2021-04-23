package com.kaedin.spacex.controllers

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import com.android.billingclient.api.*
import com.kaedin.spacex.R
import kotlinx.android.synthetic.main.donation_views.view.*

class BillingController(private val activity: Activity, private val context: Context) : PurchasesUpdatedListener {

    private var selectedIndex: Int = 0
    private lateinit var billingClient: BillingClient

    fun run(){
        setUpBillingClient()
    }

    private fun setUpBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    println("Setup Billing Done")
                    // The BillingClient is ready. You can query purchases here.
                    queryAvaliableProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                println("Billing client Disconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun queryAvaliableProducts() {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(arrayListOf("small_donation", "medium_donation", "large_donation"))
            .setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                showDialogDonations(skuDetailsList)
            }
        }
    }

    private var consumeListener = ConsumeResponseListener { billingResult, purchaseToken ->
        val acknowledgePurchaseParams =
            AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchaseToken).build()
        billingClient.acknowledgePurchase(
            acknowledgePurchaseParams,
            acknowledgePurchaseResponseListener
        )
    }
    private var acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { billingResult ->
        println("Acknowledged: $billingResult")
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                val token = purchase.purchaseToken
                val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build()

                billingClient.consumeAsync(consumeParams, consumeListener)


                println("User handled purchase: ${purchase.orderId}")
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }

    private fun showDialogDonations(skuDetails: List<SkuDetails>) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.donations_cards, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val mMotionLayout = view.findViewById<MotionLayout>(R.id.motion_container)

        val v1 = view.findViewById<View>(R.id.v1)
        val v2 = view.findViewById<View>(R.id.v2)
        val v3 = view.findViewById<View>(R.id.v3)

        view.large_cost.text = skuDetails[0].price
        view.medium_cost.text = skuDetails[1].price
        view.small_cost.text = skuDetails[2].price

        v1.setOnClickListener {
            if (selectedIndex == 0) {
                launchBillingFlow(skuDetails)
                return@setOnClickListener
            }

            mMotionLayout.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            mMotionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) {
                launchBillingFlow(skuDetails)
                return@setOnClickListener
            }

            if (selectedIndex == 2) {
                mMotionLayout.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                mMotionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            mMotionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) {
                launchBillingFlow(skuDetails)
                return@setOnClickListener
            }

            mMotionLayout.setTransition(R.id.s2, R.id.s3) //orange to red transition
            mMotionLayout.transitionToEnd()
            selectedIndex = 2;
        }


        dialog.show()
    }

    private fun launchBillingFlow(skuDetails: List<SkuDetails>){
        val flowParams =
            BillingFlowParams.newBuilder().setSkuDetails(skuDetails[0]).build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
    }

}