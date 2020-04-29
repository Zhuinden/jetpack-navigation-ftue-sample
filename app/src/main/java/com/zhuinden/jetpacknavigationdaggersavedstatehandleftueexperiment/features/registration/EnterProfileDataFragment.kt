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
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.injection.Injector
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.events.observe
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.databinding.EnterProfileDataFragmentBinding
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.navGraphViewModel
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onClick
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onTextChanged


class EnterProfileDataFragment : Fragment(R.layout.enter_profile_data_fragment) {
    val backPressListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.onBackEvent()
        }
    }

    private val viewModel by navGraphViewModel(R.id.registration_graph) { handle ->
        Injector.get().registrationViewModelFactory().create(handle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = EnterProfileDataFragmentBinding.bind(view)
        with(binding) {
            textFullName.setText(viewModel.fullName.value)
            textBio.setText(viewModel.bio.value)

            textFullName.onTextChanged { fullName -> viewModel.fullName.value = fullName }
            textBio.onTextChanged { bio -> viewModel.bio.value = bio }

            viewModel.isEnterProfileNextEnabled.observe(viewLifecycleOwner) { enabled ->
                buttonEnterProfileNext.isEnabled = enabled
            }
            buttonEnterProfileNext.onClick { viewModel.onEnterProfileNextClicked() }
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