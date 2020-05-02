# Jetpack Navigation + Fragments + NavGraphs + SavedStateHandle + Dagger + AssistedInject + EventEmitter (toasts / navigation commands)

This is a sample repository for the aforementioned tools (primarily Jetpack Navigation + NavGraph-scoped ViewModel + SavedStateHandle + Dagger + AssistedInject).

The sample is based on the FTUE-rewrite in [Simple-Stack Tutorial: First-Time User Experience with RxJava](https://github.com/Zhuinden/simple-stack-tutorials/tree/93554f7000efe49fca39de7ca707eb6843a5eaf8/app/src/main/java/com/zhuinden/simplestacktutorials/steps/step_9).

. . .

The original version of the "FTUE sample" is based on the ["Conditional Navigation" section of Jetpack Navigation documentation](https://developer.android.com/guide/navigation/navigation-conditional#first-time_user_experience).

Please beware that the original FTUE approach outlined in the documentation of Jetpack Navigation has various anti-patterns that are fixed in this sample:

- Jetpack Navigation docs uses `by activityViewModels()` instead of properly scoping the ViewModel to the `NavBackStackEntry` of the `registration_graph`

- Jetpack Navigation docs does NOT care about process death at all, while this sample uses the `lifecycle-viewmodel-savedstate` artifact and `SavedStateHandle` to properly survive across process death

- Jetpack Navigation docs claim you must have the `startDestination` on your graph at all times, in this sample we can clearly see a "splash" to make conditional navigation based on auth state.

- In general, Google samples rely on `LiveData<Event<T>>` (which should be considered an anti-pattern) and delegate Navigation responsibilities to the Fragment, in this sample we rely on `EventEmitter` to invoke `NavController` methods from the `ViewModel` directly.

## License

    Copyright 2020 Gabor Varadi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
