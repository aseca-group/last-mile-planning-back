package com.example.model

import kotlinx.serialization.Serializable

@Serializable
class Driver(val id: Int, val name: String) {
    fun startSchedule(schedule: Schedule) {
        schedule.start()
    }

}