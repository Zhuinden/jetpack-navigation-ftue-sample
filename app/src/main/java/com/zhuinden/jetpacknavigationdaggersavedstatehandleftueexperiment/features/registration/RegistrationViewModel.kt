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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.RegistrationGraphDirections
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.AuthenticationManager
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation.NavigationDispatcher
import com.zhuinden.livedatacombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.zhuinden.livedatavalidatebykt.validateBy

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val navigationDispatcher: NavigationDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class RegistrationState { // this is actually kinda superfluous/unnecessary but ok
        COLLECT_PROFILE_DATA,
        COLLECT_USER_PASSWORD,
        REGISTRATION_COMPLETED
    }

    private var currentState: MutableLiveData<RegistrationState> =
        savedStateHandle.getLiveData("currentState", RegistrationState.COLLECT_PROFILE_DATA)

    val fullName: MutableLiveData<String> = savedStateHandle.getLiveData("fullName", "")
    val bio: MutableLiveData<String> = savedStateHandle.getLiveData("bio", "")

    val isEnterProfileNextEnabled = validateBy(
        fullName.map { it.isNotBlank() },
        bio.map { it.isNotBlank() }
    )

    val username: MutableLiveData<String> = savedStateHandle.getLiveData("username", "")
    val password: MutableLiveData<String> = savedStateHandle.getLiveData("password", "")

    val isRegisterAndLoginEnabled = validateBy(
        username.map { it.isNotBlank() },
        password.map { it.isNotBlank() }
    )

    fun onEnterProfileNextClicked() {
        if (fullName.value!!.isNotBlank() && bio.value!!.isNotBlank()) {
            currentState.value = RegistrationState.COLLECT_USER_PASSWORD
            navigationDispatcher.emit {
                navigate(R.id.enter_profile_data_to_create_login_credentials)
            }
        }
    }

    fun onRegisterAndLoginClicked() {
        if (username.value!!.isNotBlank() && password.value!!.isNotBlank()) {
            val username = username.value!!
            currentState.value = RegistrationState.REGISTRATION_COMPLETED
            authenticationManager.saveRegistration(username)
            navigationDispatcher.emit { navigate(RegistrationGraphDirections.registrationToLoggedIn(username))}
        }
    }

    fun onCreateLoginCredentialsBackEvent() {
        currentState.value = RegistrationState.COLLECT_USER_PASSWORD
        navigationDispatcher.emit { popBackStack() }
    }
}
