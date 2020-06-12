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

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.events.observe
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation.NavigationDispatcher

class MainActivity : AppCompatActivity() {
    private val navigationDispatcher by viewModels<NavigationDispatcher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationDispatcher.navigationCommands.observe(this) { command ->
            command.invoke(Navigation.findNavController(this, R.id.nav_host), this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) { // for sample's sake
            Injector.get().authenticationManager().clearRegistration()
        }
    }
}