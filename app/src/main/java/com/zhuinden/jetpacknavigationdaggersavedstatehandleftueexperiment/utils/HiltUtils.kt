package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.EntryPoints
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.internal.GeneratedComponentManager
import dagger.hilt.internal.GeneratedComponentManagerHolder

typealias HiltAssisted = androidx.hilt.Assisted

@Deprecated(
    level = DeprecationLevel.ERROR,
    message = "Does not correctly restore state after process death.",
    replaceWith = ReplaceWith("")
)
inline fun <reified T : ViewModel> Fragment.hiltNavGraphViewModels(@IdRes navGraphIdRes: Int) =
    viewModels<T>(
        ownerProducer = { findNavController().getBackStackEntry(navGraphIdRes) },
        factoryProducer = { defaultViewModelProviderFactory }
    )

inline fun <reified T> Fragment.accessor(): T {
    try {
        return EntryPointAccessors.fromFragment(this, T::class.java)
    } catch (e: Throwable) {
    }

    try {
        return EntryPointAccessors.fromActivity(requireActivity(), T::class.java)
    } catch (e: Throwable) {
    }

    try {
        val activity = requireActivity() as GeneratedComponentManagerHolder
        val activityComponentManager = activity.componentManager()

        @Suppress("UNCHECKED_CAST")
        val retainedComponentManager =
            activityComponentManager.javaClass.getDeclaredField("activityRetainedComponentManager")
                .let { accessorField ->
                    accessorField.isAccessible = true
                    accessorField.get(activityComponentManager)
                } as GeneratedComponentManager<ActivityRetainedComponent>

        return EntryPoints.get(retainedComponentManager, T::class.java)
    } catch (e: Throwable) {
    }

    try {
        return EntryPointAccessors.fromApplication(
            requireContext().applicationContext,
            T::class.java
        )
    } catch (e: Throwable) {
    }

    throw IllegalStateException("Could not find ${T::class.java.name} as EntryPoint on any of the inherited components")
}