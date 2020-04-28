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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.databinding.CreateLoginCredentialsFragmentBinding
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.events.observe
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onClick
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onTextChanged


class CreateLoginCredentialsFragment : Fragment(R.layout.create_login_credentials_fragment) {
    private lateinit var viewModel: RegistrationViewModel

    val backPressListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.onBackEvent()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // see https://twitter.com/Zhuinden/status/1255204348067483648
        if(!::viewModel.isInitialized) { // cannot retrieve in `onCreate` because `NavController` is not ready (to get the NavGraph entry)
            // solution: move this logic to execute once in `onCreateView` as that is the only callback directly after onCreate()
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
            val navGraphBackstackEntry = navController.getBackStackEntry(R.id.registration_graph)

            viewModel = ViewModelProvider(
                navGraphBackstackEntry,
                Injector.get()
                    .registrationViewModelFactory()
                    .createFactory(this@CreateLoginCredentialsFragment, arguments ?: Bundle())
            ).get(RegistrationViewModel::class.java)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CreateLoginCredentialsFragmentBinding.bind(view)
        with(binding) {
            textUsername.setText(viewModel.username.value)
            textPassword.setText(viewModel.password.value)

            textUsername.onTextChanged { username -> viewModel.username.value = username }
            textPassword.onTextChanged { password -> viewModel.password.value = password }

            viewModel.isRegisterAndLoginEnabled.observe(viewLifecycleOwner) { enabled ->
                buttonRegisterAndLogin.isEnabled = enabled
            }
            buttonRegisterAndLogin.onClick { viewModel.onRegisterAndLoginClicked() }
        }

        fun setBackPressListenerDisabledIfApplicable() { // "in place of `onBackEvent()`"
            if (Navigation.findNavController(requireView()).currentDestination == null /* no entries => finish */) {
                backPressListener.isEnabled = false
            }
        }

        setBackPressListenerDisabledIfApplicable()

        viewModel.navigationCommands.observe(viewLifecycleOwner) { navigationCommand ->
            navigationCommand(Navigation.findNavController(view), requireContext())

            setBackPressListenerDisabledIfApplicable() // end "in place of `onBackEvent()`"
        }
    }
}