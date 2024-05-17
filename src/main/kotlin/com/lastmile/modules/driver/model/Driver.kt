package com.lastmile.modules.driver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

@Serializable
data class Driver(
    val id: Int,
    val name: String,
)

object Drivers : IntIdTable() {
    val name = varchar("name", 50)
}