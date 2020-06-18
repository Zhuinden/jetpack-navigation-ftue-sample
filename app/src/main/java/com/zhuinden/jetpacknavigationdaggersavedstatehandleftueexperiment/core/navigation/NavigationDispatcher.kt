package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class NavigationDispatcher {
    private val navigationEmitter: EventEmitter<NavigationCommand> = EventEmitter()
    val navigationCommands: EventSource<NavigationCommand> = navigationEmitter

    fun emit(navigationCommand: NavigationCommand) {
        navigationEmitter.emit(navigationCommand)
    }
}