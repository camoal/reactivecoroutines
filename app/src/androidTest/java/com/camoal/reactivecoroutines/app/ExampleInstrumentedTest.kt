package com.camoal.reactivecoroutines.app

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        viewModel = MainViewModel()
    }

    @Test
    fun failsIfTheValueDoesNotStartAtTenAndEndsAtZero(){
        viewModel
            .countdownObservable()
            .test()
            .assert(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    }
}
