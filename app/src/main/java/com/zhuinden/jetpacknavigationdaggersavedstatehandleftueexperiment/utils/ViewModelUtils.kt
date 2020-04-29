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

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.savedstate.SavedStateRegistryOwner

inline fun <reified T : ViewModel> SavedStateRegistryOwner.createAbstractSavedStateViewModelFactory(
    arguments: Bundle,
    crossinline creator: (SavedStateHandle) -> T
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory(this, arguments) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String, modelClass: Class<T>, handle: SavedStateHandle
        ): T = creator(handle) as T
    }
}

inline fun <reified T : ViewModel> Fragment.navGraphSavedStateViewModels(
    @IdRes navGraphId: Int,
    crossinline creator: (SavedStateHandle) -> T
): Lazy<T> {
    // Wrapped in lazy to not search the NavController each time we want the backStackEntry
    val backStackEntry by lazy { findNavController().getBackStackEntry(navGraphId) }

    return createViewModelLazy(T::class, storeProducer = {
        backStackEntry.viewModelStore
    }, factoryProducer = {
        backStackEntry.createAbstractSavedStateViewModelFactory(
            arguments = arguments ?: Bundle(), creator = creator
        )
    })
}

inline fun <reified T : ViewModel> Fragment.fragmentSavedStateViewModels(
    crossinline creator: (SavedStateHandle) -> T
): Lazy<T> {
    return createViewModelLazy(T::class, storeProducer = {
        viewModelStore
    }, factoryProducer = {
        createAbstractSavedStateViewModelFactory(arguments ?: Bundle(), creator)
    })
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.fragmentViewModels(
    crossinline creator: () -> T
): Lazy<T> {
    return createViewModelLazy(T::class, storeProducer = {
        viewModelStore
    }, factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(
                modelClass: Class<T>
            ): T = creator.invoke() as T
        }
    })
}
