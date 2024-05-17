package com.lastmile.modules.driver.dao

import com.lastmile.db.DatabaseSingleton.dbQuery
import com.lastmile.modules.driver.model.Driver
import com.lastmile.modules.driver.model.Drivers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DriverDAOFacadeImpl : DriverDAOFacade {
    override suspend fun createDriver(name: String): Driver? = dbQuery {
        val insertStatement = Drivers.insert {
            it[Drivers.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToDriver)
    }

    override suspend fun getDriver(id: Int): Driver? = dbQuery {
        Drivers.select { Drivers.id eq id }.map(::resultRowToDriver).singleOrNull()
    }

    override suspend fun getAllDrivers(): List<Driver> = dbQuery {
        Drivers.selectAll().map(::resultRowToDriver)
    }

    override suspend fun deleteDriver(id: Int): Boolean = dbQuery {
        Drivers.deleteWhere { Drivers.id eq id } > 0
    }

    private fun resultRowToDriver(row: ResultRow): Driver {
        return Driver(
            id = row[Drivers.id].value,
            name = row[Drivers.name]
        )
    }
}

val driverDao: DriverDAOFacade = DriverDAOFacadeImpl()