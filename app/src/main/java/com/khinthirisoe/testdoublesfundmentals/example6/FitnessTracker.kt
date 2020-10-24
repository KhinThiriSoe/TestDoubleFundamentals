package com.khinthirisoe.testdoublesfundmentals.example6

import com.khinthirisoe.testdoublesfundmentals.example6.Counter.Companion.instance

class FitnessTracker {
    fun step() {
        instance!!.add()
    }

    fun runStep() {
        instance!!.add(RUN_STEPS_FACTOR)
    }

    companion object {
        const val RUN_STEPS_FACTOR = 2
    }
}