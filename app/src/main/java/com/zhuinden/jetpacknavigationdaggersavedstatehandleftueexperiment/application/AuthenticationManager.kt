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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationManager @Inject constructor(val sharedPref: SharedPreferences) {
    fun isAuthenticated(): Boolean =
        sharedPref.getString("username", "")!!.isNotEmpty()

    fun saveRegistration(username: String) {
        sharedPref.edit().putString("username", username).apply()
    }

    fun clearRegistration() {
        sharedPref.edit().remove("username").apply()
    }

    fun getAuthenticatedUser(): String {
        return checkNotNull(sharedPref.getString("username", "").takeIf { it!!.isNotEmpty() })
    }

    var authToken: String = "" // why would this be in the viewModel?
}