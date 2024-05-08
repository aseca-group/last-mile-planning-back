package com.lastmile.requests

import com.lastmile.models.Schedule
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*


const val SCHEDULES_ENDPOINT = "http://localhost:8080/api/schedule"
suspend fun getAllSchedules(): List<Schedule> {
    val httpClient = HttpClient()
    try {
        val response: HttpResponse = httpClient.get(SCHEDULES_ENDPOINT)
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<Schedule>>()
        }
    } catch (e: Exception) {
        throw e
    } finally {
        httpClient.close()
    }
    return emptyList()
}

suspend fun getScheduleById(id: Int): Schedule? {
    val httpClient = HttpClient()
    try {
        val response: HttpResponse = httpClient.get("$SCHEDULES_ENDPOINT/$id")
        if (response.status == HttpStatusCode.OK) {
            return response.body<Schedule>()
        }
    } catch (e: Exception) {
        throw e
    } finally {
        httpClient.close()
    }
    return null
}


@OptIn(InternalAPI::class)
suspend fun createSchedule(schedule: Schedule) {
    val httpClient = HttpClient()
    try {
        val response: HttpResponse = httpClient.post(SCHEDULES_ENDPOINT) {
           setBody(schedule)
        }
        if (response.status != HttpStatusCode.Created) {
            throw Exception("Failed to create schedule")
        }
    } catch (e: Exception) {
        throw e
    } finally {
        httpClient.close()
    }
}

suspend fun updateSchedule(schedule: Schedule) {
    val httpClient = HttpClient()
    try {
        val response: HttpResponse = httpClient.put("$SCHEDULES_ENDPOINT/${schedule.id}") {
            setBody(schedule)
        }
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Failed to update schedule")
        }
    } catch (e: Exception) {
        throw e
    } finally {
        httpClient.close()
    }
}


