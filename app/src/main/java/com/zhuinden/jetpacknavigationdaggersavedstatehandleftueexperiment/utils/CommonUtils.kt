package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

fun Unit.safe() = Unit

fun Any.safe() = Unit
