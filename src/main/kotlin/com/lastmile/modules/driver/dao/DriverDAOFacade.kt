package com.lastmile.modules.driver.dao

import com.lastmile.modules.driver.model.Driver

interface DriverDAOFacade {
    suspend fun createDriver(name: String): Driver?
    suspend fun getDriver(id: Int): Driver?
    suspend fun getAllDrivers(): List<Driver>
    suspend fun deleteDriver(id: Int): Boolean
}