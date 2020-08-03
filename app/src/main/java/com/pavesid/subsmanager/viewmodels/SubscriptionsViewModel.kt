package com.pavesid.subsmanager.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.subsmanager.database.SubscriptionDbHelper
import com.pavesid.subsmanager.models.Subscription
import com.pavesid.subsmanager.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscriptionsViewModel : ViewModel() {
    private val mutableSubscriptionsLiveData: MutableLiveData<List<Subscription>> =
        MutableLiveData()
    val subscriptionsLiveData: LiveData<List<Subscription>> = mutableSubscriptionsLiveData

    private var subscriptions: MutableList<Subscription> = arrayListOf()

    private val database =
        SubscriptionDbHelper(MainActivity.mainActivity)

    init {
        getAllSubscriptions()
    }

    @MainThread
    private fun createAllCosts() {
        var price = 0f
        var needPrice = 0f
        var minTimes = Long.MAX_VALUE
        var minName = ""
        subscriptions.forEach {
            price += it.price.toFloat()
            if (minTimes > it.ended.toLong()) {
                minTimes = it.ended.toLong()
                minName = it.title
                needPrice = it.price.toFloat()
            } else if (minTimes == it.ended.toLong()) {
                needPrice += it.price.toFloat()
            }
        }
        subscriptions.add(
            0, Subscription(
                title = minName,
                started = "0",
                ended = minTimes.toString(),
                price = price.toString(),
                needPrice = needPrice,
                type = 1
            )
        )
    }

    @MainThread
    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch(Dispatchers.IO) {
            database.addSubscription(subscription)
        }
        getAllSubscriptions()
    }

    @MainThread
    fun getSubscription(position: Int): Subscription = subscriptions[position]

    @MainThread
    fun deleteSubscription(subscription: Subscription) {
        viewModelScope.launch(Dispatchers.IO) {
            database.removeSubscription(subscription)
        }
        getAllSubscriptions()
    }

    @MainThread
    private fun getAllSubscriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = database.getListOfSubscription()
            subscriptions.clear()
            subscriptions.addAll(list)
            if (subscriptions.isNotEmpty()) {
                createAllCosts()
            }
            mutableSubscriptionsLiveData.postValue(subscriptions)
        }
    }

}