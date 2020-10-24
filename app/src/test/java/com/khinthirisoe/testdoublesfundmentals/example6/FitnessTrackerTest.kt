package com.khinthirisoe.testdoublesfundmentals.example6

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FitnessTrackerTest {

    lateinit var SUT: FitnessTracker

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = FitnessTracker()
    }

    @Test
    @Throws(Exception::class)
    fun step_totalIncremented() {
        SUT.step()
        Assert.assertThat(SUT.step(), `is`(1))
    }

    @Test
    @Throws(Exception::class)
    fun runStep_totalIncrementedByCorrectRatio() {
        SUT.runStep()
        Assert.assertThat(SUT.runStep(), `is`(2))
    }
}