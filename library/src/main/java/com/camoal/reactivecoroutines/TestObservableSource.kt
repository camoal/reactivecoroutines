package com.camoal.reactivecoroutines

interface TestObservableSource<T> {

    /**
     * Check if the values are correct and otherwise throw an AssertionError.
     *
     * @param values ​​that are checked in the test.
     * @see java.lang.AssertionError
     */
    fun assert(vararg values: T)
}
