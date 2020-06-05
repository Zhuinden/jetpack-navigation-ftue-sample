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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.splash

import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.SplashGraphDirections
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector

class SplashFragment : Fragment(R.layout.splash_fragment) {
    private val handler = Handler()

    private val finishSplash: Runnable = Runnable {
        val authenticationManager = Injector.get().authenticationManager()

        if (authenticationManager.isAuthenticated()) {
            Navigation.findNavController(requireView())
                .navigate(SplashGraphDirections.splashToLoggedIn(authenticationManager.getAuthenticatedUser()))
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.splash_to_logged_out)
        }
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(finishSplash, 1L)
    }

    override fun onStop() {
        handler.removeCallbacks(finishSplash)
        super.onStop()
    }
}