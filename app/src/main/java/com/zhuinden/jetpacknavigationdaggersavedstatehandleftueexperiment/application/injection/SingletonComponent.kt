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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection

import android.app.Application
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.AuthenticationManager
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.login.LoginViewModel
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.profile.ProfileViewModel
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.registration.RegistrationViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AssistedInjectionModule::class])
interface SingletonComponent {
    fun authenticationManager(): AuthenticationManager

    fun loginViewModelFactory(): LoginViewModel.Factory
    fun registrationViewModelFactory(): RegistrationViewModel.Factory
    fun profileViewModelFactory(): ProfileViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): SingletonComponent
    }
}