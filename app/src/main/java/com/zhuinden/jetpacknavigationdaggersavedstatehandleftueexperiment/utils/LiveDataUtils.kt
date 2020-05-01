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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Utils to observe liveData in a cleaner way from an Activity.
 */
inline fun <T> AppCompatActivity.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    observeLifeCycle(liveData, observer)
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner way from an Activity.
 */
inline fun <T> AppCompatActivity.observeOnce(
    liveData: LiveData<T>,
    crossinline observer: (T) -> Unit
) {
    observeLifeCycleOnce(liveData, observer)
}

/**
 * Utils to observe liveData in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    viewLifecycleOwner.observeLifeCycle(liveData, observer)
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
inline fun <T> Fragment.observeOnce(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    viewLifecycleOwner.observeLifeCycleOnce(liveData, observer)
}

/**
 * Utils to observe liveData in a cleaner way from any LifecycleOwner.
 *
 * This should not be used with Fragment as lifecycleOwner
 */
inline fun <T> LifecycleOwner.observeLifeCycle(
    liveData: LiveData<T>,
    crossinline observer: (T) -> Unit
) {
    liveData.observe(this, Observer { observer(it) })
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner way from any LifecycleOwner.
 *
 * This should not be used with Fragment as lifecycleOwner
 */
inline fun <T> LifecycleOwner.observeLifeCycleOnce(
    liveData: LiveData<T>,
    crossinline observer: (T) -> Unit
) {
    liveData.observe(this, object : Observer<T> {
        override fun onChanged(t: T) {
            liveData.removeObserver(this)
            observer(t)
        }
    })
}
