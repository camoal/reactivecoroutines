package com.camoal.reactivecoroutines.app

import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        viewModel = MainViewModel()
    }

    @Test
    fun failsIfTheValueDoesNotStartAtTenAndEndsAtZero() = runBlockingTest {
        viewModel
            .countdownObservable()
            .test()
            .assert(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    }
}
