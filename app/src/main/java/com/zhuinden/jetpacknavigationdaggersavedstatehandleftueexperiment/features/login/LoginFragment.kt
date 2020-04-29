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
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.events.observe
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.databinding.LoginFragmentBinding
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.fragmentSavedStateViewModels
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onClick
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onTextChanged


class LoginFragment : Fragment(R.layout.login_fragment) {
    private val viewModel by fragmentSavedStateViewModels { handle ->
        Injector.get().loginViewModelFactory().create(handle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = LoginFragmentBinding.bind(view)
        with(binding) {
            textUsername.setText(viewModel.username.value)
            textPassword.setText(viewModel.password.value)

            textUsername.onTextChanged { username -> viewModel.username.value = username }
            textPassword.onTextChanged { password -> viewModel.password.value = password }
            buttonLogin.onClick { viewModel.onLoginClicked() }
            buttonRegister.onClick { viewModel.onRegisterClicked() }

            viewModel.errorEvents.observe(viewLifecycleOwner) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }

            viewModel.navigationCommands.observe(viewLifecycleOwner) { navigationCommand ->
                navigationCommand(Navigation.findNavController(view), requireContext())
            }
        }
    }
}