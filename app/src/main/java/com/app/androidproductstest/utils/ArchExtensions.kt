package com.app.androidproductstest.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.eventObserver(liveData: LiveData<Event<T>>, action: (t: T) -> Unit) {
    liveData.observe(this, EventObserver { it?.let { t -> action(t) } })
}
