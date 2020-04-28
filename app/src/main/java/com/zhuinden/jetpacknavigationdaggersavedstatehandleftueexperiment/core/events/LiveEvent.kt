/*
 * Copyright 2020 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.events

import androidx.lifecycle.*
import com.zhuinden.eventemitter.EventSource


private class LiveEvent<T> constructor(
    private val eventSource: EventSource<T>,
    private val lifecycleOwner: LifecycleOwner,
    private val observer: EventSource.EventObserver<T>
) : LifecycleEventObserver {
    init {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }

    private var isActive: Boolean = false
    private var notificationToken: EventSource.NotificationToken? = null

    private fun shouldBeActive(): Boolean {
        return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }

    private fun disposeObserver() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            stopListening()
            disposeObserver()
            return
        }
        checkIfActiveStateChanged(shouldBeActive())
    }

    private fun checkIfActiveStateChanged(newActive: Boolean) {
        if (newActive == isActive) {
            return
        }
        val wasActive = isActive
        isActive = newActive
        val isActive = isActive

        if (!wasActive && isActive) {
            stopListening()
            notificationToken = eventSource.startListening(observer)
        }

        if (wasActive && !isActive) {
            stopListening()
        }
    }

    private fun stopListening() {
        notificationToken?.stopListening()
        notificationToken = null
    }
}

fun <T> EventSource<T>.observe(lifecycleOwner: LifecycleOwner, eventObserver: (T) -> Unit) {
    LiveEvent(
        this,
        lifecycleOwner,
        EventSource.EventObserver<T> { event -> eventObserver.invoke(event) })
}