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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.AuthenticationManager
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation.NavigationCommand
import javax.inject.Inject

class ProfileViewModel(
    private val authenticationManager: AuthenticationManager
): ViewModel() {
    @Suppress("UNCHECKED_CAST")
    class VmFactory @Inject constructor(private val authenticationManager: AuthenticationManager): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ProfileViewModel(authenticationManager) as T
    }

    private val navigationEmitter: EventEmitter<NavigationCommand> = EventEmitter()
    val navigationCommands: EventSource<NavigationCommand> get() = navigationEmitter

    val activationCheck: LiveData<Unit> = object: LiveData<Unit>(Unit) {
        override fun onActive() {
            if (!authenticationManager.isAuthenticated()) {
                navigationEmitter.emit { navController, context ->
                    navController.navigate(R.id.logged_in_to_logged_out)
                }
            }
        }
    }
}