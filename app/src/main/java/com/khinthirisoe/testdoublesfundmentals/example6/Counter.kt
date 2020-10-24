package com.khinthirisoe.testdoublesfundmentals.example6

class Counter {
    var totalSteps = 0

        private set
        get() {
        return instance!!.totalSteps
    }

    fun add() {
        totalSteps++
    }

    fun add(count: Int) {
        totalSteps += count
    }

    companion object {
        private var sInstance: Counter? = null

        @JvmStatic
        val instance: Counter?
            get() {
                if (sInstance == null) {
                    sInstance = Counter()
                }
                return sInstance
            }
    }
}