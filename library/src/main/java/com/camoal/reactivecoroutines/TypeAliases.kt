package com.camoal.reactivecoroutines

typealias NextListener<T> = (T) -> Unit
typealias ErrorListener = (Throwable) -> Unit
typealias CompleteListener = () -> Unit
typealias SubscribeListener = () -> Unit
typealias TerminateListener = () -> Unit
typealias Predicate<T> = (T) -> Boolean
typealias ObservableEmitter<T> = suspend Emitter<T>.() -> Unit