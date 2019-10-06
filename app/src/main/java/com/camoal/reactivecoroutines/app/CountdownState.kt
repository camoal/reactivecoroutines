package com.camoal.reactivecoroutines.app

sealed class CountdownState {
    class Start: CountdownState()
    class Count(val count: Int): CountdownState()
    class Error(val message: String?): CountdownState()
    class End: CountdownState()
}