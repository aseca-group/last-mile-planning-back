package aseca.lastmile.modules.driver.dao

import aseca.lastmile.modules.driver.model.CreateDriverDTO
import aseca.lastmile.modules.driver.model.Driver

interface DriverDAOFacade {
    suspend fun createDriver(driverDTO: CreateDriverDTO): Driver?
    suspend fun getDriver(id: Int): Driver?
    suspend fun getAllDrivers(): List<Driver>
    suspend fun deleteDriver(id: Int): Boolean
}