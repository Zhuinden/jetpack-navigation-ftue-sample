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

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation.NavigationDispatcher
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.fragmentViewModels

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private val navigationDispatcher by activityViewModels<NavigationDispatcher>()

    private val viewModel by fragmentViewModels {
        Injector.get().profileViewModelFactory().create(navigationDispatcher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ProfileFragmentArgs.fromBundle(requireArguments())

        Toast.makeText(requireContext(), "Welcome ${args.username}!", Toast.LENGTH_LONG).show()

        viewModel.activationCheck.observe(viewLifecycleOwner) {}
    }
}